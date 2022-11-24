package one.beefsupreme.shibachatandroid.ui.friendsscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.apolloStore
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AllUsersQuery
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.MeQuery
import one.beefsupreme.shibachatandroid.SendFRequestMutation
import one.beefsupreme.shibachatandroid.repo.LoginState
import one.beefsupreme.shibachatandroid.repo.Me
import javax.inject.Inject

private const val TAG = "**FRequestsViewModel**"

sealed class FRequestsUiEvent {
//  object RefreshBtnClick: FriendsUiEvent()
  class SendFReqBtnClick(val friendId: Int): FRequestsUiEvent()
}

sealed class AllUsersResult {
  object Loading : AllUsersResult()
  class Failed(val error: ApolloException) : AllUsersResult()
  class Success(val data: AllUsersQuery.Data) : AllUsersResult()
}

sealed class SendFRequestResult {
  object Ready: SendFRequestResult()
  object Loading : SendFRequestResult()
  class Failed(val error: ApolloException) : SendFRequestResult()
  class Success(val data: SendFRequestMutation.Data) : SendFRequestResult()
}

@HiltViewModel
class FRequestsViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val appDispatchers: AppDispatchers,
  val loginState: LoginState,
  val me: Me
): ViewModel() {
  // Has to be MutableStateFlow because its value is changed in the init block
  private val _allUsersResult: MutableStateFlow<AllUsersResult>
    = MutableStateFlow(AllUsersResult.Loading)
  val allUsersResult: StateFlow<AllUsersResult> = _allUsersResult.asStateFlow()

  // Prefer Mutable state otherwise
  var sendFRequestResult: SendFRequestResult
    by mutableStateOf(SendFRequestResult.Ready)
      private set

  init { allUsers() }

  fun handle(event: FRequestsUiEvent) {
    when (event) {
      is FRequestsUiEvent.SendFReqBtnClick -> sendFRequest(event.friendId)
    }
  }

  private fun allUsers() {
    viewModelScope.launch(appDispatchers.io) {
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      apolloClient
        .query(AllUsersQuery())
        .fetchPolicy(FetchPolicy.NetworkOnly)
        .watch()
        .map {
          val data = it.data
          if (data?.users == null) {
            AllUsersResult.Failed(ApolloException("what happened?"))
          } else {
            AllUsersResult.Success(data)
          }
        }
        .collect {
          ensureActive()
          _allUsersResult.value = it
        }
    }

    Log.v(TAG, "AllUsersQuery just ran")
  }

  private fun sendFRequest(friendId: Int) {
    // TODO: Needs to use mutex to protect against multiple button clicks after protecting it UI wise first
    viewModelScope.launch(appDispatchers.io) {
      sendFRequestResult = SendFRequestResult.Loading

      sendFRequestResult = try {
        val data = apolloClient
          .mutation(SendFRequestMutation(friendId))
          .execute()
          .dataAssertNoErrors
        // Mutation was successful

        /** Modifying MeQuery cache (SentFRequest) */
        val ( // Grabs all properties of data.sendFRequest
          __typename,
          meId,
          _, // would have been friendId, but that is already available as parameter
          sentFRequestFragment
        ) = data.sendFRequest // is SendFRequestMutation.SendFRequest

        // Uses above properties to construct a new MeQuery.SentFRequest
        // This works because MeQuery.SentFRequest has the same shape
        // as SendFRequestMutation.SendFRequest
        val newSentFRequest = MeQuery.SentFRequest(
          __typename,
          meId,
          friendId,
          sentFRequestFragment
        )

        // Reads MeQuery data out of the cache.
        var meData = apolloClient.apolloStore.readOperation(MeQuery())
        // Turns it into a mutable list
        val sentFRequests = meData.user.sentFRequests.toMutableList()
        // Appends newSentFRequest the mutable list
        sentFRequests.add(newSentFRequest)
        // Alters meData by replacing the sentFRequests list with the new list
        meData = meData.copy(
          user = meData.user.copy(
            sentFRequests = sentFRequests.toList()
          )
        )

        // Write the altered MeQuery data that includes the just sent FRequest
        // into the cache
        apolloClient.apolloStore.writeOperation(
          operation = MeQuery(),
          operationData = meData
        )

        /**
         * Modifying AllUsersQuery cache by changing one of the user's
         * receivedFReqFromMe property to true
         */
        val allUsersData = apolloClient.apolloStore.readOperation(AllUsersQuery())

        // Replaces the user element to whom the friend request was sent with the
        // copy of that element with the property receivedFReqFromMe changed to true
        val users = allUsersData.users.toMutableList()
        val i = users.indexOfFirst { it.id == friendId }
        if (i >= 0 ) { // Above returns -1 if not found
          val friend = users.removeAt(i)
          users.add(i, friend.copy(receivedFReqFromMe = true))
        }

        apolloClient.apolloStore.writeOperation(
          operation = AllUsersQuery(),
          operationData = allUsersData.copy(users = users.toList())
        )

        SendFRequestResult.Success(data)
      } catch (error: ApolloException) {
        Log.e(TAG, error.message.toString())
        SendFRequestResult.Failed(error)
      }

      // For slowing down UI to take a closer look at the loading UI
//      delay(TimeUnit.MILLISECONDS.toMillis(750))

      // Flips the result object back to Ready
      sendFRequestResult = SendFRequestResult.Ready
    }
  }
}
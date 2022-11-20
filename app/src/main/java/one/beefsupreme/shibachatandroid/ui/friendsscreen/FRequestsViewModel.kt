package one.beefsupreme.shibachatandroid.ui.friendsscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AllUsersQuery
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.SendFRequestMutation
import one.beefsupreme.shibachatandroid.repo.Me
import java.util.concurrent.TimeUnit
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
      _allUsersResult.value = try {
        val data = apolloClient
          .query(AllUsersQuery())
          .fetchPolicy(FetchPolicy.NetworkOnly)
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Fetch was successful.
        // Login mutation always returns an access token in this case.
        AllUsersResult.Success(data)
      } catch (error: ApolloException) {
        AllUsersResult.Failed(error)
      }
    }

    Log.v(TAG, "AllUsersQuery just ran")
  }

  private fun sendFRequest(friendId: Int) {
    // Needs to use mutex to protect against multiple button clicks after protecting it UI wise first
    viewModelScope.launch(appDispatchers.io) {
      sendFRequestResult = SendFRequestResult.Loading

      sendFRequestResult = try {
        val data = apolloClient
          .mutation(SendFRequestMutation(friendId))
          .execute()
          .dataAssertNoErrors

        SendFRequestResult.Success(data)
      } catch (error: ApolloException) {
        SendFRequestResult.Failed(error)
      }

      // SendFRequestResult needs to be flipped back to Ready before the buttons will work again.
      // delays for 3/4 of a second, then flips it back to Ready. This delay is really needed.
      delay(TimeUnit.MILLISECONDS.toMillis(750))
      sendFRequestResult = SendFRequestResult.Ready
    }
  }
}
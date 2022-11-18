package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AllUsersQuery
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.SendFRequestMutation
import one.beefsupreme.shibachatandroid.repo.Me
import javax.inject.Inject

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
  private val _allUsersResult: MutableStateFlow<AllUsersResult> = MutableStateFlow(AllUsersResult.Loading)
  val allUsersResult: StateFlow<AllUsersResult> = _allUsersResult.asStateFlow()

  // Prefer Mutable state otherwise
  var sendFRequestResult: SendFRequestResult by mutableStateOf(SendFRequestResult.Ready)
    private set

  init { refresh() }

  fun handle(event: FRequestsUiEvent) {
    when (event) {
      is FRequestsUiEvent.SendFReqBtnClick -> sendFRequest(event.friendId)
    }
  }

  private fun refresh() {
    viewModelScope.launch(appDispatchers.io) {
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      _allUsersResult.value = try {
        val data = apolloClient
          .query(AllUsersQuery())
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Fetch was successful.
        // Login mutation always returns an access token in this case.
        AllUsersResult.Success(data)
      } catch (error: ApolloException) {
        AllUsersResult.Failed(error)
      }
    }
  }

  private fun sendFRequest(friendId: Int) {
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
    }
  }
}
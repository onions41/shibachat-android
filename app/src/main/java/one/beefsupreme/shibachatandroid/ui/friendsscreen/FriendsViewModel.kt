package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AllUsersQuery
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.SendFRequestMutation
import one.beefsupreme.shibachatandroid.repo.MeFetch
import javax.inject.Inject

sealed class FReqUiEvent {
  object RefreshBtnClick: FReqUiEvent()
  class SendFReqBtnClick(val friendId: Int): FReqUiEvent()
}

sealed class FReqUiState {
  object Loading : FReqUiState()
  class Error(val e: ApolloException) : FReqUiState()
  class Success(val allUsers: List<AllUsersQuery.User>) : FReqUiState()
}

@HiltViewModel
class FriendsViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val appDispatchers: AppDispatchers,
  val meFetch: MeFetch
): ViewModel() {
  var state: MutableStateFlow<FReqUiState> = MutableStateFlow(FReqUiState.Loading)
    private set

  init { refresh() }

  fun handle(event: FReqUiEvent) {
    when (event) {
      is FReqUiEvent.RefreshBtnClick -> refresh()
      is FReqUiEvent.SendFReqBtnClick -> sendFRequest(event.friendId)
    }
  }

  private fun sendFRequest(friendId: Int) {
    viewModelScope.launch(appDispatchers.default) {
      try {
        apolloClient
          .mutation(SendFRequestMutation(friendId))
          .execute()
          .dataAssertNoErrors
      } catch (e: ApolloException) {}
      // TODO
    }
  }

  private fun refresh() {
    viewModelScope.launch(appDispatchers.default) {
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      state.value = try {
        val data = apolloClient
          .query(AllUsersQuery())
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Fetch was successful.
        // Login mutation always returns an access token in this case.
        FReqUiState.Success(data.users)
      } catch (e: ApolloException) {
        FReqUiState.Error(e)
      }
    }
  }
}
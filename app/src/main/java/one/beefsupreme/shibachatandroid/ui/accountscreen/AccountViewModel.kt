package one.beefsupreme.shibachatandroid.ui.accountscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AllUsersQuery
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.LogoutMutation
import one.beefsupreme.shibachatandroid.repo.LoginState
import one.beefsupreme.shibachatandroid.repo.MeFetch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val appDispatchers: AppDispatchers,
  private val loginState: LoginState,
  private val meFetch: MeFetch
): ViewModel() {
  var state: AccountUiState by mutableStateOf(AccountUiState.Loading)

  init {
    viewModelScope.launch(appDispatchers.io) {
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      try { // Don't lift assignment, makes more sense like this in this case.
        val data = apolloClient
          .query(AllUsersQuery())
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Fetch was successful.
        // Login mutation always returns an access token in this case.
        state = AccountUiState.Success(data.users)

      } catch (e: ApolloException) {
        state = AccountUiState.Error(e)
      }
    }
  }

  fun handle(event: AccountUiEvent) {
    when (event) {
      is AccountUiEvent.LogoutBtnClk -> logout()
      is AccountUiEvent.SendFriendReqBtnClk -> {/* TODO */}
    }
  }

  /**
   * Private functions called by handle()
   */

  private fun logout() {
    viewModelScope.launch(appDispatchers.io) {
      // The only possible error should be a network error,
      // in which case, the refresh token will not have cleared.
      // Doesn't really matter presently.
      apolloClient.mutation(LogoutMutation()).execute()
    }
    loginState.logout()
    meFetch.stop()
  }
}
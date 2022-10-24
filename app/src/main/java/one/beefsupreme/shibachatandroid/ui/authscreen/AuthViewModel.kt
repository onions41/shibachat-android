package one.beefsupreme.shibachatandroid.ui.authscreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.LoginMutation
import one.beefsupreme.shibachatandroid.ProtectedQuery
import one.beefsupreme.shibachatandroid.UnprotectedQuery
import one.beefsupreme.shibachatandroid.repo.LoginStateImpl
import javax.inject.Inject

private const val TAG = "**AuthViewModel**"

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val loginState: LoginStateImpl
): ViewModel() {
  var state by mutableStateOf(AuthUiState())
  val isLoggedIn
    get() = loginState.isLoggedIn

  // Handles all AuthUiEvents
  fun handle(event: AuthUiEvent) {
    when (event) {
      // Button press events. Calls respective methods.
      is AuthUiEvent.LoginButtonPress -> login()
      is AuthUiEvent.ProtectedButtonPress -> protected()
      is AuthUiEvent.UnprotectedButtonPress -> unprotected()
      is AuthUiEvent.RegisterButtonPress -> {}

      // TextField text change events. No need for own methods.
      is AuthUiEvent.LoginNicknameChange -> {
        state = state.copy(loginNickname = event.value)
      }
      is AuthUiEvent.LoginPasswordChange -> {
        state = state.copy(loginPassword = event.value)
      }
      is AuthUiEvent.RegisterNicknameChange -> {
        state = state.copy(registerNickname = event.value)
      }
      is AuthUiEvent.RegisterPasswordChange -> {
        state = state.copy(registerPassword = event.value)
      }
    }
  }

  /**
   * Below functions are private, they are only used by the handle above.
   */

  private fun login() {
    viewModelScope.launch {
      // Need to catch errors here, otherwise the app will crash if the server is not on.
      val response = apolloClient.mutation(LoginMutation("Homer", "123456")).execute()

      val newAccessToken: String? = response.data?.login?.accessToken

      if (newAccessToken != null) {
        Log.v(TAG, "Got a new access token from the login mutation!")
        // Stores the access token fetched from the Login mutation
        loginState.login(newAccessToken)
      } else {
        Log.v(TAG, "No newAccessToken was returned by the login mutation")
      }
    }
  }

  private fun protected() {
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.query(ProtectedQuery()).execute()
      val data = response.data?.protected ?: "response.data.unprotected was null"
      Log.v(TAG, (if (data == true) "true" else "false or null, something wrong"))
    }
  }

  private fun unprotected() {
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.query(UnprotectedQuery()).execute()
      val data = response.data?.unprotected ?: "response.data.unprotected was null"
      Log.v(TAG, (if (data == true) "true" else "false or null, something wrong"))
    }
  }
}
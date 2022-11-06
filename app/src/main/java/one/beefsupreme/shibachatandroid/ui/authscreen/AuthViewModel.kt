package one.beefsupreme.shibachatandroid.ui.authscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.LoginMutation
import one.beefsupreme.shibachatandroid.RegisterMutation
import one.beefsupreme.shibachatandroid.repo.LoginStateImpl
import javax.inject.Inject

private const val TAG = "**AuthViewModel**"

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val loginState: LoginStateImpl
): ViewModel() {
  var state by mutableStateOf(AuthUiState())

  // Handles all AuthUiEvents
  fun handle(event: AuthUiEvent) {
    when (event) {
      // Button press events. Calls respective methods.
      is AuthUiEvent.SubmitButtonPress -> {
        when (state.form) {
          Form.LOGIN -> login()
          Form.REGISTER -> register()
        }
      }
      is AuthUiEvent.SwitchFormButtonPress -> switchForm()

      // TextField text change events. No need for own methods.
      is AuthUiEvent.NicknameInputChange -> {
        state = state.copy(nicknameInput = event.value, errors = null)
      }
      is AuthUiEvent.PasswordInputChange -> {
        state = state.copy(passwordInput = event.value, errors = null)
      }
    }
  }

  /**
   * Below functions are private, they are only used by the handle above.
   */

  private fun login() {
    viewModelScope.launch {
      state = state.copy(loading = true)
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      try { // Don't lift assignment, makes more sense like this in this case.
        val data = apolloClient
          .mutation(LoginMutation(state.nicknameInput, state.passwordInput))
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Login was successful.
        // Login mutation always returns an access token in this case.
        val newAccessToken = data.login
        loginState.login(newAccessToken)
        state = state.copy(
          nicknameInput = "",
          passwordInput = "",
          loading = false,
        )

      } catch (e: ApolloException) {
        state = state.copy(errors = ApolloException(), loading = false)
      }
    }
  }

  private fun register() {
    viewModelScope.launch {
      // ApolloClient throws only network errors by default. Must specify dataAssertNoErrors
      // in order for ApolloClient to throw both network errors and errors thrown in
      // the backend resolvers. Otherwise, ApolloClient will return an errors object that
      // contains the resolver errors separately. I don't want them separately at the moment.
      try { // Don't lift assignment, makes more sense like this in this case.
        val data = apolloClient
          .mutation(RegisterMutation(state.nicknameInput, state.passwordInput))
          .execute()
          .dataAssertNoErrors // Because I want apolloClient to throw all errors

        // Register was successful.
        // Register mutation always returns an access token in this case.
        val newAccessToken = data.register
        loginState.login(newAccessToken)
        state = state.copy(
          nicknameInput = "",
          passwordInput = "",
          loading = false,
        )
      } catch (e: ApolloException) {
        state = state.copy(errors = ApolloException(), loading = false)
      }
    }
  }

  private fun switchForm() {
    state = when (state.form) {
      Form.LOGIN -> state.copy(form = Form.REGISTER)
      Form.REGISTER -> state.copy(form = Form.LOGIN)
    }
  }
}
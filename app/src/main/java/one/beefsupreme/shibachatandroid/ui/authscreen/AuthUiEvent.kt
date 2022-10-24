package one.beefsupreme.shibachatandroid.ui.authscreen

sealed class AuthUiEvent {
  // Login form events
  data class LoginNicknameChange(val value: String): AuthUiEvent()
  data class LoginPasswordChange(val value: String): AuthUiEvent()
  object LoginButtonPress: AuthUiEvent()

  // Register form events
  data class RegisterNicknameChange(val value: String): AuthUiEvent()
  data class RegisterPasswordChange(val value: String): AuthUiEvent()
  object RegisterButtonPress: AuthUiEvent()

  // For testing
  object ProtectedButtonPress: AuthUiEvent()
  object UnprotectedButtonPress: AuthUiEvent()
}

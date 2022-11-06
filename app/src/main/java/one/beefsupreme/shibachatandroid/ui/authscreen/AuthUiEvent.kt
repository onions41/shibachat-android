package one.beefsupreme.shibachatandroid.ui.authscreen

sealed class AuthUiEvent {
  object SubmitButtonPress: AuthUiEvent()
  object SwitchFormButtonPress: AuthUiEvent()
  data class NicknameInputChange(val value: String): AuthUiEvent()
  data class PasswordInputChange(val value: String): AuthUiEvent()
}

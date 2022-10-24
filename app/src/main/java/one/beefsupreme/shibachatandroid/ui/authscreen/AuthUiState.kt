package one.beefsupreme.shibachatandroid.ui.authscreen

data class AuthUiState(
  val isLoading: Boolean = false,
  val loginNickname: String = "",
  val loginPassword: String = "",
  val registerNickname: String = "",
  val registerPassword: String = ""
)
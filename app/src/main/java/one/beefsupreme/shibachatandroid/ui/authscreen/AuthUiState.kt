package one.beefsupreme.shibachatandroid.ui.authscreen

import com.apollographql.apollo3.exception.ApolloException

data class AuthUiState(
  val loading: Boolean = false,
  val nicknameInput: String = "",
  val passwordInput: String = "",
  val errors: ApolloException? = null,
  val form: Form = Form.LOGIN
)

enum class Form {
  REGISTER, LOGIN
}
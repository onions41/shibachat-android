package one.beefsupreme.shibachatandroid

object AccessTokenStorage {
  private var accessToken: String = ""
  fun getAccessTok(): String = accessToken
  fun setAccessTok(newToken: String) {
    accessToken = newToken
  }
}
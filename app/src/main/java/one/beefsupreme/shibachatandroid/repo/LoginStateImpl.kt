package one.beefsupreme.shibachatandroid.repo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * We can only know that we have been deemed logged in or logged out by the server at 3 occasions:
 * 1. At Login mutation.
 * 2. When TokenRefreshInterceptor fires because our token is expired.
 * 3. At app mount.
 * 4. Logout...
 * we are the spanish inq...
 *
 * The response from /refresh-token will contain "" as the accessToken
 * Basically, if the accessToken is "" it should also be logged out. I just don't want to usee the
 * AccessToken string as a state, which is why I separated it like this
 */

class LoginStateImpl {
  var isLoggedIn by mutableStateOf(false)
    private set

  var accessToken: String = ""
    private set

  fun login(newAccessToken: String) {
    isLoggedIn = true
    accessToken = newAccessToken
  }

  fun logout() {
    isLoggedIn = false
    accessToken = ""
  }
}
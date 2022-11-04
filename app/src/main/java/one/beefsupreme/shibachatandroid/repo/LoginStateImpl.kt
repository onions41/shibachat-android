package one.beefsupreme.shibachatandroid.repo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * The response from /refresh-token will contain "" as the accessToken
 * Basically, if the accessToken is "" it should also be logged out. I just don't want to usee the
 * AccessToken string as a state, which is why I separated it like this
 */
class LoginStateImpl {
  var isLoggedIn by mutableStateOf(false)
    private set

  var accessToken: String = ""
    private set

  // Used to make sure the initial token fetch is only done
  // after a process kill or finish (), which would reset this to false.
  var doneInitialTokenFetch: Boolean = false

  fun login(newAccessToken: String) {
    isLoggedIn = true
    accessToken = newAccessToken
  }

  fun logout() {
    isLoggedIn = false
    accessToken = ""
  }
}
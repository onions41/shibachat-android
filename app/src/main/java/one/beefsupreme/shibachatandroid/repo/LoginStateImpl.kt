package one.beefsupreme.shibachatandroid.repo

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.auth0.android.jwt.Claim
import javax.inject.Inject
import com.auth0.android.jwt.JWT

/**
 * The response from /refresh-token will contain "" as the accessToken
 * Basically, if the accessToken is "" it should also be logged out. I just don't want to user the
 * AccessToken string as a state, which is why I separated it like this
 */
interface LoginState {
  var isLoggedIn: Boolean
  var accessToken: String
  var meId: Int?
  var finishedInitializingApp: Boolean
  fun login(newAccessToken: String)
  fun logout()
}

class LoginStateImpl @Inject constructor(): LoginState {
  override var isLoggedIn by mutableStateOf(false)

  override var accessToken: String = ""

  override var meId: Int? = null

  // Used to make sure the initial token fetch and meQuery is only done
  // after a process kill, finish(), or login which would reset this to false.
  // Set to true by AppViewModel after finishedInitializingApp
  override var finishedInitializingApp: Boolean = false

  override fun login(newAccessToken: String) {
    isLoggedIn = true
    accessToken = newAccessToken
    val claim: Claim = JWT(newAccessToken).getClaim("userId")
    meId = claim.asInt()
  }

  override fun logout() {
    isLoggedIn = false
    accessToken = ""
    meId = null
  }
}
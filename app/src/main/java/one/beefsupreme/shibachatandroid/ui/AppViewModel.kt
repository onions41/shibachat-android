package one.beefsupreme.shibachatandroid.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.BuildConfig
import one.beefsupreme.shibachatandroid.di.TokenRefreshOkHttp
import one.beefsupreme.shibachatandroid.repo.LoginState
import one.beefsupreme.shibachatandroid.repo.MeFetch
import one.beefsupreme.shibachatandroid.repo.TokenRefreshInterceptorImpl
import java.io.IOException
import javax.inject.Inject

private const val TAG = "**AppViewModel**"

@HiltViewModel
class AppViewModel @Inject constructor(
  private val appDispatchers: AppDispatchers,
  private val loginState: LoginState,
  val meFetch: MeFetch,
  @TokenRefreshOkHttp private val okHttp: OkHttpClient
): ViewModel() {
  val isLoggedIn // This is a observed state
    get() = loginState.isLoggedIn

  var finishedInitializingApp // Not an observed state
    get() = loginState.finishedInitializingApp
    set(value) { loginState.finishedInitializingApp = value }

  var loading by mutableStateOf(false)
    private set

  // Moshi for parsing JSON response
  @JsonClass(generateAdapter = true)
  data class Gist(var ok: Boolean, var accessToken: String)
  private val moshi = Moshi.Builder().build()
  private val gistJsonAdapter = moshi.adapter(TokenRefreshInterceptorImpl.Gist::class.java)

  // Fetches access token and refresh token from /refresh-token
  // and updates the login state accordingly
  fun fetchTokens(): Boolean {
    loading = true // So @App can render a loading indicator

    // Builds the request. POST to /refresh-token
    val request = Request.Builder()
      .url("${BuildConfig.SERVER_URL}/refresh-token")
      // Empty string for the request body
      .post("".toRequestBody("text/x-markdown; charset=utf-8".toMediaType()))
      .build()

    // Network call needs to be outside of main dispatcher.
    // Runs blocking while @App shows a loading indicator
    val response = runBlocking(appDispatchers.io) {
      try {
        okHttp.newCall(request).execute()
      } catch (error: IOException) {
        // Network error
        null
      }
    }

    var tokensWereFetched = false
    if (response != null) {
      // Try to grab the accessToken out of the body. If fail, return ""
      // Even if it successfully grabs the accessToken, it could be "" if
      // refreshToken was invalid
      val newAccessToken = try {
        // square.github.io/okhttp/recipes/#parse-a-json-response-with-moshi-kt-java
        gistJsonAdapter.fromJson(response.body!!.source())!!.accessToken
      } catch (error: NullPointerException) {
        ""
      }
      if (newAccessToken.isEmpty()) {
        loginState.logout()
        Log.v(TAG, "Logged out")
      } else {
        loginState.login(newAccessToken)
        Log.v(TAG, "Logged in with $newAccessToken}")
        tokensWereFetched = true
      }
    }

    // Is now logged in or logged out depending on fetch outcome
    loading = false

    return tokensWereFetched
  }


}






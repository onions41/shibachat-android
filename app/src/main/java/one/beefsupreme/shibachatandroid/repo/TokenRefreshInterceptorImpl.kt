package one.beefsupreme.shibachatandroid.repo

import android.util.Log
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import one.beefsupreme.shibachatandroid.AppDispatchers
import java.util.Date
import javax.inject.Inject
import one.beefsupreme.shibachatandroid.BuildConfig
import one.beefsupreme.shibachatandroid.di.TokenRefreshOkHttpClient
import java.io.IOException

private const val TAG = "**TokenRefreshInterceptor**"

/**
 * Makes a POST request to /refresh-token and grab the new tokens in the response.
 * The access token is stored in the AccessTokenStorage by the code in this interceptor,
 * while the refresh token is taken care of automatically by cookiejar outside of this interceptor.
 */
class TokenRefreshInterceptorImpl @Inject constructor(
  @TokenRefreshOkHttpClient val okHttpClient: OkHttpClient,
  private val appDispatchers: AppDispatchers,
  val loginState: LoginStateImpl
): Interceptor {
  // Mutex for blocking subsequent responses
  private val mutex = Mutex()

  // Moshi for parsing JSON response
  @JsonClass(generateAdapter = true)
  data class Gist(var ok: Boolean, var accessToken: String)
  private val moshi = Moshi.Builder().build()
  private val gistJsonAdapter = moshi.adapter(Gist::class.java)

  override fun intercept(chain: Interceptor.Chain): Response {
    val req = chain.request()

    // This makes all subsequent requests have to wait
    runBlocking(appDispatchers.io) {
      mutex.withLock {
        if (isAccessTokenInvalid()) {
          Log.v(TAG, "TokenRefreshInterceptor Fired")

          // Makes a POST request to /refresh-token
          val request = Request.Builder()
            .url("${BuildConfig.SERVER_URL}/refresh-token")
            // Empty string for the request body
            .post("".toRequestBody("text/x-markdown; charset=utf-8".toMediaType()))
            .build()

          val response: Response? = try {
            okHttpClient.newCall(request).execute()
          } catch (error: IOException) {
            loginState.logout()
            // I should log the Exception instead
            Log.v(TAG, "Your refresh token was invalid. You need to log in again")
            null
          }

          if (response != null) {
            val newAccessToken = try {
              gistJsonAdapter.fromJson(response.body!!.source())!!.accessToken
            } catch (error: NullPointerException) {
              ""
            }
            if (newAccessToken == "") {
              loginState.logout()
            } else {
              loginState.login(newAccessToken)
            }
          }
        }
      }
    }
    // accessToken stored in AccessTokenStorage is either "" or valid
    // The next interceptor (AuthInterceptor) will attache the accessToken to req
    return chain.proceed(req)
  }

  // Returns true of there is an access token in AccessTokenStorage
  // that is invalid due to being expired. Returns false if the access token
  // is an empty string or if the access token is still valid.
  private fun isAccessTokenInvalid(): Boolean {
    val accessToken = loginState.accessToken
    // There is no token
    if (accessToken == "") return false

    try {
      // If an error is thrown here it means token is invalid
      val jwt = JWT(accessToken)
      // Token is expired (I'm pretty sure these are both in ms)
      if (Date() > jwt.expiresAt) {
        return true
      }
    // Access token is invalid
    } catch (error: DecodeException) {
      return true
    }

    // accessToken is valid
    return false
  }
}
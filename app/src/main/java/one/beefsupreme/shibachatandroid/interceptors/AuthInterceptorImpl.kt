package one.beefsupreme.shibachatandroid.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import one.beefsupreme.shibachatandroid.AccessTokenStorage
import javax.inject.Inject

private const val TAG = "**AuthInterceptor**"

/**
 * Attaches the access token from AccessTokenStorage
 * to header.access-token of every request.
 */
class AuthInterceptorImpl @Inject constructor(): Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val accessToken = AccessTokenStorage.getAccessTok()
    val response = chain.proceed(newRequestWithAccessToken(accessToken, request))

    // Logs response cookies. Trying to look for refresh-token
    // being sent back from login mutation
    Log.v(TAG, "Response cookies ${response.headers.values("Set-Cookie")}")

    return response
  }

  // Attaches access token string to header.access-token of the request
  private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
    request.newBuilder()
      .header("access-token", accessToken)
      .build()
}
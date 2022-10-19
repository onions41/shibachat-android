package one.beefsupreme.shibachatandroid

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

// Interceptor class
class AuthInterceptor: Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val accessToken = AccessTokenStorage.getAccessTok()

    val response = chain.proceed(newRequestWithAccessToken(accessToken, request))

//    if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
//      val newAccessToken = AccessTokenStorage.getAccessTok()
//      if (newAccessToken != accessToken) {
//        return chain.proceed(newRequestWithAccessToken(accessToken, request))
//      } else {
//        accessToken = refreshToken()
//        if (accessToken.isNullOrBlank()) {
//          sessionManager.logout()
//          return response
//        }
//        return chain.proceed(newRequestWithAccessToken(accessToken, request))
//      }
//    }

    return response
  }

  private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
    request.newBuilder()
      .header("access-token", accessToken)
      .build()

//  private fun refreshToken(): String? {
//    synchronized(this) {
//      val refreshToken = sessionManager.getRefreshToken()
//      refreshToken?.let {
//        return sessionManager.refreshToken(refreshToken)
//      } ?: return null
//    }
//  }
}

val okHttpClient = OkHttpClient.Builder()
  .addNetworkInterceptor(AuthInterceptor())
  .build()

val apolloClient = ApolloClient.Builder()
  .serverUrl("${BuildConfig.SERVER_URL}/graphql")
  .okHttpClient(okHttpClient)
  .build()

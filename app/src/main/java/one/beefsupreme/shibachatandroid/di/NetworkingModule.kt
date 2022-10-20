package one.beefsupreme.shibachatandroid.di

import android.app.Application
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import one.beefsupreme.shibachatandroid.interceptors.AuthInterceptor
import one.beefsupreme.shibachatandroid.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

  @Provides
  @Singleton
  fun provideCookieJar(app: Application): ClearableCookieJar {
    return PersistentCookieJar(
      SetCookieCache(),
      SharedPrefsCookiePersistor(app.applicationContext)
    )
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    cookieJar: ClearableCookieJar,
    authInterceptor: AuthInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .addNetworkInterceptor(authInterceptor)
      .cookieJar(cookieJar)
      .build()
  }

  @Provides
  @Singleton
  fun provideApolloClient(
    okHttpClient: OkHttpClient
  ): ApolloClient {
    return ApolloClient.Builder()
      .serverUrl("${BuildConfig.SERVER_URL}/graphql")
      .okHttpClient(okHttpClient)
      .build()
  }
}
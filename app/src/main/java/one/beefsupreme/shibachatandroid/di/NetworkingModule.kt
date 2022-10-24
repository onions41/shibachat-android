package one.beefsupreme.shibachatandroid.di

import android.app.Application
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import one.beefsupreme.shibachatandroid.BuildConfig
import one.beefsupreme.shibachatandroid.repo.AuthInterceptorImpl
import one.beefsupreme.shibachatandroid.repo.LoginStateImpl
import one.beefsupreme.shibachatandroid.repo.TokenRefreshInterceptorImpl
import javax.inject.Qualifier
import javax.inject.Singleton

// Interceptor implementations
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenRefreshInterceptor

// OkHTTPImplementations
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApolloOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenRefreshOkHttpClient

// Networking Module
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkingModule {

  @AuthInterceptor
  @Binds
  @Singleton
  abstract fun bindAuthInterceptor(
    authInterceptorImpl: AuthInterceptorImpl
  ): Interceptor

  @TokenRefreshInterceptor
  @Binds
  @Singleton
  abstract fun bindTokenRefreshInterceptor(
    tokenRefreshInterceptorImpl: TokenRefreshInterceptorImpl
  ): Interceptor

  internal companion object{
    @Provides
    @Singleton
    fun provideLoginState(): LoginStateImpl {
      return LoginStateImpl()
    }

    @Provides
    @Singleton
    fun provideCookieJar(app: Application): ClearableCookieJar {
      return PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(app.applicationContext)
      )
    }

    @TokenRefreshOkHttpClient
    @Provides
    @Singleton
    fun provideTokenRefreshOkHttpClient(
      cookieJar: ClearableCookieJar,
    ): OkHttpClient {
      return OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build()
    }

    @ApolloOkHttpClient
    @Provides
    @Singleton
    fun provideApolloOkHttpClient(
      cookieJar: ClearableCookieJar,
      @TokenRefreshInterceptor tokenRefreshInterceptor: Interceptor,
      @AuthInterceptor authInterceptor: Interceptor
    ): OkHttpClient {
      return OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addNetworkInterceptor(tokenRefreshInterceptor)
        .addNetworkInterceptor(authInterceptor)
        .build()
    }

    @Provides
    @Singleton
    fun provideApolloClient(
      @ApolloOkHttpClient okHttpClient: OkHttpClient
    ): ApolloClient {
      return ApolloClient.Builder()
        .serverUrl("${BuildConfig.SERVER_URL}/graphql")
        .okHttpClient(okHttpClient)
        .build()
    }
  }
}
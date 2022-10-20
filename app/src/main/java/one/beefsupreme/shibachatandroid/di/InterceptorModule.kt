package one.beefsupreme.shibachatandroid.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import one.beefsupreme.shibachatandroid.interceptors.AuthInterceptor
import one.beefsupreme.shibachatandroid.interceptors.AuthInterceptorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterceptorModule {

  @Binds
  @Singleton
  abstract fun bindAuthInterceptor(
    authInterceptorImpl: AuthInterceptorImpl
  ): AuthInterceptor
}
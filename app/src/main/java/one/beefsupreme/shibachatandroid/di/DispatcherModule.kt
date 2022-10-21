package one.beefsupreme.shibachatandroid.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.AppDispatchersImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {
  @Binds
  @Singleton
  abstract fun bindDispatcher(
    appDispatchersImpl: AppDispatchersImpl
  ): AppDispatchers
}
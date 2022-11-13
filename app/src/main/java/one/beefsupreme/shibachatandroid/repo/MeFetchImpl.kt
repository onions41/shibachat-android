package one.beefsupreme.shibachatandroid.repo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.watch
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.MeQuery
import javax.inject.Inject

sealed class MeQueryState {
  object Loading: MeQueryState()
  object Error: MeQueryState()
  class Success(val me: MeQuery.User): MeQueryState()
}

interface MeFetch {
  var state: MeQueryState
  fun start()
  fun stop()
}

class MeFetchImpl @Inject constructor(
  appDispatchers: AppDispatchers,
  // Has to be lazy to solve the circular dependency.
  private val apolloClient: Lazy<ApolloClient>
): MeFetch {
  private val scope = CoroutineScope(appDispatchers.default)
  private lateinit var job: Job
  override var state: MeQueryState by mutableStateOf(MeQueryState.Loading)

  override fun start() {
    job = scope.launch {
      apolloClient.get().query(MeQuery()).watch()
        .map {
          val me = it
            .data
            ?.user
          if (me == null) {
            // There were some error
            // TODO: do something with response.errors
            MeQueryState.Error
          } else {
            MeQueryState.Success(me)
          }
        }
        .catch { e ->
          ensureActive()
          emit(MeQueryState.Error)
        }
        .collect {
          // Either MeQueryState.Error or MeQueryState,Success(me: User)
          ensureActive()
          state = it
        }
    }
  }

  override fun stop() {
    job.cancel()
    state = MeQueryState.Loading
  }
}

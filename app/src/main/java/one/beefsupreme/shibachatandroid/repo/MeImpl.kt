package one.beefsupreme.shibachatandroid.repo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.watch
import com.apollographql.apollo3.exception.ApolloException
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

sealed class MeResult {
  object Loading: MeResult()
  class Failed(val error: ApolloException): MeResult()
  class Success(val data: MeQuery.Data): MeResult()
}

interface Me {
  val result: MeResult
  fun start()
  fun stop()
}

class MeImpl @Inject constructor(
  appDispatchers: AppDispatchers,
  // Has to be lazy to solve the circular dependency.
  private val apolloClient: Lazy<ApolloClient>
): Me {
  private var _result: MeResult by mutableStateOf(MeResult.Loading)
  override val result
    get() = _result

  private val scope = CoroutineScope(appDispatchers.io)
  private lateinit var job: Job

  override fun start() {
    job = scope.launch {
      apolloClient.get().query(MeQuery())
        .fetchPolicy(FetchPolicy.CacheAndNetwork)
        .watch()
        .map {
          val data = it.data
          if (data?.user == null) {
            // There were some error
            // TODO: do something with response.errors
            MeResult.Failed(ApolloException("me fetched was null!"))
          } else {
            MeResult.Success(data)
          }
        }
        .catch { e -> // I don't think this e is always an ApolloException.
          ensureActive()
          emit(MeResult.Failed(e as ApolloException)) // emit is used for collecting from the catch block
        }
        .collect {
          ensureActive()
          _result = it
        }
    }
  }

  override fun stop() {
    job.cancel()
    _result = MeResult.Loading
  }
}

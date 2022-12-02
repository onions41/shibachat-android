package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AcceptFRequestMutation
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.repo.Me
import javax.inject.Inject

sealed class FriendsUiEvent {
  object RefreshBtnClick: FriendsUiEvent()
  class AcceptFReqBtnClick(val friendId: Int): FriendsUiEvent()
}

sealed class AcceptFRequestResult {
  object Ready : AcceptFRequestResult()
  object Loading : AcceptFRequestResult()
}

@HiltViewModel
class FriendsViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val appDispatchers: AppDispatchers,
  val me: Me
): ViewModel() {
  /** States */
  var acceptFRequestResult: AcceptFRequestResult
    by mutableStateOf(AcceptFRequestResult.Ready)
      private set

  /** Event handler */
  fun handle(event: FriendsUiEvent) {
    when (event) {
      is FriendsUiEvent.RefreshBtnClick -> {
        me.stop()
        me.start()
      }
      is FriendsUiEvent.AcceptFReqBtnClick -> acceptFRequest(event.friendId)
    }
  }

  /** Private functions called by the event handler */
  private fun acceptFRequest(friendId: Int) {
    viewModelScope.launch(appDispatchers.io) {
      acceptFRequestResult = AcceptFRequestResult.Loading

      apolloClient
        .mutation(AcceptFRequestMutation(friendId))
        .execute()
        .dataAssertNoErrors

      acceptFRequestResult = AcceptFRequestResult.Ready
    }
  }
}
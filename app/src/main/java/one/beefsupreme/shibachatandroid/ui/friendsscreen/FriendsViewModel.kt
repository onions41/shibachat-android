package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.lifecycle.ViewModel
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import one.beefsupreme.shibachatandroid.AppDispatchers
import one.beefsupreme.shibachatandroid.repo.Me
import javax.inject.Inject

sealed class FriendsUiEvent {}

@HiltViewModel
class FriendsViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val appDispatchers: AppDispatchers,
  val me: Me
): ViewModel() {
  fun handle(event: FriendsUiEvent) {
//    when (event) {
//      is FriendsUiEvent.RefreshBtnClick -> refresh()
//      is FriendsUiEvent.SendFReqBtnClick -> sendFRequest(event.friendId)
//    }
  }
}
package one.beefsupreme.shibachatandroid.ui.friendsscreen

import android.util.Log

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import one.beefsupreme.shibachatandroid.repo.MeResult
import one.beefsupreme.shibachatandroid.ui.FriendsNavGraph

private const val TAG = "**FRequestsScreen**"

@FriendsNavGraph
@Destination
@Composable
fun FRequestsScreen(
  vm: FRequestsViewModel = hiltViewModel()
) {
  val allUsersResult by vm.allUsersResult.collectAsState()
  val meResult = vm.me.result

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    LazyColumn(
      modifier = Modifier.padding(vertical = 4.dp)
    ) {
      item(key = "heading-sendFRequests") {
        Text("You should ask these shibas to be your friend.")
      }
      // UserCards. Displays users with a button to send them a fRequest.
      if (allUsersResult is AllUsersResult.Success) {
        items(
          items = (allUsersResult as AllUsersResult.Success).data.users,
          key = { user -> "user-${user.id}" }
        ) { user ->
          // Display the card only if the user is not me
          if (user.id != vm.loginState.meId) {
            UserCard(vm, user)
            Log.v(TAG, user.toString())
          }
        }
      } else {
        item(key = "allUsers-loading-indicator") {
          Text("AllUsers query is either still loading or it failed")
        }
      }

      item(key = "heading-sentFRequests") {
        Text("These are the friend requests you sent. Waiting for response.")
      }

      // SentFReqCard
      if (meResult is MeResult.Success) {
        items(
          items = meResult.data.user.sentFRequests,
          key = { sentFRequest -> "sentFRequest-${sentFRequest.friendId}" }
        ) {sentFRequest ->
          SentFReqCard(vm, sentFRequest)
        }
      } else {
        item(key = "me-loading-indicator") {
          Text("Me query is either still loading or it failed.")
        }
      }
    }
  }
}

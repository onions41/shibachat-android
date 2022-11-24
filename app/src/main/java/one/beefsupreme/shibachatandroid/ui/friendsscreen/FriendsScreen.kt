package one.beefsupreme.shibachatandroid.ui.friendsscreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import one.beefsupreme.shibachatandroid.ui.FriendsNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import one.beefsupreme.shibachatandroid.repo.MeResult
import one.beefsupreme.shibachatandroid.ui.destinations.FRequestsScreenDestination

private const val TAG = "**FriendsScreen**"

@FriendsNavGraph(start = true)
@Destination
@Composable
fun FriendsScreen(
  vm: FriendsViewModel = hiltViewModel(),
  navigator: DestinationsNavigator
) {
  val meResult = vm.me.result

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    LazyColumn(
      modifier = Modifier.padding(vertical = 4.dp)
    ) {
      item(key = "make-new-friends-card") {
        MakeNewFriendsCard { navigator.navigate(FRequestsScreenDestination) }
      }

      if (meResult is MeResult.Success) {
        items(
          items = meResult.data.user.receivedFRequests,
          key = { receivedFRequest -> "receivedFRequest-${receivedFRequest.meId}" }
        ) { receivedFRequest ->
          ReceivedFReqCard(receivedFRequest)
        }
      } else {
        item(key = "loading-indicator") {
          Text("Me is still loading or it failed")
        }

        if (meResult is MeResult.Failed) {
          Log.e(TAG, "MeResult.Failed.error: ${meResult.error.toString()}")
        }
      }
    }
  }
}

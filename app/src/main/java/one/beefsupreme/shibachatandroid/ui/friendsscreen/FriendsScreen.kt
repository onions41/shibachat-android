package one.beefsupreme.shibachatandroid.ui.friendsscreen

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import one.beefsupreme.shibachatandroid.repo.MeQueryState

private const val TAG = "**FriendsScreen**"

@Destination
@Composable
fun FriendsScreen(
  vm: FriendsViewModel = hiltViewModel()
) {
  val meState = vm.meFetch.state
  Log.v(TAG, meState.toString())

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Column {
      Text("Received friend requests")

      if (meState is MeQueryState.Success) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
          items(items = meState.me.receivedFriendRequests) { user ->
            Text("user nickname: ${user?.nickname} ")
          }
        }
      }
    }
  }
}
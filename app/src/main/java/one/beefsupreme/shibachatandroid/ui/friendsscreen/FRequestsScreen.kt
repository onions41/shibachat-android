package one.beefsupreme.shibachatandroid.ui.friendsscreen

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
import one.beefsupreme.shibachatandroid.ui.FriendsNavGraph

@FriendsNavGraph
@Destination
@Composable
fun FRequestsScreen(
  vm: FriendsViewModel = hiltViewModel()
) {
  val meState = vm.meFetch.state
  val state by vm.state.collectAsState()

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    LazyColumn(
      modifier = Modifier.padding(vertical = 4.dp)
    ) {

      if (state is FReqUiState.Success) {
        items(
          items = (state as FReqUiState.Success).allUsers,
          key = { user -> user.id }
        ) { user ->
          if (true) { UserCard(user) }
        }
      } else {
        item(key = "loading-indicator") {
          Text("AllUsers query is either still loading or it failed")
        }
      }
    }
  }
}

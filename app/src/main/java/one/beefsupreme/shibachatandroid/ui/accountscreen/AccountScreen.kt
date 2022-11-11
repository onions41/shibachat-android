package one.beefsupreme.shibachatandroid.ui.accountscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AccountScreen(
  vm: AccountViewModel = hiltViewModel()
) {
  val state = vm.state

  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Column {
      Text("All Users")

      if (state is AccountUiState.Success){
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
          items(
            items = state.allUsers,
            key = { user -> user!!.id }
          ) { user ->
            Surface(
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(user!!.nickname)
                Button(
                  onClick = { vm.handle(AccountUiEvent.SendFriendReqBtnClk(user.id)) }
                ) {
                  Text("Send friend req")
                }
              }
            }
          }
        }
      }

      Button(
        onClick = { vm.handle(AccountUiEvent.LogoutBtnClk) }
      ) {
        Text("Logout")
      }
    }
  }
}
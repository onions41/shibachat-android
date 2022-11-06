package one.beefsupreme.shibachatandroid.ui.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

private const val TAG = "**HomeScreen**"

@Destination(start = true)
@Composable
fun HomeScreen(
  vm: HomeViewModel = hiltViewModel()
) {
  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
    ) {
      Text("This is the HomeScreen")
      Button(
        onClick = { vm.logout() }
      ) {
        Text("Logout")
      }
    }
  }
}
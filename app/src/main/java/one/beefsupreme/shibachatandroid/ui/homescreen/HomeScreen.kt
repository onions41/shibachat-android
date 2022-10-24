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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import one.beefsupreme.shibachatandroid.ui.destinations.AuthScreenDestination

@Destination(start = true)
@Composable
fun HomeScreen(
  navigator: DestinationsNavigator,
  homeViewModel: HomeViewModel = hiltViewModel()
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
        onClick = { navigator.navigate(AuthScreenDestination) }
      ) {
        Text("Go to AuthScreen")
      }
    }
  }
}
package one.beefsupreme.shibachatandroid.ui.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

private const val TAG = "**HomeScreen**"

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
) {
  Surface(
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
    ) {
      Text("This is the HomeScreen")
     }
  }
}
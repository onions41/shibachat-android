package one.beefsupreme.shibachatandroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import one.beefsupreme.shibachatandroid.ui.imagecomposables.FlowerDogeLogo
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier
) {
  Surface(
    color = MaterialTheme.colors.primary,
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("First Line I am Hello")
      FlowerDogeLogo()
      // The one button on this page
      Button(
        enabled = true,
        onClick = {},
      ) {
        Text("Click me")
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 260, heightDp = 400)
@Composable
fun DefaultPreview() {
  ShibachatAndroidTheme { FirstScreen() }
}
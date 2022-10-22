package one.beefsupreme.shibachatandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.lifecycle.viewmodel.compose.viewModel
import one.beefsupreme.shibachatandroid.ui.MeViewModel
import one.beefsupreme.shibachatandroid.ui.imagecomposables.FlowerDogeLogo
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun FirstScreen(
  modifier: Modifier = Modifier,
  meViewModel: MeViewModel = viewModel()
) {
  Surface(
    color = MaterialTheme.colors.primary,
    modifier = modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
    ) {
      Text("First Line I am Hello")
      FlowerDogeLogo()
      // The first button, makes the BingBongQuery
      Button(
        enabled = false,
        onClick = { meViewModel.makeRequest() },
      ) {
        Text("Click me")
      }
      Text(text = meViewModel.myState)
      Button(
        enabled = true,
        onClick = { meViewModel.protected() }
      ) {
        Text("Protected")
      }
      Button(
        enabled = true,
        onClick = { meViewModel.unprotected() }
      ) {
        Text("Unprotected")
      }
      Button(
        enabled = true,
        onClick = { meViewModel.login() }
      ) {
        Text("Login")
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 260, heightDp = 400)
@Composable
fun FirstScreenPreview() {
  ShibachatAndroidTheme { FirstScreen() }
}
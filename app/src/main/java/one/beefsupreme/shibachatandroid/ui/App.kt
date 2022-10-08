package one.beefsupreme.shibachatandroid.ui

import androidx.compose.runtime.Composable
import one.beefsupreme.shibachatandroid.ui.screens.FirstScreen
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun App() {
  ShibachatAndroidTheme {
    // Todo: NavHost goes here
    FirstScreen()
  }
}

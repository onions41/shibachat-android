package one.beefsupreme.shibachatandroid.ui

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

@Composable
fun App() {
  ShibachatAndroidTheme {
    DestinationsNavHost(navGraph = NavGraphs.root)
  }
}

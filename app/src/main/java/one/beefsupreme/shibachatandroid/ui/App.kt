package one.beefsupreme.shibachatandroid.ui

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import one.beefsupreme.shibachatandroid.ui.authscreen.AuthScreen
import one.beefsupreme.shibachatandroid.ui.homescreen.NavGraphs
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

private const val TAG = "**@App**"

@Composable
fun App(
  vm: AppViewModel = hiltViewModel()
) {
  Log.v(TAG, "Re-composed!")
  Log.v(TAG, "isLoggedIn: ${vm.isLoggedIn}")
  Log.v(TAG, "doneInitialTokenFetch: ${vm.doneInitialTokenFetch}")

  // Fetches new access token and refresh token just once per
  // process kill or finish()
  if (!vm.doneInitialTokenFetch) {
    vm.fetchTokens()
    vm.doneInitialTokenFetch = true
  }

  ShibachatAndroidTheme {
    if (vm.loading) {
      Text("This is the @App loading screen")
    } else if (vm.isLoggedIn) {
      DestinationsNavHost(navGraph = NavGraphs.root)
    } else {
      AuthScreen()
    }
  }
}

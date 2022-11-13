package one.beefsupreme.shibachatandroid.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import one.beefsupreme.shibachatandroid.ui.authscreen.AuthScreen
import one.beefsupreme.shibachatandroid.ui.destinations.Destination
import one.beefsupreme.shibachatandroid.ui.theme.ShibachatAndroidTheme

private const val TAG = "**@App**"

//@Composable
//private fun TopBar() {
//  Text("This is the TopBar")
//}

@Composable
private fun BottomBar(navController: NavHostController) {
  val currentDestination: Destination = navController.appCurrentDestinationAsState().value
    ?: NavGraphs.root.startAppDestination

  BottomNavigation {
    BottomBarDestination.values().forEach { destination ->
      BottomNavigationItem(
        selected = currentDestination == destination.direction,
        onClick = {
          navController.navigate(
            direction = destination.direction,
            navOptionsBuilder = { NavOptions.Builder().setLaunchSingleTop(true) }
          )
        },
        icon = { Icon(destination.icon, contentDescription = stringResource(destination.label))},
        label = { Text(stringResource(destination.label)) },
      )
    }
  }
}

@Composable
private fun AppScaffold() {
  val navController:NavHostController = rememberNavController()

  Scaffold(
//    topBar = { TopBar() },
    bottomBar = { BottomBar(navController) }
  ) {
    DestinationsNavHost(
      navGraph = NavGraphs.root,
      navController = navController,
      modifier = Modifier.padding(it)
    )
  }
}

@Composable
fun App(
  vm: AppViewModel = hiltViewModel(),
) {
  // Fetches new access token and refresh token just once per
  // process kill or finish(), then login or logout depending on fetch outcome.
  if (!vm.finishedInitializingApp) {
    val wasSuccessful = vm.fetchTokens() // Runs blocking and vm.loading is true while blocking
    if (wasSuccessful) { vm.meFetch.start() }
    vm.finishedInitializingApp = true // So it doesn't run after UI re-config
  }

  ShibachatAndroidTheme {
    if (vm.loading) {
      Surface {
        Text("This is the @App loading screen")
      } // TODO: Put in actual loading indicator
    } else if (vm.isLoggedIn) {
      AppScaffold()
    } else {
      AuthScreen()
    }
  }
}

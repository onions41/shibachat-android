package one.beefsupreme.shibachatandroid.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.Direction
import one.beefsupreme.shibachatandroid.R
import one.beefsupreme.shibachatandroid.ui.destinations.AccountScreenDestination
import one.beefsupreme.shibachatandroid.ui.destinations.HomeScreenDestination

enum class BottomBarDestination(
  val direction: Direction,
  val icon: ImageVector,
  @StringRes val label: Int
) {
  Home(HomeScreenDestination, Icons.Default.Home, R.string.home_screen),
  Friends(NavGraphs.friends, Icons.Default.Email, R.string.friends_screen),
  Account(AccountScreenDestination, Icons.Default.Home, R.string.account_screen),
}
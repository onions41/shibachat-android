package one.beefsupreme.shibachatandroid.ui.authscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import one.beefsupreme.shibachatandroid.ui.destinations.HomeScreenDestination
import one.beefsupreme.shibachatandroid.ui.imagecomposables.FlowerDogeLogo

@Destination
@Composable
fun AuthScreen(
  navigator: DestinationsNavigator,
  vm: AuthViewModel = hiltViewModel()
) {
  // Let's get the things I need out of the vm here.
  val state = vm.state

  // Primary orange background
  Surface(
    color = if (!vm.isLoggedIn) { Color.Blue } else { Color.Green },
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Spacer(modifier = Modifier.size(30.dp).background(Color.Blue))

      // Should style this text at some point
      Text("Login")

      // Silly Doge with a cherry blossom on its face
      FlowerDogeLogo()

      // Login nickname input
      TextField(
        value = state.loginNickname,
        onValueChange = {
          vm.handle(AuthUiEvent.LoginNicknameChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(text = "Nickname")
        }
      )

      // Login password input
      TextField(
        value = state.loginPassword,
        onValueChange = {
          vm.handle(AuthUiEvent.LoginPasswordChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(text = "Password")
        }
      )

      // Login button
      Button(
        // Should be enabled only when there is some text in both fields
        enabled = true,
        colors = buttonColors(
          MaterialTheme.colors.surface,
          MaterialTheme.colors.primaryVariant,
          MaterialTheme.colors.primary,
          MaterialTheme.colors.primaryVariant
        ),
        onClick = { vm.handle(AuthUiEvent.LoginButtonPress) },
      ) {
        Text("Login")
      }

      Button(
        enabled = true,
        onClick = { vm.handle(AuthUiEvent.ProtectedButtonPress) }
      ) {
        Text("Protected")
      }

      Button(
        enabled = true,
        onClick = { vm.handle(AuthUiEvent.UnprotectedButtonPress) }
      ) {
        Text("Unprotected")
      }

      // Get rid of this button later
      Button(
        onClick = { navigator.navigate(HomeScreenDestination) }
      ) {
        Text("Go to HomeScreen")
      }

      // Text link to register page
      TextButton(
        onClick = {},
        colors = buttonColors(
          MaterialTheme.colors.surface,
          MaterialTheme.colors.primaryVariant,
          MaterialTheme.colors.primary,
          MaterialTheme.colors.primaryVariant
        ),
      ) {
        Text("Register")
      }
    }
  }
}

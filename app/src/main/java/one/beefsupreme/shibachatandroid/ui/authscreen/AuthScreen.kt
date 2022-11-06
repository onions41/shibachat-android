package one.beefsupreme.shibachatandroid.ui.authscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import androidx.hilt.navigation.compose.hiltViewModel
import one.beefsupreme.shibachatandroid.ui.imagecomposables.FlowerDogeLogo

@Composable
fun AuthScreen(
  vm: AuthViewModel = hiltViewModel()
) {
  val state = vm.state

  // Primary orange background
  Surface(
    color = MaterialTheme.colors.primary,
    modifier = Modifier.fillMaxSize()
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
    ) {
      // Should style this text at some point
      when (state.form) {
        Form.LOGIN -> Text("Login")
        Form.REGISTER -> Text("Register")
      }

       // Silly Doge with a cherry blossom on its face
      FlowerDogeLogo()

      // TODD: Need to clear the form
      // Login nickname input
      TextField(
        value = state.nicknameInput,
        onValueChange = {
          vm.handle(AuthUiEvent.NicknameInputChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(text = "Nickname")
        }
      )

      // Login password input
      TextField(
        value = state.passwordInput,
        onValueChange = {
          vm.handle(AuthUiEvent.PasswordInputChange(it))
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(text = "Password")
        }
      )

      // Submit button
      Button(
        // Should be enabled only when there is some text in both fields
        enabled = true,
        colors = buttonColors(
          MaterialTheme.colors.surface,
          MaterialTheme.colors.primaryVariant,
          MaterialTheme.colors.primary,
          MaterialTheme.colors.primaryVariant
        ),
        onClick = { vm.handle(AuthUiEvent.SubmitButtonPress) },
      ) {
        when (state.form) {
          Form.LOGIN -> Text("Login!")
          Form.REGISTER -> Text("Register!")
        }
      }

      // Text link to register page
      TextButton(
        onClick = { vm.handle(AuthUiEvent.SwitchFormButtonPress) },
        colors = buttonColors(
          MaterialTheme.colors.surface,
          MaterialTheme.colors.primaryVariant,
          MaterialTheme.colors.primary,
          MaterialTheme.colors.primaryVariant
        ),
      ) {
        when (state.form) {
          Form.LOGIN -> Text("Don't have an account? Register!")
          Form.REGISTER -> Text("Already have an account? Login!")
        }
      }

      // Crud display of errors
      if (state.errors != null) {
        Text(state.errors.toString())
      }
    }
  }

  // loading dialog
  if (state.loading) {
    Dialog(
      onDismissRequest = {/* Empty to disable closing*/},
      content = {
        Text(text = "Loading")
      },
      properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
      )
    )
  }
}

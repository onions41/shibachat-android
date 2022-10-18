package one.beefsupreme.shibachatandroid.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.BingBongQuery
import one.beefsupreme.shibachatandroid.LoginMutation
import one.beefsupreme.shibachatandroid.ProtectedQuery
import one.beefsupreme.shibachatandroid.UnprotectedQuery
import one.beefsupreme.shibachatandroid.apolloClient

private const val TAG = "**MeViewModel**"

class MeViewModel: ViewModel() {
  // Just using the basic state for now. Read only
  var myState by mutableStateOf("Nothing here yet")
    private set

  fun makeRequest() {
    // I'll probably end up using flow instead of this coroutine
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.query(BingBongQuery()).execute()
      val name = response.data?.example1?.name ?: "response.data was null"
      Log.v(TAG, name)
      myState = "You clicked the button"
    }
  }

  fun protected() {
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.query(ProtectedQuery()).execute()
      val data = response.data?.protected ?: "response.data.unprotected was null"
      Log.v(TAG, (if (data == true) "true" else "false or null, something wrong"))
      myState = "You clicked the button"
    }
  }

  fun unprotected() {
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.query(UnprotectedQuery()).execute()
      val data = response.data?.unprotected ?: "response.data.unprotected was null"
      Log.v(TAG, (if (data == true) "true" else "false or null, something wrong"))
      myState = "You clicked the button"
    }
  }

  fun login() {
    viewModelScope.launch {
      // Need to catch errors here.
      val response = apolloClient.mutation(LoginMutation("Homer", "123456")).execute()
      val data = response.data?.login
      Log.v(TAG, data?.accessToken ?: "accessToken was null" )
      myState = "You clicked the button"
    }
  }
}

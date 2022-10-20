package one.beefsupreme.shibachatandroid.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.AccessTokenStorage
import one.beefsupreme.shibachatandroid.BingBongQuery
import one.beefsupreme.shibachatandroid.LoginMutation
import one.beefsupreme.shibachatandroid.ProtectedQuery
import one.beefsupreme.shibachatandroid.UnprotectedQuery
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "**MeViewModel**"

@HiltViewModel
class MeViewModel @Inject constructor(
  private val apolloClient: ApolloClient
): ViewModel() {
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

      val newAccessToken: String? = response.data?.login?.accessToken

      if (newAccessToken != null) {
        Log.v(TAG, "Got a new access token from the login mutation!")
        // Stores the access token fetched from the Login mutation
        AccessTokenStorage.setAccessTok(newAccessToken)
      } else {
        Log.v(TAG, "No newAccessToken was returned by the login mutation")
      }

      myState = "You clicked the button"
    }
  }
}
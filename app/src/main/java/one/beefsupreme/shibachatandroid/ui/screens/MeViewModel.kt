package one.beefsupreme.shibachatandroid.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.BingBongQuery
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
}
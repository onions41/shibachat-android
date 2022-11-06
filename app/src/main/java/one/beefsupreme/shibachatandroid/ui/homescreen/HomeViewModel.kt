package one.beefsupreme.shibachatandroid.ui.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.ApolloClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.beefsupreme.shibachatandroid.LogoutMutation
import one.beefsupreme.shibachatandroid.repo.LoginStateImpl
import javax.inject.Inject

private const val TAG = "**HomeViewModel**"

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val apolloClient: ApolloClient,
  private val loginState: LoginStateImpl
): ViewModel() {
  val isLoggedIn // This is a observed state
    get() = loginState.isLoggedIn

  fun logout() {
    viewModelScope.launch {
      apolloClient.mutation(LogoutMutation()).execute()
      loginState.logout()
      Log.v(TAG, "This is the login state: ${loginState.isLoggedIn}")
    }
  }
}
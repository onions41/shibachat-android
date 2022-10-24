package one.beefsupreme.shibachatandroid.ui.homescreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import one.beefsupreme.shibachatandroid.repo.LoginStateImpl
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val loginState: LoginStateImpl
  // Need the interceptor okhttp
): ViewModel() {

}
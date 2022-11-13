package one.beefsupreme.shibachatandroid.ui.friendsscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import one.beefsupreme.shibachatandroid.repo.MeFetch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
  val meFetch: MeFetch
): ViewModel() {}
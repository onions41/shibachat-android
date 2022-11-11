package one.beefsupreme.shibachatandroid.ui.accountscreen

sealed class AccountUiEvent {
  object LogoutBtnClk: AccountUiEvent()
  class SendFriendReqBtnClk(val friendId: Int): AccountUiEvent()
}

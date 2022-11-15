package one.beefsupreme.shibachatandroid.ui.accountscreen

sealed class AccountUiEvent {
  object LogoutBtnClk: AccountUiEvent()
  class SendFRequestBtnClk(val friendId: Int): AccountUiEvent()
}

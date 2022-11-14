package one.beefsupreme.shibachatandroid.ui.accountscreen

import com.apollographql.apollo3.exception.ApolloException
import one.beefsupreme.shibachatandroid.AllUsersQuery

sealed class AccountUiState {
  object Loading : AccountUiState()
  class Error(val e: ApolloException) : AccountUiState()
  class Success(val allUsers: List<AllUsersQuery.User>) : AccountUiState()
}

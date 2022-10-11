package one.beefsupreme.shibachatandroid

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
  .serverUrl("${BuildConfig.SERVER_URL}/graphql")
  .build()
package one.beefsupreme.shibachatandroid

import com.apollographql.apollo3.ApolloClient

val apolloClient = ApolloClient.Builder()
  // Todo: I need to use env var for the production endpoint
  // Todo: I need to use a script to find out hte endpoint for my dev machine
  .serverUrl("http://192.168.0.243:9611/graphql")
  .build()
package com.example.demo.http

object PathTemplate {
    /*
  Allow a user to express their desire to start a new game
  users will enter a waiting lobby, where a matchmaking algorithm will select pairs of users and start games with them.
  POST
  */
    const val START_GAME = "/games"

    const val CHECK_GAME = "/games/check/{id}"

    //Allow a user to play a round.POST
    const val PLAY = "/games/{id}"

    const val GAME_STATE = "/games/state/{id}"

    //No authorization needed
    const val HOME = "/home"

    //No authorization needed
    const val STATISTICS = "/home/stats/{id}"

    //No authorization needed
    const val GAMES_COUNT = "/home/stats/games/{id}"

    //No authorization needed
    const val AUTHORS = "/home/authors"

    //No authorization needed
    const val USER_BY_ID = "/users/{id}"

    //No authorization needed
    const val USER_BY_USERNAME = "/users/username/{username}"

    //No authorization needed
    const val CREATE_USER = "/users"




}
package com.example.demo.http

object PathTemplate {
    /*
  Allow an user to express their desire to start a new game
  users will enter a waiting lobby, where a matchmaking algorithm will select pairs of users and start games with them.
  POST
  */
    const val START_GAME = "/games"

    const val CREATE_GAME = "/games/start"

    const val CHECK_GAME = "/games/check/{id}"

    //Allow an user to observe the game state. GET

    const val GAME_BY_ID = "/games/{id}"

    //Allow an user to play a round.POST

    const val PLAY = "/games/{id}"

    const val GAME_STATE = "/games/state/{id}"

    //unauth
    const val HOME = "/home"

    //unauth
    const val STATICS = "/home/stats/{id}"

    //unauth
    const val GAMES_COUNT = "/home/stats/games/{id}"

    //unauth
    const val AUTHORS = "/home/authors"
    //unauth
    const val USER_BY_ID = "/users/{id}"
    //unauth
    const val USER_BY_USERNAME = "/users/username/{username}"

    //unauth
    const val CREATE_USER = "/users"




}
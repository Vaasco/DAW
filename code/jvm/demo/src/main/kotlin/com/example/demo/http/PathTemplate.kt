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

    //Obtain stats authors and login form.
    const val HOME = "/home"

    //Obtain statistical and ranking information, such as number of played games and users ranking, by an unauthenticated user.
    const val STATICS = "/home/stats/{id}"

    const val GAMES_NUMBER = "/home/stats/games/{id}"

    //Obtain information about the system, such as the system authors and the system version, by an unauthenticated user

    const val AUTHORS = "/home/authors"

    const val USER_BY_ID = "/users/{id}"

    const val USER_BY_USERNAME = "/users/{username}"

    // Register a new user.
    const val CREATE_USER = "/users"




}
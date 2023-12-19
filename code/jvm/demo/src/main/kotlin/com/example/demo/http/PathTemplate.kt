package com.example.demo.http

import org.springframework.web.util.UriTemplate

object PathTemplate {
    /*
    Allow a user to express their desire to start a new game
    users will enter a waiting lobby, where a matchmaking algorithm will select pairs of users and start games with them.
    POST
    */
    const val START_GAME = "/games"

    const val GAME_BY_ID = "/games/{id}"

    const val LAST_GAME = "/games/user/{username}"

    //Allow a user to play a round.POST
    const val PLAY = "/games/{id}"

    //Allow a user to forfeit a game
    const val FORFEIT = "/games/forfeit/{id}"

    //Allow a user to cancel waiting for an opponent
    const val CANCEL = "/games/cancel"

    //No authorization needed
    const val HOME = "/home"

    //No authorization needed
    const val STATISTICS_BY_USERNAME = "/stats/{username}"

    const val STATISTICS = "/stats"

    //No authorization needed
    const val AUTHORS = "/authors"

    //No authorization needed
    const val USER_BY_ID = "/users/{id}"

    //No authorization needed
    const val USER_BY_USERNAME = "/users/username/{username}"

    //No authorization needed
    const val CREATE_USER = "/users"

    const val LOGIN = "/users/login"

    const val LOGOUT = "/users/logout"

    const val COOKIES = "/cookies"
}

fun gameByIdURI(id: Int) = UriTemplate(PathTemplate.GAME_BY_ID).expand(id)

fun playURI(id: Int?) = UriTemplate(PathTemplate.PLAY).expand(id)

fun statisticsByUsernameURI(username: String) = UriTemplate(PathTemplate.STATISTICS_BY_USERNAME).expand(username)

fun userByIdURI(id: Int) = UriTemplate(PathTemplate.USER_BY_ID).expand(id)

fun userByUsernameURI(username: String) = UriTemplate(PathTemplate.USER_BY_USERNAME).expand(username)

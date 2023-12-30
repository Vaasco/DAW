package com.example.demo.http.siren

import com.example.demo.domain.Token
import com.example.demo.http.PathTemplate.AUTHORS
import com.example.demo.http.PathTemplate.CREATE_USER
import com.example.demo.http.PathTemplate.HOME
import com.example.demo.http.PathTemplate.LOGIN
import com.example.demo.http.PathTemplate.START_GAME
import com.example.demo.http.PathTemplate.STATISTICS
import com.example.demo.http.gameByIdURI
import com.example.demo.http.model.AuthorsModel
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.SignUpModel
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserOutputModel
import com.example.demo.http.playURI
import com.example.demo.http.statisticsByUsernameURI
import com.example.demo.http.userByIdURI
import com.example.demo.http.userByUsernameURI
import org.springframework.http.HttpMethod.*
import java.net.URI

class SirenMaker {

    fun sirenGetUserById(body: UserOutputModel): SirenModel<UserOutputModel> {
        return siren(body) {
            clazz("user by id")
            action(HOME, URI(HOME), GET)
            link(userByIdURI(body.id).toString(), LinkRelation(body.id.toString()))
        }
    }

    fun sirenGetUsersByUsername(body: List<UserOutputModel>): SirenModel<List<UserOutputModel>> {
        return siren(body) {
            clazz("user by username")
            action(HOME, URI(HOME), GET)
            body.forEach {
                link(userByUsernameURI(it.username).toString(), LinkRelation(it.username))
            }
        }
    }

    fun sirenSignIn(body: SignUpModel): SirenModel<SignUpModel> {
        return siren(body) {
            clazz("sign in")
            action(HOME, URI(HOME), GET)
            action(LOGIN, URI(LOGIN), POST)
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenLogIn(body: Token): SirenModel<Token> {
        return siren(body) {
            clazz("log in")
            action(HOME, URI(HOME), GET)
            action(CREATE_USER, URI(CREATE_USER), POST)
            link(LOGIN, LinkRelation(LOGIN))
        }
    }

    fun sirenLogOut(body: Boolean): SirenModel<Boolean> {
        return siren(body) {
            clazz("log out")
            action(HOME, URI(HOME), GET)
            action(LOGIN, URI(LOGIN), POST)
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenAuthors(body: List<AuthorsModel>): SirenModel<List<AuthorsModel>> {
        return siren(body) {
            clazz("authors")
            action(HOME, URI(HOME), GET)
            link(AUTHORS, LinkRelation(AUTHORS))
        }
    }

    fun sirenStatistics(body: List<StatisticsModel>): SirenModel<List<StatisticsModel>> {
        return siren(body) {
            clazz("statistics")
            action(HOME, URI(HOME), GET)
            body.forEach {
                val uri = statisticsByUsernameURI(it.username)
                action(uri.toString(), uri, GET)
            }
            link(STATISTICS, LinkRelation(STATISTICS))
        }
    }

    fun sirenStatisticsByUsername(body: List<StatisticsByIdModel>): SirenModel<List<StatisticsByIdModel>> {
        return siren(body) {
            clazz("statistics by id")
            action(HOME, URI(HOME), GET)
            action(STATISTICS, URI(STATISTICS), GET)
            body.forEach {
                link(statisticsByUsernameURI(it.username).toString(), LinkRelation(it.username))
            }
        }
    }

    fun sirenGetGameById(body: GameModel): SirenModel<GameModel> {
        return siren(body) {
            clazz("game by id")
            action(HOME, URI(HOME), GET)
            val playUri = playURI(body.id)
            action(playUri.toString(), playUri, POST)
            val gameUri = gameByIdURI(body.id)
            link(gameUri.toString(), LinkRelation(gameUri.toString()))
        }
    }

    fun sirenGetLastGame(body : GameModel?): SirenModel<GameModel?> {
        return siren(body){
            clazz("last game")
            action(HOME, URI(HOME), GET)
            if(body != null) {
                val playUri = playURI(body.id)
                action(playUri.toString(), playUri, POST)
                val gameUri = gameByIdURI(body.id)
                link(gameUri.toString(), LinkRelation(gameUri.toString()))
            }
        }
    }

    fun sirenCreateLobby(body: GameModel?): SirenModel<GameModel?> {
        return siren(body) {
            clazz("create lobby")
            action(HOME, URI(HOME), GET)
            if (body != null) {
                val playUri = playURI(body.id)
                action(playUri.toString(), playUri, POST)
            }
            link(START_GAME, LinkRelation(START_GAME))
        }
    }

    fun sirenPlay(body: GameModel): SirenModel<GameModel> {
        return siren(body) {
            clazz("play")
            action(HOME, URI(HOME), GET)
            val playUri = playURI(body.id)
            action(playUri.toString(), playUri, POST)
            link(playUri.toString(), LinkRelation(playUri.toString()))
        }
    }

    fun sirenForfeit(body: Boolean): SirenModel<Boolean> {
        return siren(body) {
            clazz("forfeit")
            action(HOME, URI(HOME), POST)
        }
    }

    fun sirenCancel(body:Unit):SirenModel<Unit>{
        return siren(body){
            clazz("cancel")
            action(HOME,URI(HOME), POST)
        }
    }
}
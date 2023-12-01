package com.example.demo.http.siren

import com.example.demo.domain.Message
import com.example.demo.domain.Token
import com.example.demo.http.PathTemplate.CREATE_USER
import com.example.demo.http.PathTemplate.HOME
import com.example.demo.http.PathTemplate.LOGIN
import com.example.demo.http.PathTemplate.PLAY
import com.example.demo.http.PathTemplate.STATISTICS
import com.example.demo.http.PathTemplate.STATISTICS_BY_ID
import com.example.demo.http.model.AuthorsModel
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.LobbyModel
import com.example.demo.http.model.PlayModel
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserOutputModel
import org.springframework.http.HttpMethod
import java.net.URI

class SirenMaker {

    //TODO()fun sirenHome(body: Result<>)

    fun sirenGetUserById(body: UserOutputModel): SirenModel<UserOutputModel> {
        return siren(body) {
            clazz("user by id")
            link(HOME, LinkRelation(HOME))
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenGetUserByUsername(body: UserOutputModel): SirenModel<UserOutputModel> {
        return siren(body) {
            clazz("user by username")
            link(HOME, LinkRelation(HOME))
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenSignIn(body: UserOutputModel): SirenModel<UserOutputModel> {
        return siren(body) {
            clazz("sign in")
            action(HOME, URI(LOGIN), HttpMethod.GET)
            action(LOGIN, URI(LOGIN), HttpMethod.POST)
        }
    }

    fun sirenLogIn(body: Token): SirenModel<Token> {
        return siren(body) {
            clazz("log in")
            link(HOME, LinkRelation(HOME))
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenAuthors(body: List<AuthorsModel>): SirenModel<List<AuthorsModel>> {
        return siren(body) {
            clazz("authors")
            link(HOME, LinkRelation(HOME))
        }
    }

    fun sirenStatistics(body: List<StatisticsModel>): SirenModel<List<StatisticsModel>> {
        return siren(body) {
            clazz("statistics")
            //link(HOME, LinkRelation(HOME))
           // link(STATISTICS_BY_ID, LinkRelation(STATISTICS_BY_ID))
        }
    }

    fun sirenStatisticsById(body: StatisticsByIdModel): SirenModel<StatisticsByIdModel> {
        return siren(body) {
            clazz("statistics by id")
           // link(HOME, LinkRelation(HOME))
           // link(STATISTICS, LinkRelation(STATISTICS))
        }
    }

    fun sirenGetGameById(body: GameModel): SirenModel<GameModel> {
        return siren(body) {
            clazz("game by id")
            link(HOME, LinkRelation(HOME))
            link(PLAY, LinkRelation(PLAY))
        }
    }

    fun sirenCreateLobby(body: Message): SirenModel<Message> {
        return siren(body) {
            //clazz("create lobby")
            //link(HOME, LinkRelation(HOME))
            //link(PLAY, LinkRelation(PLAY))
        }
    }

    fun sirenPlay(body: GameModel): SirenModel<GameModel> {
        return siren(body) {
            //clazz("play")
            //link(HOME, LinkRelation(HOME))
            //link(PLAY, LinkRelation(PLAY))
        }
    }

    fun sirenGameState(body: GameModel): SirenModel<GameModel> {
        return siren(body) {
            //clazz("game state")
            //link(PLAY, LinkRelation(PLAY))
        }
    }
}
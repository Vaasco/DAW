package com.example.demo.http.siren

import com.example.demo.http.PathTemplate.CREATE_USER
import com.example.demo.http.PathTemplate.HOME
import com.example.demo.http.PathTemplate.LOGIN
import com.example.demo.http.PathTemplate.PLAY
import com.example.demo.http.PathTemplate.STATISTICS
import com.example.demo.http.PathTemplate.STATISTICS_BY_ID
import com.example.demo.http.model.AuthorsModel
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserOutputModel

class SirenMaker {

    //TODO()fun sirenHome(body: Result<>)

    fun sirenSignIn(body: Result<UserOutputModel>) {
        siren(body) {
            clazz("sign in")
            link(HOME, LinkRelation(HOME))
            link(LOGIN, LinkRelation(LOGIN))
        }
    }

    fun sirenLogIn(body: Result<UserOutputModel>) {
        siren(body) {
            clazz("log in")
            link(HOME, LinkRelation(HOME))
            link(CREATE_USER, LinkRelation(CREATE_USER))
        }
    }

    fun sirenAuthors(body: Result<List<AuthorsModel>>) {
        siren(body) {
            clazz("authors")
            link(HOME, LinkRelation(HOME))
        }
    }

    fun sirenStatistics(body: Result<StatisticsModel>) {
        siren(body) {
            clazz("statistics")
            link(HOME, LinkRelation(HOME))
            link(STATISTICS_BY_ID, LinkRelation(STATISTICS_BY_ID))
        }
    }

    fun sirenStatisticsById(body: Result<StatisticsByIdModel>) {
        siren(body) {
            clazz("statistics by id")
            link(HOME, LinkRelation(HOME))
            link(STATISTICS, LinkRelation(STATISTICS))
        }
    }

    fun sirenGameState(body: Result<GameModel>) {
        siren(body) {
            clazz("game state")
            link(PLAY, LinkRelation(PLAY))
        }
    }



}
package com.example.demo.repository.jbdi

import com.example.demo.http.model.StatisticsOutputModel
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.UUID

@Component
class JbdiUsersRepository(private val jdbi: Jdbi) : UsersRepository {
    //converter a String em user

    override fun getById(id: Int): String? {
        return jdbi.withHandle<String?, Exception> { handle ->
            handle.createQuery("select username from player where id = :id")
                .bind("id", id)
                .mapTo(String::class.java)
                .singleOrNull()

        }
    }

    override fun createUser(username: String, password: String) {
       val token =  UUID.randomUUID().toString()

        jdbi.useHandle<Exception> { handle ->
            val query = "INSERT INTO player(username, password, token) VALUES (:username, :password, :token)"
            handle.createUpdate(query)
                .bind("userName", username)
                .bind("password", password)
                .bind("token", token)
                .execute()
        }
    }

    override fun getGameState(id: Int):String? {
        return jdbi.withHandle<String?,Exception> { handle ->
            val query = "select state from game where id = :id"
            handle.createQuery(query)
                .bind("id",id)
                .mapTo(String::class.java )
                .singleOrNull()
        }
    }

    override fun getStatisticsById(id: Int):StatisticsOutputModel? {
        return jdbi.withHandle<StatisticsOutputModel,Exception>{ handle ->
            val query = "select rank, played_games, won_games, lost_games from ranking where player_id = :id"
            handle.createQuery(query)
                .bind("id",id)
                .mapTo(StatisticsOutputModel::class.java)
                .singleOrNull()
        }
    }
}
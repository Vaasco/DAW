package com.example.demo.repository.jbdi

import com.example.demo.domain.Authentication
import com.example.demo.http.model.StatisticsOutputModel
import com.example.demo.http.model.UserOutputModel
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception
import java.sql.Date
import java.util.UUID

@Component
class JbdiUsersRepository(private val jdbi: Jdbi) : UsersRepository {
    //converter a String em user

    override fun getUserById(id: Int): UserOutputModel? {
        return jdbi.withHandle<UserOutputModel?, Exception> { handle ->
            handle.createQuery("select id, username, password from player where id = :id")
                .bind("id", id)
                .mapTo(UserOutputModel::class.java)
                .singleOrNull()

        }
    }

    override fun createUser(username: String, password: String) {
        val token = UUID.randomUUID().toString()
        val query = "INSERT INTO player(username, password) VALUES (:username, :password)"
        //TODO("Create token")

        jdbi.useHandle<Exception> { handle ->
            handle.createUpdate(query)
                .bind("userName", username)
                .bind("password", password)
                .bind("token", token)
                .execute()
        }
    }

    override fun getStatisticsById(id: Int): StatisticsOutputModel? {
        return jdbi.withHandle<StatisticsOutputModel, Exception> { handle ->
            val query = "select rank, played_games, won_games, lost_games from ranking where player_id = :id"
            handle.createQuery(query)
                .bind("id", id)
                .mapTo(StatisticsOutputModel::class.java)
                .singleOrNull()
        }
    }

    override fun getGamesCount(id: Int): Int {
        return jdbi.withHandle<Int?, Exception> { handle ->
            val query = "select played_games from ranking where player_id = :id"
            handle.createQuery(query)
                .bind("id", id)
                .mapTo(Int::class.java)
                .single()
        }
    }

    override fun getUserByUsername(username: String): Int? {
        return jdbi.withHandle<Int?, Exception> { handle ->
            val query = "select id from player where username = :username"
            handle.createQuery(query)
                .bind("username", username)
                .mapTo(Int::class.java)
                .singleOrNull()
        }
    }

    override fun getUserPassword(username: String): String {
        return jdbi.withHandle<String?, Exception> {handle ->
            val query = "select password from player where username = :username"
            handle.createQuery(query)
                .bind("username", username)
                .mapTo(String::class.java)
                .single()
        }
    }

    override fun createToken(authentication: Authentication) {
        val query = "insert into authentication (player_id, token,createdAt,lastUsedAt) VALUES (:player_id, :token, :createdAt, :lastUsedAt)"
        return jdbi.useHandle<Exception> { handle ->
            handle.createUpdate( query)
                .bind( "player_id",authentication.userId )
                .bind("token",authentication.token)
                .bind("createdAt",authentication.createdAt)
                .bind("lastUsedAt",authentication.lastUsedAt)
                .execute()
        }
    }

    override fun getCurrDate(): Date {
        val query = "select CURRENT_DATE;"
        return jdbi.withHandle<Date, Exception> {handle  ->
            handle.createQuery(query)
                .mapTo(Date::class.java)
                .single()
        }
    }

    override fun getToken(token: String): String? {
        val query = "select token from authentication where token = :token"
        return jdbi.withHandle<String?, Exception> {handle  ->
            handle.createQuery(query)
                .bind("token", token)
                .mapTo(String::class.java)
                .singleOrNull()
        }
    }

}
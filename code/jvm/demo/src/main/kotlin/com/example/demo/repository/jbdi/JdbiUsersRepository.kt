package com.example.demo.repository.jbdi

import com.example.demo.domain.Authentication
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserTemp
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Handle
import java.sql.Date

class JdbiUsersRepository(/*private val jdbi: Jdbi*/private val handle: Handle) : UsersRepository {
    override fun getUserById(id: Int): UserModel? {
        return handle.createQuery("select id, username, password from player where id = :id")
            .bind("id", id)
            .mapTo(UserModel::class.java)
            .singleOrNull()

    }

    override fun createUser(username: String, password: String): Int {
        val query = "insert into player(username, password) values (:username, :password)"
        val query2 = "select id from player where username = :username"

        handle.createUpdate(query)
            .bind("username", username)
            .bind("password", password)
            .execute()
        return handle.createQuery(query2)
            .bind("username", username)
            .mapTo(Int::class.java)
            .single()

    }

    override fun getStatisticsById(id: Int): StatisticsModel {
        val query = "select rank, played_games, won_games, lost_games from ranking where player_id = :id"

        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(StatisticsModel::class.java)
            .single()
    }

    override fun getGamesCount(id: Int): Int {
        val query = "select played_games from ranking where player_id = :id"

        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(Int::class.java)
            .single()

    }

    override fun getUserByUsername(username: String): UserModel? {
        val query = "select id, username, password from player where username = :username"

        return handle.createQuery(query)
            .bind("username", username)
            .mapTo(UserModel::class.java)
            .singleOrNull()

    }

    override fun getUserPassword(username: String): String {
        val query = "select password from player where username = :username"

        return handle.createQuery(query)
            .bind("username", username)
            .mapTo(String::class.java)
            .single()

    }

    override fun createAuthentication(token: Authentication) {
        val query = "insert into authentication (player_id, token,createdAt,lastUsedAt) values" +
                "(:player_id, :token, :createdAt, :lastUsedAt)"
        handle.createUpdate(query)
            .bind("player_id", token.userId)
            .bind("token", token.token)
            .bind("createdAt", token.createdAt)
            .bind("lastUsedAt", token.lastUsedAt)
            .execute()
    }

    override fun getCurrDate(): Date {
        val query = "select CURRENT_DATE;"

        return handle.createQuery(query)
            .mapTo(Date::class.java)
            .single()

    }

    override fun getUserByToken(token: String): UserTemp? {
        val query = "select * from authentication where token = :token"

        return handle.createQuery(query)
            .bind("token", token)
            .mapTo(UserTemp::class.java)
            .singleOrNull()

    }
}
package com.example.demo.repository.jbdi

import com.example.demo.domain.Authentication
import com.example.demo.domain.Token
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Handle
import java.sql.Date

class JdbiUsersRepository(private val handle: Handle) : UsersRepository {

    override fun getUserById(id: Int): UserOutputModel? {
        val query = "select id, username from player where id = :id"
        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(UserOutputModel::class.java)
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

    override fun getStatisticsByUsername(username: String): List<StatisticsByIdModel> {
        val query = "select username, rank, played_games, won_games, lost_games from ranking " +
                "join player on (player_id = id) where username ilike :username"
        return handle.createQuery(query)
            .bind("username", "$username%")
            .mapTo(StatisticsByIdModel::class.java)
            .toList()
    }

    override fun getStatistics(): List<StatisticsModel> {
        val query = "select username, rank, played_games, won_games, lost_games from ranking " +
                "join player on (player_id = id) order by rank desc limit 50"
        return handle.createQuery(query)
            .mapTo(StatisticsModel::class.java)
            .list()
    }

    override fun getGamesCount(id: Int): Int {
        val query = "select played_games from ranking where player_id = :id"
        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(Int::class.java)
            .single()
    }

    override fun getUserByUsername(username: String): UserOutputModel? {
        val query = "select id, username from player where username = :username"
        return handle.createQuery(query)
            .bind("username", username)
            .mapTo(UserOutputModel::class.java)
            .singleOrNull()
    }

    override fun getUsersByUsername(username: String): List<UserOutputModel>? {
        val query = "select id, username from player where username ilike :username"
        val list = handle.createQuery(query)
            .bind("username", "$username%")
            .mapTo(UserOutputModel::class.java)
            .toList()
        return list.ifEmpty { null }
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

    override fun deleteAuthentication(username: String?): Boolean {
        val query = "delete from authentication using player where " +
                "authentication.player_id = player.id and player.username = :username;"
        handle.createUpdate(query)
            .bind("username", username)
            .execute()
        return true
    }

    override fun getCurrDate(): Date {
        val query = "select CURRENT_DATE;"
        return handle.createQuery(query)
            .mapTo(Date::class.java)
            .single()
    }

    override fun getUserByToken(token: String): UserModel? {
        val query = "select * from player p join authentication au on (p.id = au.player_id) where token = :token"
        return handle.createQuery(query)
            .bind("token", token)
            .mapTo(UserModel::class.java)
            .singleOrNull()
    }

    override fun getUserToken(username: String): Token {
        val query = "select token from authentication join player on (id = player_id) where username = :username"
        return handle.createQuery(query)
            .bind("username", username)
            .mapTo(Token::class.java)
            .single()
    }

    override fun getUserTokens(): List<Token> {
        val query = "select token from authentication"
        return handle.createQuery(query)
            .mapTo(Token::class.java)
            .toList()
    }
}
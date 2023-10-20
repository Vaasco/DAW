package com.example.demo.repository.jbdi

import com.example.demo.domain.Authentication
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception
import java.sql.Date
import java.time.Instant

@Component
class JdbiUsersRepository(private val jdbi: Jdbi) : UsersRepository {
    override fun getUserById(id: Int): UserModel? {
        return jdbi.withHandle<UserModel?, Exception> { handle ->
            handle.createQuery("select id, username, password from player where id = :id")
                .bind("id", id)
                .mapTo(UserModel::class.java)
                .singleOrNull()
        }
    }

    override fun createUser(username: String, password: String): Int {
        val query = "insert into player(username, password) values (:username, :password)"
        val query2 = "select id from player where username = :username"
        return jdbi.withHandle<Int, Exception> { handle ->
            handle.createUpdate(query)
                .bind("username", username)
                .bind("password", password)
                .execute()
            handle.createQuery(query2)
                .bind("username", username)
                .mapTo(Int::class.java)
                .single()
        }
    }

    override fun getStatisticsById(id: Int): StatisticsModel {
        val query = "select rank, played_games, won_games, lost_games from ranking where player_id = :id"
        return jdbi.withHandle<StatisticsModel, Exception> { handle ->
            handle.createQuery(query)
                .bind("id", id)
                .mapTo(StatisticsModel::class.java)
                .single()
        }
    }

    override fun getGamesCount(id: Int): Int {
        val query = "select played_games from ranking where player_id = :id"
        return jdbi.withHandle<Int?, Exception> { handle ->
            handle.createQuery(query)
                .bind("id", id)
                .mapTo(Int::class.java)
                .single()
        }
    }

    override fun getUserByUsername(username: String): UserModel? {
        val query = "select id, username, password from player where username = :username"
        return jdbi.withHandle<UserModel?, Exception> { handle ->
            handle.createQuery(query)
                .bind("username", username)
                .mapTo(UserModel::class.java)
                .singleOrNull()
        }
    }

    override fun getUserPassword(username: String): String {
        val query = "select password from player where username = :username"
        return jdbi.withHandle<String?, Exception> {handle ->
            handle.createQuery(query)
                .bind("username", username)
                .mapTo(String::class.java)
                .single()
        }
    }

    override fun createAuthentication(token: Authentication) {
        val query = "insert into authentication (player_id, token,createdAt,lastUsedAt) values" +
                        "(:player_id, :token, :createdAt, :lastUsedAt)"
        return jdbi.useHandle<Exception> { handle ->
            handle.createUpdate(query)
                .bind( "player_id", token.userId )
                .bind("token", token.token)
                .bind("createdAt", token.createdAt)
                .bind("lastUsedAt", token.lastUsedAt)
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
        val query = "select player_id from authentication where token = :token"
        return jdbi.withHandle<String?, Exception> {handle  ->
            handle.createQuery(query)
                .bind("token", token)
                .mapTo(String::class.java)
                .singleOrNull()
        }
    }
}
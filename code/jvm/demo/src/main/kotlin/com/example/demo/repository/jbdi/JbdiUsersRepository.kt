package com.example.demo.repository.jbdi

import com.example.demo.domain.TempUser
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class JbdiUsersRepository(private val jdbi: Jdbi) : UsersRepository {
    override fun getById(id: Int): String? {
        return jdbi.withHandle<String?, Exception> { handle ->
            handle.createQuery("select username from player where id = :id")
                .bind("id", id)
                .mapTo(String::class.java)
                .singleOrNull()
        }
    }

    override fun createUser(user: TempUser) {
        val userName = user.name

        jdbi.useHandle<Exception> { handle ->
            val query = "INSERT INTO player(username) VALUES (:userName)"
            handle.createUpdate(query)
                .bind("userName", userName)
                .execute()
        }
    }

    override fun checkGameState(id: Int):String? {
        return jdbi.withHandle<String?,Exception> { handle ->
            val query = "select state from game where id = :id"
            handle.createQuery(query)
                .bind("id",id)
                .mapTo(String::class.java )
                .singleOrNull()
        }
    }
}
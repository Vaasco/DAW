package com.example.demo.repository.jbdi

import com.example.demo.domain.User
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class JbdiUsersRepository(private val jdbi: Jdbi) : UsersRepository {
    override fun getById(uId: Int): String? {
        return jdbi.withHandle<String?, Exception> { handle ->
            handle.createQuery("select username from player where id = :uId")
                .bind("uId", uId)
                .mapTo(String::class.java)
                .singleOrNull()

        }
    }

    override fun createUser(username: String) {
         jdbi.withHandle<User?,Exception> { handle ->
             val query = "insert into player(username) values($username)"
            handle.createQuery(query)
                .mapTo(User::class.java)
                .singleOrNull()
        }
    }

}
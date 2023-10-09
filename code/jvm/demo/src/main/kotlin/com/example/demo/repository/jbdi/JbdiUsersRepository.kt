package com.example.demo.repository.jbdi

import com.example.demo.domain.User
import com.example.demo.repository.UsersRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.Exception

@Component
class JbdiUsersRepository (private val jdbi: Jdbi): UsersRepository{
    override fun getById(id: Int): User? {
        return jdbi.withHandle<User?,Exception>{handle ->
            handle.createQuery("")
                .bind("id", id)
                .mapTo(User::class.java)
                .singleOrNull()

        }
    }

    override fun insert(name: String) {
        TODO("Not yet implemented")
    }

}
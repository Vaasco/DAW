package com.example.demo.repository.jbdi

import com.example.demo.repository.Transaction
import org.jdbi.v3.core.Handle

class JdbiTransaction(
    private val handle : Handle
) : Transaction {
    override val usersRepository = JdbiUsersRepository(handle)
    override val gameRepository = JdbiGamesRepository(handle)
    override fun rollback() {
        handle.rollback()
    }
}
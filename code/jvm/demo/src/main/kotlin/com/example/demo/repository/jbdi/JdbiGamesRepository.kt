package com.example.demo.repository.jbdi


import com.example.demo.domain.Game
import com.example.demo.repository.GamesRepository

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.Query
import org.springframework.stereotype.Component


@Component
class JbdiGamesRepository(private val jdbi: Jdbi) : GamesRepository {
    override fun getById(id: Int): Game? {
        TODO("Not yet implemented")
    }

    override fun updateGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun createGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun createLobby() {
        jdbi.useHandle<Exception> { handle ->
            val query = "INSERT INTO lobby DEFAULT VALUES"
            handle.createUpdate(query)
                .execute()
        }
    }

}
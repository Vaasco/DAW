package com.example.demo.repository.jbdi


import com.example.demo.domain.*
import com.example.demo.repository.GamesRepository

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component


@Component
class JbdiGamesRepository(private val jdbi: Jdbi) : GamesRepository {
    override fun getById(id: Int): Game? {
        return jdbi.withHandle<Game?, Exception> { handle ->
            handle.createQuery("select id, board, state, rules, variant from game where id = :id")
                .bind("id", id)
                .mapTo(Game::class.java)
                .singleOrNull()
        }
    }

    override fun updateGame(game: Game) {
        jdbi.useHandle<Exception> { handle ->
            val board = game.board as BoardRun
            val id = game.id
            val query = "UPDATE game SET board = :board, turn = :turn WHERE id = :id"
            handle.createUpdate(query)
                .bind("board", board.toString()) // Assuming you need to bind a string representation of the board.
                .bind("turn", game.board.turn.string) // Assuming 'turn' is a property in your 'Game' class.
                .bind("id", id) // Assuming you have a unique identifier for the game.
                .execute()
        }
    }


    override fun createGame(state: String, rules: String, variant: String, board: BoardRun) {
        jdbi.useHandle<Exception> { handle ->
            handle.createUpdate("INSERT INTO game (board, turn ,state, rules, variant) VALUES (:board,:turn ,:state, :rules, :variant)")
                .bind("board", board.toString())
                .bind("turn", board.turn.string)
                .bind("state", state)
                .bind("rules", rules)
                .bind("variant", variant)
                .execute()
        }
    }


    override fun createLobby() {
        jdbi.useHandle<Exception> { handle ->
            val query = "INSERT INTO lobby DEFAULT VALUES"
            handle.createUpdate(query)
                .execute()
        }
    }

    override fun getGameState(id: Int): String? {
        return jdbi.withHandle<String?, java.lang.Exception> { handle ->
            val query = "select state from game where id = :id"
            handle.createQuery(query)
                .bind("id", id)
                .mapTo(String::class.java)
                .singleOrNull()
        }
    }

}
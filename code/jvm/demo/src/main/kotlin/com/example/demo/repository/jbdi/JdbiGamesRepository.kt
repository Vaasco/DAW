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

    /*override fun createGame(rules: String, variant: String, boardSize: Int, board: BoardRun) {
        jdbi.useHandle<Exception> { handle ->
            handle.createUpdate("insert into game (rules, variant, board_size) VALUES (:rules, :variant, :board_size)")
                .bind("rules", rules)
                .bind("variant", variant)
                .bind("board_size", boardSize)
                .execute()
        }
    }*/


    override fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int) {
    jdbi.useHandle<Exception> { handle ->
            val query = "insert into lobby (player1_id, rules, variant, board_size) values (:playerId, :rules, :variant, :boardSize)"
            handle.createUpdate(query)
                .bind("player1_id",playerId)
                .bind("rules",rules)
                .bind("variant", variant)
                .bind("boardSize", boardSize)
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
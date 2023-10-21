package com.example.demo.repository.jbdi

import com.example.demo.domain.Game
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import org.jdbi.v3.core.Handle

class JdbiGamesRepository(/*private val jdbi: Jdbi*/private val handle: Handle) : GamesRepository {

    override fun updateGame(game: Game, turn: Player, state: String) {
        val query = "update game set board = :board, state = :state, turn = :turn where id = :id"

        handle.createUpdate(query)
            .bind("board", game.board.toString()) // Assuming you need to bind a string representation of the board.
            .bind("state", state)
            .bind("turn", turn.string) // Assuming 'turn' is a property in your 'Game' class.
            .bind("id", game.id) // Assuming you have a unique identifier for the game.
            .execute()

    }

    override fun getGameById(id: Int): GameModel? {

        return handle.createQuery("select id, board, turn, state, player_b, player_w, rules, variant, board_size " +
                "from game where id = :id")
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .singleOrNull()

    }

    override fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int) {

        val query =
            "insert into lobby (player1_id, rules, variant, board_size) values (:playerId, :rules, :variant, :boardSize)"
        handle.createUpdate(query)
            .bind("playerId", playerId)
            .bind("rules", rules)
            .bind("variant", variant)
            .bind("boardSize", boardSize)
            .execute()

    }

    override fun getGame(id: Int): GameModel? {
        val query =
            "select id, board, turn, state, player_b, player_w, rules, variant, board_size from game where id = :id"

        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .singleOrNull()

    }
}

package com.example.demo.repository.jbdi

import com.example.demo.domain.GameUpdate
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import org.jdbi.v3.core.Handle

class JdbiGamesRepository(private val handle: Handle) : GamesRepository {

    override fun updateGame(game: GameUpdate, turn: Player, state: String) {
        val query = "update game set board = :board, state = :state, turn = :turn where id = :id"
        handle.createUpdate(query)
            .bind("board", game.board.toString())
            .bind("state", state)
            .bind("turn", turn.string)
            .bind("id", game.id)
            .execute()
    }

    override fun getGameById(id: Int): GameModel? {
        return handle.createQuery(
            "select id, board, turn, state, player_b, player_w, rules, variant, board_size " +
                    "from game where id = :id"
        )
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .singleOrNull()
    }

    override fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int): String? {
        val query =
            "insert into lobby (player1_id, rules, variant, board_size) " +
                    "values (:playerId, :rules, :variant, :boardSize) returning id"

        val query2 = "select game_id from lobby where id = :id"// order by id desc limit 1"
        val x = handle.createQuery(query)
            .bind("playerId", playerId)
            .bind("rules", rules)
            .bind("variant", variant)
            .bind("boardSize", boardSize)
            .mapTo(Int::class.java)
            .singleOrNull()
        val y = handle.createQuery(query2)
            .bind("id", x)
            .mapTo(String::class.java)
            .singleOrNull()
        return y
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

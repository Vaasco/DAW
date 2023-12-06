package com.example.demo.repository.jbdi

import com.example.demo.domain.Board
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import org.jdbi.v3.core.Handle

class JdbiGamesRepository(private val handle: Handle) : GamesRepository {

    override fun updateGame(id: Int, board: Board, state: String): GameModel {
        val query = "update game g set board = :board, state = :state where id = :id returning *"
        return handle.createQuery(query)
            .bind("board", board.toString())
            .bind("state", state)
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .single()
    }

    override fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int) {
        val query = "insert into lobby (player1_id, rules, variant, board_size) " +
                "values (:playerId, :rules, :variant, :boardSize)"
        handle.createUpdate(query)
            .bind("playerId", playerId)
            .bind("rules", rules)
            .bind("variant", variant)
            .bind("boardSize", boardSize)
            .execute()
    }

    override fun getGameId(playerId: Int): String? {
        val query = "select game_id from lobby where player2_id = :playerId order by id desc limit 1"
        return handle.createQuery(query)
            .bind("playerId", playerId)
            .mapTo(String::class.java)
            .singleOrNull()
    }

    override fun getGameById(id: Int): GameModel? {
        val query = "select id, board, state, player_b, player_w, board_size from game where id = :id"
        return handle.createQuery(query)
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .singleOrNull()
    }

    override fun getLastGame(username: String): GameModel? {
        val query = "select g.id, board, state, player_b, player_w, board_size from game g " +
                "join player p on (g.player_w = p.id or g.player_b = p.id) where p.username = :username " +
                "and g.state = 'Playing'"
        return handle.createQuery(query)
            .bind("username", username)
            .mapTo(GameModel::class.java)
            .singleOrNull()
    }

    override fun swapPlayers(gameId: Int) {
        val query = "update game g set player_b = g.player_w, player_w = g.player_b where id = :id"
        handle.createUpdate(query)
            .bind("id", gameId)
            .execute()
    }
}

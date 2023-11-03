package com.example.demo.repository.jbdi

import com.example.demo.domain.GameUpdate
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import org.jdbi.v3.core.Handle

class JdbiGamesRepository(private val handle: Handle) : GamesRepository {

    override fun updateGame(game: GameUpdate, turn: Player, state: String) {
        val query = "update game set board = :board, state = :state where id = :id"
        handle.createUpdate(query)
            .bind("board", game.board.toString())
            .bind("state", state)
            .bind("turn", turn.string)
            .bind("id", game.id)
            .execute()
    }




    override fun getGameById(id: Int): GameModel? {
        return handle.createQuery(
            "select id, board, state, player_b, player_w, rules, variant, board_size " +
                    "from game where id = :id"
        )
            .bind("id", id)
            .mapTo(GameModel::class.java)
            .singleOrNull()
    }

    override fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int) {
        val query =
            "insert into lobby (player1_id, rules, variant, board_size) " +
                    "values (:playerId, :rules, :variant, :boardSize)"

        //val query2 = "select game_id from lobby where id = :id"
        handle.createUpdate(query)
            .bind("playerId", playerId)
            .bind("rules", rules)
            .bind("variant", variant)
            .bind("boardSize", boardSize)
            .execute()
            /*.mapTo(Int::class.java)
            .single()*/
        //return x
        /*val y = handle.createQuery(query2)
            .bind("id", x)
            .mapTo(String::class.java)
            .singleOrNull()*/
        //return y
    }

    override fun getGameId(playerId: Int): String? {
        val query = "select game_id from lobby where player2_id = :playerId order by id desc limit 1"
        return handle.createQuery(query)
            .bind("playerId", playerId)
            .mapTo(String::class.java)
            .singleOrNull()
    }

    override fun getGame(id: Int): GameModel? {
        val query =
            "select id, board, state, player_b, player_w, rules, variant, board_size from game where id = :id"
        return handle.createQuery(query)
            .bind("id", id)
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

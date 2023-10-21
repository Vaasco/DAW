package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component

@Component
class GamesService(private val transactionManager: TransactionManager) {
    fun getGameById(id: Int): GameModel? {
        return transactionManager.run {
            it.gameRepository.getGameById(id)
        }
    }

    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

    fun createLobby(playerId: Int?, rules: String?, variant: String?, boardSize: Int?) {
        return transactionManager.run{
            require(playerId != null && it.usersRepository.getUserById(playerId) != null) { "Invalid player" }
            require(rules != null && validRules.contains(rules)) { "Invalid rules" }
            require(variant != null && validVariants.contains(variant)) { "Invalid variant" }
            require(boardSize != null) { "Invalid boardSize" }
            it.gameRepository.createLobby(playerId, rules, variant, boardSize)
        }
    }

    fun getGameState(id: Int?): GameModel? {
        return transactionManager.run {
            require(id != null) { "Invalid id" }
            it.gameRepository.getGame(id)
        }
    }

    fun play(gameId: Int?, row: Int?, col: Int?, playerId: Int?) {
        return transactionManager.run {
            require(gameId != null) { "Invalid game id" }
            require(row != null) { "Invalid row" }
            require(col != null) { "Invalid column" }
            require(playerId != null) { "Invalid player id" }
            val player = it.usersRepository.getUserById(playerId)
            require(player != null) { "There's no player with the given id" }
            val game = getGameById(gameId)
            require(game != null) { "There's no game with the given id" }
            require(row < game.boardSize && col < game.boardSize) { "Invalid position" }
            require(game.state == "Playing") { "Game has already ended" }
            if (game.turn == "W")
                require(playerId == game.playerW) {"It's not your turn"}
            else
                require(playerId == game.playerB) {"It's not your turn"}
            val position = Position(row.indexToRow(), col.indexToColumn())
            val turn = (game.board as BoardRun).turn
            val newBoard = game.board.play(position, turn)
            val state = when(newBoard){
                is BoardDraw -> "Ended D"
                is BoardWin -> "Ended $turn"
                else -> game.state
            }
            val newGame = Game(game.id, newBoard, game.state)
            it.gameRepository.updateGame(newGame, turn.other(), state)
        }
    }
}

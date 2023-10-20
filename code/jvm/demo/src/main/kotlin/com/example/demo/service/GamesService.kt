package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import com.example.demo.repository.UsersRepository
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository, private val usersRepository: UsersRepository) {
    fun getGameById(id: Int) = gameRepository.getGameById(id)

    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

    fun createLobby(playerId: Int?, rules: String?, variant: String?, boardSize: Int?) {
        require(playerId != null && usersRepository.getUserById(playerId) != null) { "Invalid player" }
        require(rules != null && validRules.contains(rules)) { "Invalid rules" }
        require(variant != null && validVariants.contains(variant)) { "Invalid variant" }
        require(boardSize != null) { "Invalid boardSize" }
        gameRepository.createLobby(playerId, rules, variant, boardSize)
    }

    fun getGameState(id: Int?): GameModel? {
        require(id != null && getGameById(id) != null) { "Invalid id" }
        return gameRepository.getGame(id)
    }

    fun play(gameId: Int, row: Int, col: Int) {
        val game = getGameById(gameId)
        require(game != null) { "Invalid game id" }
        require(game.state == "Playing") { "Game has already ended"}
        val position = Position(row.indexToRow(), col.indexToColumn())
        val turn = (game.board as BoardRun).turn
        val newBoard = game.board.play(position, turn)
        val state = when(newBoard){
            is BoardDraw -> "Ended D"
            is BoardWin -> "Ended $turn"
            else -> game.state
        }
        val newGame = Game(game.id, newBoard, game.state, game.rules, game.variant)
        gameRepository.updateGame(newGame, turn, state)
    }
}

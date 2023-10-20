package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.repository.GamesRepository
import com.example.demo.repository.UsersRepository
import com.example.demo.service.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository, private val usersRepository: UsersRepository) {
    fun getById(id: Int) = gameRepository.getGameById(id)

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
        require(id != null && gameRepository.getGameById(id) != null) { "Invalid id" }
        return gameRepository.getGame(id)
    }

    fun play(gameId: Int, row: Int, col: Int) {
        require(gameRepository.getGameById(gameId) != null) { "Invalid id" }
        val game = gameRepository.getGameById(gameId) ?: throw NotFoundException()
        val position = Position(row.indexToRow(), col.indexToColumn())
        val newBoard = game.board.play(position = position, (game.board as BoardRun).turn)
        gameRepository.updateGame(Game(gameId, newBoard, game.state, game.rules, game.variant))
    }
}

package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.repository.GamesRepository
import com.example.demo.repository.UsersRepository
import com.example.demo.service.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository, private val usersRepository: UsersRepository) {
    fun getById(id: Int) = gameRepository.getById(id)

    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

    fun createLobby(playerId: Int?, rules: String?, variant: String?, boardSize: Int?) {
        require(playerId != null && usersRepository.getUserById(playerId) != null) {"Invalid player"}
        require(rules != null && validRules.contains(rules)) {"Invalid rules"}
        require(variant != null && validVariants.contains(variant)) {"Invalid variant"}
        require(boardSize != null) {"Invalid boardSize"}
        gameRepository.createLobby(playerId, rules, variant, boardSize)
    }
    /*fun createGame(rules: String, variant: String, boardSize: Int, board: BoardRun){
        return gameRepository.createGame(rules, variant, boardSize, board)
    }*/

    fun getGameState(id: Int?) {
        require( id != null && gameRepository.getById( id ) != null){"Invalid id"}
        gameRepository.getGameState(id)
    }

    fun play(gameId: Int, row: Int, col: Int) {
        require(gameRepository.getById( gameId ) != null){"Invalid id"}
        val game = gameRepository.getById(gameId) ?: throw NotFoundException()
        val position = Position(row.indexToRow(), col.indexToColumn())
        val newBoard = game.board.play(position = position, (game.board as BoardRun).turn)
        gameRepository.updateGame(Game(gameId, newBoard, game.state, game.rules, game.variant))
    }
}

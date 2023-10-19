package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.repository.GamesRepository
import com.example.demo.service.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository) {
    fun getById(id: Int) = gameRepository.getById(id)

    fun createLobby() = gameRepository.createLobby()

    fun createGame(state: String?, rules: String?, variant: String?, board: BoardRun?){
        require(state != null) {"Invalid state"}
        require(rules != null) {"Invalid rules"}
        require(variant != null) {"Invalid variant"}
        require(board != null) {"Invalid board"}
        return gameRepository.createGame(state, rules, variant, board)
    }

    fun getGameState(id: Int) = gameRepository.getGameState(id)

    fun play(gameId: Int, row: Int, col: Int) {
        val game = gameRepository.getById(gameId) ?: throw NotFoundException()
        val position = Position(row.indexToRow(), col.indexToColumn())
        val newBoard = game.board.play(position = position, (game.board as BoardRun).turn)
        gameRepository.updateGame(Game(gameId, newBoard, game.state, game.rules, game.variant))
    }
}

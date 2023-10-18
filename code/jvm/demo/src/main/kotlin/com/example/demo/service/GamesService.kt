package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.repository.GamesRepository
import com.example.demo.service.exception.NotFoundException
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository) {
    fun getById(id: Int) = gameRepository.getById(id)

    fun createLobby() = gameRepository.createLobby()

    fun createGame(state: String, rules: String, variant: String, board: BoardRun) =
        gameRepository.createGame(state, rules, variant, board)

    fun play(gameId: Int, row: Int, col: Int) {
        val game = gameRepository.getById(gameId) ?: throw NotFoundException()
        val position = Position(row.indexToRow(), col.indexToColumn())
        val newBoard = game.board.play(position = position, (game.board as BoardRun).turn)
        gameRepository.updateGame(Game(gameId, newBoard, game.state, game.rules, game.variant))
    }
}
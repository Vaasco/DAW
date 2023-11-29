package com.example.demo.repository

import com.example.demo.domain.Board
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel

interface GamesRepository {
    fun updateGame(id: Int, board: Board, state: String): GameModel

    fun swapPlayers(gameId : Int)

    fun getGameById(id : Int): GameModel?

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)

    fun getGameId(playerId: Int): String?
}
package com.example.demo.repository

import com.example.demo.domain.Board
import com.example.demo.http.model.GameModel

interface GamesRepository {
    fun updateGame(id: Int, board: Board, state: String): GameModel

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)

    fun getGameId(playerId: Int): String?

    fun getGameById(id: Int): GameModel?

    fun getLastGame(username: String): GameModel?

    fun swapPlayers(gameId: Int)

    fun forfeitGame(id: Int, player: String): Boolean

    /*fun cancelLobby(id: Int)

    fun getLobbyId(userId: Int): Int?*/
}

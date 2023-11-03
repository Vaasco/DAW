package com.example.demo.repository

import com.example.demo.domain.GameUpdate
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel

interface GamesRepository {
    fun updateGame(game: GameUpdate, turn: Player, state: String)

    fun swapPlayers(gameId : Int)

    fun getGameById(id: Int): GameModel?

    fun getGame(id : Int):GameModel?

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)

    fun getGameId(playerId: Int): String?
}
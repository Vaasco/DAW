package com.example.demo.repository

import com.example.demo.domain.BoardRun
import com.example.demo.domain.Game
import com.example.demo.domain.Player
import com.example.demo.http.model.GameModel

interface GamesRepository {
    fun updateGame(game: Game, turn: Player, state: String)

    fun getGameById(id: Int): GameModel?

    fun getGame(id : Int):GameModel?

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)
}
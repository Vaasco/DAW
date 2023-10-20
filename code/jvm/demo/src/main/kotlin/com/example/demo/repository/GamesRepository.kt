package com.example.demo.repository

import com.example.demo.domain.BoardRun
import com.example.demo.domain.Game
import com.example.demo.http.model.GameModel

interface GamesRepository {

    fun updateGame(game: Game)

    fun getGameById(id: Int): Game?
    fun getGame(id : Int):GameModel?

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)
}
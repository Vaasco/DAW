package com.example.demo.repository

import com.example.demo.domain.BoardRun
import com.example.demo.domain.Game

interface GamesRepository {
    fun getById(id: Int): Game?

    fun updateGame(game: Game)

    //fun createGame(rules: String, variant: String, boardSize: Int, board: BoardRun)

    fun getGameState(id : Int):String?

    fun createLobby(playerId: Int, rules: String, variant: String, boardSize: Int)
}
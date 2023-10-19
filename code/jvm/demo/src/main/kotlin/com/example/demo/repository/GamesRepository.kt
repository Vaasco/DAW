package com.example.demo.repository

import com.example.demo.domain.BoardRun
import com.example.demo.domain.Game

interface GamesRepository {
    fun getById(id: Int): Game?
    fun updateGame(game: Game)
    fun createGame(state:String,rules:String,variant:String,board:BoardRun)

    fun getGameState(id : Int):String?

    fun createLobby()
}
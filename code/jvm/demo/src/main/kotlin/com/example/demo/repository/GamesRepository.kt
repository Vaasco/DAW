package com.example.demo.repository

import com.example.demo.domain.Game
import java.util.*

interface GamesRepository {
    fun getById(id: Int): Game?
    fun updateGame(game: Game)
    fun createGame(game: Game)

    fun createLobby()
}
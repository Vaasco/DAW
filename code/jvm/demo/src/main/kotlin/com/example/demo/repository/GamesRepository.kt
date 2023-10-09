package com.example.demo.repository

import com.example.demo.domain.Game
import java.util.*

interface GamesRepository {
    fun getById(id: UUID): Game?
    fun update(game: Game)
    fun insert(game: Game)
}
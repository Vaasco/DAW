package com.example.demo.service

import com.example.demo.repository.GamesRepository
import org.springframework.stereotype.Component

@Component
class GamesService(private val gameRepository: GamesRepository) {
    fun getById(id: Int) = gameRepository.getById(id)

    fun createLobby() = gameRepository.createLobby()
}
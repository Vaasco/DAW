package com.example.demo.http


import com.example.demo.service.GamesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController (private val gamesService : GamesService) {

    @GetMapping(PathTemplate.START)
    fun createLobby() = gamesService.createLobby()
}
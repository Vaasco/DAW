package com.example.demo.http

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.http.model.LobbyModel
import com.example.demo.http.model.PlayModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.example.demo.http.siren.SirenMaker
import com.example.demo.service.*

@RestController
class GameController(private val gamesService: GamesService) {

    @GetMapping(PathTemplate.GAME_BY_ID)
    fun getGameById(@PathVariable id: Int): ResponseEntity<*> {
        val res = gamesService.getGameById(id)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetGameById(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @PostMapping(PathTemplate.START_GAME)
    fun createLobby(@RequestBody lobbyModel: LobbyModel, user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.createLobby(
            lobbyModel.playerId,
            lobbyModel.rules,
            lobbyModel.variant,
            lobbyModel.boardSize,
            user
        )
        return handleResponse(res) {
            val siren = SirenMaker().sirenCreateLobby(it)
            ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody play: PlayModel?, user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.play(id, play, user)
        return handleResponse(res) {
            val siren = SirenMaker().sirenPlay(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }
}
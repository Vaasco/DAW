package com.example.demo.http

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.http.model.LobbyModel
import com.example.demo.http.model.PlayModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.example.demo.http.siren.SirenMaker
import com.example.demo.http.siren.response
import com.example.demo.service.*

@RestController
class GameController(private val gamesService: GamesService) {

    @GetMapping(PathTemplate.GAME_BY_ID)
    fun getGameById(@PathVariable id: Int): ResponseEntity<*> {
        val res = gamesService.getGameById(id)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetGameById(it)
            siren.response(200)
        }
    }

    @GetMapping(PathTemplate.LAST_GAME)
    fun getLastGame(@PathVariable username: String): ResponseEntity<*> {
        val res = gamesService.getLastGame(username)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetLastGame(it)
            siren.response(200)
        }
    }

    @PostMapping(PathTemplate.START_GAME)
    fun createLobby(@RequestBody lobbyModel: LobbyModel, user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.createLobby(
            lobbyModel.rules,
            lobbyModel.variant,
            lobbyModel.boardSize,
            user
        )
        return handleResponse(res) {
            val siren = SirenMaker().sirenCreateLobby(it)
            siren.response(201)
        }
    }

    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody play: PlayModel?, user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.play(id, play, user)
        return handleResponse(res) {
            val siren = SirenMaker().sirenPlay(it)
            siren.response(200)
        }
    }

    @PostMapping(PathTemplate.FORFEIT)
    fun forfeit(@PathVariable id: Int, user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.forfeitGame(id, user)
        return handleResponse(res) {
            val siren = SirenMaker().sirenForfeit(it)
            siren.response(200)
        }
    }
    /*@PostMapping(PathTemplate.CANCEL)
    fun cancelLobby(@RequestBody lobbyId : Int,user: AuthenticatedUser?): ResponseEntity<*> {
        val res = gamesService.cancelLobby(lobbyId,user)
        return handleResponse(res) {
            val siren = SirenMaker().sirenCancel(it)
            siren.response(200)
        }
    }*/
}
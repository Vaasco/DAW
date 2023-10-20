package com.example.demo.http

import com.example.demo.http.model.LobbyModel
import com.example.demo.http.model.PositionModel
import com.example.demo.service.GamesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gamesService: GamesService) {

    @GetMapping(PathTemplate.CHECK_GAME)
    fun getById(@PathVariable id: Int) = gamesService.getById(id)

    @PostMapping(PathTemplate.START_GAME)
    fun createLobby(
        @RequestBody lobbyModel: LobbyModel
    ) = gamesService.createLobby(lobbyModel.playerId, lobbyModel.rules, lobbyModel.variant, lobbyModel.boardSize)

    @GetMapping(PathTemplate.GAME_STATE)
    fun getGame(@PathVariable id: Int) = gamesService.getGameState(id)


    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody pos: PositionModel) {
        gamesService.play(id, pos.row, pos.col)
    }

}
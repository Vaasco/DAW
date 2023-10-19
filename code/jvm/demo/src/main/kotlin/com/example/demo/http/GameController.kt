package com.example.demo.http

import com.example.demo.http.model.PositionInputModel
import com.example.demo.service.GamesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gamesService: GamesService) {

    @PostMapping(PathTemplate.START_GAME)
    fun createLobby(
        @RequestBody playerId: Int,
        @RequestBody rules: String,
        @RequestBody variant: String,
        @RequestBody boardSize: Int
    ) = gamesService.createLobby(playerId,rules,variant,boardSize)

    /*@PostMapping(PathTemplate.CREATE_GAME)
    fun createGame(@RequestBody tg: GameInputModel) {
        gamesService.createGame(
            tg.state,
            tg.rules,
            tg.variant,
            BoardRun(mapOf<Position, Player>("4A".toPosition() to Player.WHITE), turn = Player.WHITE)
        )
    }*/

    @GetMapping(PathTemplate.GAME_STATE)
    fun getGameState(@PathVariable id: Int) = gamesService.getGameState(id)

    @GetMapping(PathTemplate.CHECK_GAME)
    fun getById(@PathVariable id: Int) = gamesService.getById(id)

    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody pos: PositionInputModel) {
        gamesService.play(id, pos.row,pos.col)
    }

}
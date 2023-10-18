package com.example.demo.http


import com.example.demo.domain.*
import com.example.demo.http.model.GameInputModel
import com.example.demo.http.model.PositionInputModel
import com.example.demo.service.GamesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val gamesService: GamesService) {

    @PostMapping(PathTemplate.START)//Mudar o path tbm
    fun createLobby() = gamesService.createLobby() // A tabela mudou então temos que mudar isto

    @PostMapping(PathTemplate.CREATE_GAME)
    fun createGame(@RequestBody tg: GameInputModel) {
        gamesService.createGame(
            tg.state,
            tg.rules,
            tg.variant,
            BoardRun(mapOf<Position, Player>("4A".toPosition() to Player.WHITE), turn = Player.WHITE)
        )
    }

    @GetMapping(PathTemplate.CHECK_GAME)
    fun getById(@PathVariable id: Int) = gamesService.getById(id)

    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody pos: PositionInputModel) {
        gamesService.play(id, pos.row,pos.col)
    }

}
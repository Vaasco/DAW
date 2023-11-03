package com.example.demo.http

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.domain.Failure
import com.example.demo.domain.Success
import com.example.demo.http.model.LobbyModel
import com.example.demo.http.model.PlayModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import com.example.demo.domain.Error
import com.example.demo.service.*

@RestController
class GameController(private val gamesService: GamesService) {

    @GetMapping(PathTemplate.CHECK_GAME)
    fun getGameById(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = gamesService.getGameById(id)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)

            is Failure -> when (res.value) {
                GameIdFetchError.NonExistingGame ->
                    Error.response(Error.nonExistingGame.code, Error.nonExistingGame)

                GameIdFetchError.InvalidId -> Error.response(Error.invalidId.code, Error.invalidId)
            }
        }
    }

    @PostMapping(PathTemplate.START_GAME)
    fun createLobby(@RequestBody lobbyModel: LobbyModel, user: AuthenticatedUser): ResponseEntity<*> {
        val res = gamesService.createLobby(
            lobbyModel.playerId,
            lobbyModel.rules,
            lobbyModel.variant,
            lobbyModel.boardSize,
            user
        )
        return when (res) {
            is Success -> ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)

            is Failure -> when (res.value) {
                CreateLobbyError.InvalidBoardSize -> {
                    val error = Error.response(400, Error.invalidId)
                    ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(error)
                }

                CreateLobbyError.InvalidId -> Error.response(Error.invalidId.code, Error.invalidId)
                CreateLobbyError.InvalidVariant -> Error.response(Error.invalidVariant.code, Error.invalidVariant)
                CreateLobbyError.InvalidRules -> Error.response(Error.invalidRules.code, Error.invalidRules)
                CreateLobbyError.WrongAccount -> Error.response(Error.wrongAccount.code, Error.wrongAccount)
            }
        }
    }

    @GetMapping(PathTemplate.GAME_STATE)
    fun getGame(@PathVariable id: Int): ResponseEntity<*> {
        return when (val res = gamesService.getGameState(id)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)

            is Failure -> when (res.value) {
                GameStateFetchError.NonExistingGame -> Error.response(Error.nonExistingGame.code, Error.nonExistingGame)
                GameStateFetchError.InvalidId -> Error.response(Error.invalidId.code, Error.invalidId)
            }
        }
    }


    @PostMapping(PathTemplate.PLAY)
    fun play(@PathVariable id: Int, @RequestBody play: PlayModel?, user: AuthenticatedUser): ResponseEntity<*> {
        return when (val res = gamesService.play(id, play, user)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)

            is Failure -> when (res.value) {
                PlayError.GameEnded -> Error.response(Error.gameEnded.code, Error.gameEnded)
                PlayError.InvalidCol -> Error.response(Error.invalidCol.code, Error.invalidCol)
                PlayError.InvalidGameId -> Error.response(Error.invalidGameId.code, Error.invalidGameId)
                PlayError.InvalidPlayerId -> Error.response(Error.invalidUserId.code, Error.invalidUserId)
                PlayError.InvalidPosition -> Error.response(Error.invalidPosition.code, Error.invalidPosition)
                PlayError.InvalidRow -> Error.response(Error.invalidRow.code, Error.invalidRow)
                PlayError.NonExistingGame -> Error.response(Error.nonExistingGame.code, Error.nonExistingGame)
                PlayError.NonExistingUser -> Error.response(Error.nonExistingUserId.code, Error.nonExistingUserId)
                PlayError.NotYourTurn -> Error.response(Error.notYourTurn.code, Error.notYourTurn)
                PlayError.WrongAccount -> Error.response(Error.unauthorized.code, Error.unauthorized)
                PlayError.PositionOccupied -> Error.response(Error.positionOccupied.code, Error.positionOccupied)
                PlayError.WrongGame -> Error.response(Error.wrongGame.code, Error.wrongGame)
            }
        }
    }
}
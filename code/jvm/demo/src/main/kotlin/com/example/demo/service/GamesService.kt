package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component

sealed class CreateLobbyError {
    object InvalidId : CreateLobbyError()

    object InvalidRules : CreateLobbyError()

    object InvalidVariant : CreateLobbyError()

    object InvalidBoardSize : CreateLobbyError()

    object WrongAccount : CreateLobbyError()
}

typealias CreateLobbyResult = Either<CreateLobbyError, Unit>

sealed class GameIdFetchError {
    object InvalidId : GameIdFetchError()
    object NonExistingGame : GameIdFetchError()
}

typealias GameIdFetchResult = Either<GameIdFetchError, GameModel>

sealed class GameStateFetchError {
    object InvalidId : GameStateFetchError()

    object NonExistingGame : GameStateFetchError()
}

typealias GameStateFetchResult = Either<GameStateFetchError, GameModel>

sealed class PlayError {
    object InvalidGameId : PlayError()

    object InvalidRow : PlayError()

    object InvalidCol : PlayError()

    object NonExistingUser : PlayError()

    object InvalidPlayerId : PlayError()

    object NonExistingGame : PlayError()

    object InvalidPosition: PlayError()

    object GameEnded : PlayError()

    object WrongAccount : PlayError()

    object NotYourTurn : PlayError()
}

typealias PlayResult = Either<PlayError, Unit>

@Component
class GamesService(private val transactionManager: TransactionManager) {
    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

    fun getGameById(id: Int?): GameIdFetchResult {
        return transactionManager.run {
            if (id == null) failure(GameIdFetchError.InvalidId)
            else {
                val game = it.gameRepository.getGameById(id)
                if (game == null) failure(GameIdFetchError.NonExistingGame)
                else success(game)
            }
        }
    }

    fun createLobby(playerId: Int?, rules: String?, variant: String?, boardSize: Int?, user: AuthenticatedUser): CreateLobbyResult {
        return transactionManager.run {
            when {
                playerId == null || it.usersRepository.getUserById(playerId) == null -> failure(CreateLobbyError.InvalidId)
                rules == null || !validRules.contains(rules) -> failure(CreateLobbyError.InvalidRules)
                variant == null || !validVariants.contains(variant) -> failure(CreateLobbyError.InvalidVariant)
                boardSize == null -> failure(CreateLobbyError.InvalidBoardSize)
                it.usersRepository.getUserByToken(user.token)!!.id != playerId -> failure(CreateLobbyError.WrongAccount)
                else -> {
                    success(it.gameRepository.createLobby(playerId, rules, variant, boardSize))
                }
            }
        }
    }

    fun getGameState(id: Int?): GameStateFetchResult {
        return transactionManager.run {
            if (id == null) failure(GameStateFetchError.InvalidId)
            else {
                val game = it.gameRepository.getGame(id)
                if (game == null) failure(GameStateFetchError.NonExistingGame)
                else success(game)
            }
        }
    }

    fun play(gameId: Int?, row: Int?, col: Int?, playerId: Int?, user: AuthenticatedUser): PlayResult {
        return transactionManager.run {
            //require(gameId != null) { "Invalid game id" }
            if (gameId == null) failure(PlayError.InvalidGameId)
            else {
                if (row == null) failure(PlayError.InvalidRow)
                //require(col != null) { "Invalid column" }
                if (col == null) failure(PlayError.InvalidCol)
                //require(playerId != null) { "Invalid player id" }
                if (playerId == null) failure(PlayError.InvalidPlayerId)
                else {
                    val player = it.usersRepository.getUserById(playerId)
                    //require(player != null) { "There's no player with the given id" }
                    if (player == null) failure(PlayError.NonExistingUser)
                    val game = it.gameRepository.getGameById(gameId)
                    if (game == null) failure(PlayError.NonExistingGame)
                    else {
                        if ((row!! > game.boardSize) || (col!! > game.boardSize)) failure(PlayError.InvalidPosition)
                        //require(row < game.boardSize && col < game.boardSize) { "Invalid position" }
                        //require(game.state == "Playing") { "Game has already ended" }
                        if (game.state != "Playing") failure(PlayError.GameEnded)
                        if (user.user.id != playerId) failure(PlayError.WrongAccount)
                        //require(user.user.id == playerId) { "That's not your account" }
                        if ((game.turn == "W" && playerId != game.playerW) || game.turn == "B" && playerId != game.playerB) {
                            failure(PlayError.NotYourTurn)
                        }
                        /*if (game.turn == "W")
                            require(playerId == game.playerW) { "It's not your turn" }
                        else
                            require(playerId == game.playerB) { "It's not your turn" }*/

                        val position = Position(row.indexToRow(), col!!.indexToColumn())
                        val turn = (game.board as BoardRun).turn
                        val newBoard = game.board.play(position, turn)
                        val state = when (newBoard) {
                            is BoardDraw -> "Ended D"
                            is BoardWin -> "Ended $turn"
                            else -> game.state
                        }
                        val newGame = Game(game.id, newBoard, game.state)
                        success(it.gameRepository.updateGame(newGame, turn.other(), state))
                    }
                }
            }
        }
    }
}

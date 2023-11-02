package com.example.demo.service

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.domain.BoardDraw
import com.example.demo.domain.BoardRun
import com.example.demo.domain.BoardWin
import com.example.demo.domain.Either
import com.example.demo.domain.GameUpdate
import com.example.demo.domain.Message
import com.example.demo.domain.Position
import com.example.demo.domain.failure
import com.example.demo.domain.indexToColumn
import com.example.demo.domain.indexToRow
import com.example.demo.domain.success
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

typealias CreateLobbyResult = Either<CreateLobbyError, Message>

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

    object InvalidPosition : PlayError()

    object GameEnded : PlayError()

    object WrongAccount : PlayError()

    object NotYourTurn : PlayError()

    object PositionOccupied : PlayError()
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

    fun createLobby(
        playerId: Int?,
        rules: String?,
        variant: String?,
        boardSize: Int?,
        user: AuthenticatedUser
    ): CreateLobbyResult {
        return transactionManager.run {
            when {
                playerId == null || it.usersRepository.getUserById(playerId) == null -> failure(CreateLobbyError.InvalidId)
                rules == null || !validRules.contains(rules) -> failure(CreateLobbyError.InvalidRules)
                variant == null || !validVariants.contains(variant) -> failure(CreateLobbyError.InvalidVariant)
                boardSize == null -> failure(CreateLobbyError.InvalidBoardSize)
                it.usersRepository.getUserByToken(user.token)!!.id != playerId -> failure(CreateLobbyError.WrongAccount)
                else -> {
                    it.gameRepository.createLobby(playerId, rules, variant, boardSize)
                    val gameId = it.gameRepository.getGameId(playerId)
                    success(
                        Message(gameId ?: "Waiting for an opponent")
                    )
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
            if (gameId == null) return@run failure(PlayError.InvalidGameId)
            if (row == null) return@run failure(PlayError.InvalidRow)
            if (col == null) return@run failure(PlayError.InvalidCol)
            if (playerId == null) return@run failure(PlayError.InvalidPlayerId)
            if (it.usersRepository.getUserById(playerId) == null) return@run failure(PlayError.NonExistingUser)
            val game = it.gameRepository.getGameById(gameId) ?: return@run failure(PlayError.NonExistingGame)
            val r = row.indexToRow()
            if (r.number == -1) return@run failure(PlayError.InvalidRow)
            val c = col.indexToColumn()
            if (c.symbol == '?') return@run failure(PlayError.InvalidCol)
            if ((row > game.boardSize) || (col > game.boardSize)) return@run failure(PlayError.InvalidPosition)
            val position = Position(r, c)
            if (game.board.moves[position] != null) return@run failure(PlayError.PositionOccupied)
            if (game.state != "Playing") return@run failure(PlayError.GameEnded)
            if (user.user.id != playerId) return@run failure(PlayError.WrongAccount)
            if ((game.turn == "W" && playerId != game.playerW) || game.turn == "B" && playerId != game.playerB) {
                return@run failure(PlayError.NotYourTurn)
            }
            val turn = (game.board as BoardRun).turn
            val newBoard = game.board.play(position, turn)
            if (newBoard == game.board) return@run failure(PlayError.GameEnded)
            val state = when (newBoard) {
                is BoardDraw -> "Ended D"
                is BoardWin -> "Ended $turn"
                else -> game.state
            }
            val newGame = GameUpdate(game.id, newBoard, game.state)
            success(it.gameRepository.updateGame(newGame, turn.other(), state))
        }
    }
}

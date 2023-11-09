package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.PlayModel
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

    object WrongGame : PlayError()
}

typealias PlayResult = Either<PlayError, GameModel?>

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
                boardSize == null || boardSize != 15 || boardSize != 19 -> failure(CreateLobbyError.InvalidBoardSize)
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

    fun play(gameId: Int?, play: PlayModel?, user: AuthenticatedUser): PlayResult {
        return transactionManager.run {
            if (gameId == null) return@run failure(PlayError.InvalidGameId)
            if (play == null) return@run failure(PlayError.InvalidPosition)
            if (play.row == null) return@run failure(PlayError.InvalidRow)
            if (play.col == null) return@run failure(PlayError.InvalidCol)
            if (play.playerId == null) return@run failure(PlayError.InvalidPlayerId)
            if (it.usersRepository.getUserById(play.playerId) == null) return@run failure(PlayError.NonExistingUser)

            val game = it.gameRepository.getGameById(gameId) ?: return@run failure(PlayError.NonExistingGame)
            val board = game.board as BoardRun

            if (play.row.indexToRow().number == -1) return@run failure(PlayError.InvalidRow)
            if (play.col.indexToColumn().symbol == '?') return@run failure(PlayError.InvalidCol)
            if ((play.row > game.boardSize) || (play.col > game.boardSize)) return@run failure(PlayError.InvalidPosition)

            val position = Position(play.row.indexToRow(), play.col.indexToColumn(), game.boardSize)

            if (game.board.moves[position] != null) return@run failure(PlayError.PositionOccupied)
            if (game.state != "Playing") return@run failure(PlayError.GameEnded)
            if (user.user.id != play.playerId) return@run failure(PlayError.WrongAccount)
            if (play.playerId != game.playerW && play.playerId != game.playerB) return@run failure(PlayError.WrongGame)
            if ((board.turn.string == "W" && play.playerId != game.playerW) || board.turn.string == "B" && play.playerId != game.playerB) {
                return@run failure(PlayError.NotYourTurn)
            }

            val turn =  if (play.swap != null && board.moves.size == 2 && board.variant == "Swap after 1st move") {
                            it.gameRepository.swapPlayers(gameId)
                            board.turn.other()
                        } else board.turn

            val newBoard = game.board.play(position, turn)
            if (newBoard is BoardRun && newBoard.moves == board.moves) return@run failure(PlayError.InvalidPosition)

            if (newBoard == game.board) return@run failure(PlayError.GameEnded)
            val state = when (newBoard) {
                is BoardDraw -> "Ended D"
                is BoardWin -> "Ended ${board.turn}"
                else -> game.state
            }
            it.gameRepository.updateGame(game.id, newBoard, board.turn.other(), state)
            success(it.gameRepository.getGame(game.id))
        }
    }
}

package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.handleDatabaseException
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.PlayModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component

typealias CreateLobbyResult = Either<Error, GameModel?>

typealias GameIdFetchResult = Either<Error, GameModel>

typealias PlayResult = Either<Error, GameModel>

typealias ForfeitResult = Either<Error, Boolean>

@Component
class GamesService(private val transactionManager: TransactionManager) {
    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

    fun getLastGame(username: String?): CreateLobbyResult {
        return transactionManager.run {
            if (username == null) return@run failure(Error.invalidUsername)
            if (it.usersRepository.getUserByUsername(username) == null) return@run failure(Error.nonExistentUsername)
            val game = it.gameRepository.getLastGame(username) ?: return@run failure(Error.notPlaying)
            return@run success(game)
        }
    }

    fun getGameById(id: Int?): GameIdFetchResult {
        return transactionManager.run {
            if (id == null) failure(Error.invalidGameId)
            else {
                val game = it.gameRepository.getGameById(id)
                if (game == null) failure(Error.nonExistentGame)
                else success(game)
            }
        }
    }

    fun createLobby(
        rules: String?,
        variant: String?,
        boardSize: Int?,
        user: AuthenticatedUser?
    ): CreateLobbyResult {
        return transactionManager.run {
            if (user == null) return@run failure(Error.unauthorized)
            val playerId = user.user.id
            when {
                rules == null || !validRules.contains(rules) -> failure(Error.invalidRules)
                variant == null || !validVariants.contains(variant) -> failure(Error.invalidVariant)
                boardSize == null || (boardSize != 15 && boardSize != 19) -> failure(Error.invalidBoardSize)
                else -> {
                    try {
                        it.gameRepository.createLobby(playerId, rules, variant, boardSize)
                    } catch (e: Exception) {
                        return@run handleDatabaseException(e)
                    }
                    val gameId = it.gameRepository.getGameId(playerId)
                    if (gameId != null) {
                        val game = it.gameRepository.getGameById(gameId.toInt())
                        return@run success(game)
                    }
                    success(null)
                }
            }
        }
    }

    fun play(gameId: Int?, play: PlayModel?, user: AuthenticatedUser?): PlayResult {
        return transactionManager.run {
            if (user == null) return@run failure(Error.unauthorized)
            if (gameId == null) return@run failure(Error.invalidGameId)
            if (play == null) return@run failure(Error.invalidPosition)
            if (play.row == null) return@run failure(Error.invalidRow)
            if (play.col == null) return@run failure(Error.invalidCol)

            val game = it.gameRepository.getGameById(gameId) ?: return@run failure(Error.nonExistentGame)
            if (user.user.id != game.playerW && user.user.id != game.playerB) return@run failure(Error.wrongGame)
            if (game.state != "Playing") return@run failure(Error.gameEnded)

            val board = game.board as BoardRun

            if (play.row.indexToRow().number == -1) return@run failure(Error.invalidRow)
            if (play.col.indexToColumn().symbol == '?') return@run failure(Error.invalidCol)
            if ((play.row > game.boardSize) || (play.col > game.boardSize)) return@run failure(Error.invalidPosition)

            val position = Position(play.row.indexToRow(), play.col.indexToColumn(), game.boardSize)

            if ((board.turn.string == "W" && user.user.id != game.playerW) || board.turn.string == "B" && user.user.id != game.playerB) {
                return@run failure(Error.notYourTurn)
            }

            if (game.board.moves[position] != null) return@run failure(Error.positionOccupied)

            val turn = if (play.swap != null && board.moves.size == 1) board.turn.other()
            else board.turn

            val newBoard = game.board.play(position, turn, play.swap)

            if (newBoard is BoardRun && newBoard.moves == board.moves) return@run failure(Error.wrongPlace)

            if (play.swap != null && newBoard.moves.size == 2) {
                it.gameRepository.swapPlayers(gameId)
            }

            val state = when (newBoard) {
                is BoardDraw -> "Ended D"
                is BoardWin -> "Ended ${board.turn}"
                else -> game.state
            }

            val newGame = it.gameRepository.updateGame(game.id, newBoard, state)
            success(newGame)
        }
    }

    fun forfeitGame(gameId: Int?, user: AuthenticatedUser?): ForfeitResult {
        return transactionManager.run {
            if (user == null) return@run failure(Error.unauthorized)
            if (gameId == null) return@run failure(Error.invalidGameId)

            val game = it.gameRepository.getGameById(gameId) ?: return@run failure(Error.nonExistentGame)
            if (user.user.id != game.playerW && user.user.id != game.playerB) return@run failure(Error.wrongGame)
            if (game.state != "Playing") return@run failure(Error.gameEnded)

            val player = if (user.user.id == game.playerW) "W" else "B"

            val forfeited = it.gameRepository.forfeitGame(gameId, player)

            success(forfeited)
        }
    }

    /*fun cancelLobby( lobbyId :Int,user: AuthenticatedUser?): CancelResult {
        return transactionManager.run {
            if (user == null) return@run failure(Error.unauthorized)
            val lobby = it.gameRepository.getLobbyId(user.user.id)
            if (lobbyId != lobby) return@run failure(Error.unauthorized)
            success(it.gameRepository.cancelLobby(user.user.id))
        }
    }*/


}


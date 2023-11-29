package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.model.GameModel
import com.example.demo.http.model.PlayModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component

typealias CreateLobbyResult = Either<Error, Message>

typealias GameIdFetchResult = Either<Error, GameModel>

typealias PlayResult = Either<Error, GameModel>

@Component
class GamesService(private val transactionManager: TransactionManager) {
    private val validRules = listOf("Pro", "Long Pro")
    private val validVariants = listOf("Freestyle", "Swap after 1st move")

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
        playerId: Int?,
        rules: String?,
        variant: String?,
        boardSize: Int?,
        user: AuthenticatedUser
    ): CreateLobbyResult {
        return transactionManager.run {
            when {
                playerId == null || it.usersRepository.getUserById(playerId) == null -> failure(Error.invalidId)
                rules == null || !validRules.contains(rules) -> failure(Error.invalidRules)
                variant == null || !validVariants.contains(variant) -> failure(Error.invalidVariant)
                boardSize == null || (boardSize != 15 && boardSize != 19) -> failure(Error.invalidBoardSize)
                it.usersRepository.getUserByToken(user.token)!!.id != playerId -> failure(Error.wrongAccount)
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

    fun play(gameId: Int?, play: PlayModel?, user: AuthenticatedUser): PlayResult {
        return transactionManager.run {
            if (gameId == null) return@run failure(Error.invalidGameId)
            if (play == null) return@run failure(Error.invalidPosition)
            if (play.row == null) return@run failure(Error.invalidRow)
            if (play.col == null) return@run failure(Error.invalidCol)
            if (play.playerId == null) return@run failure(Error.invalidId)
            if (it.usersRepository.getUserById(play.playerId) == null) return@run failure(Error.nonExistentUserId)

            val game = it.gameRepository.getGameById(gameId) ?: return@run failure(Error.nonExistentGame)
            val board = game.board as BoardRun

            if (play.row.indexToRow().number == -1) return@run failure(Error.invalidRow)
            if (play.col.indexToColumn().symbol == '?') return@run failure(Error.invalidCol)
            if ((play.row > game.boardSize) || (play.col > game.boardSize)) return@run failure(Error.invalidPosition)

            val position = Position(play.row.indexToRow(), play.col.indexToColumn(), game.boardSize)

            if (game.board.moves[position] != null) return@run failure(Error.positionOccupied)
            if (game.state != "Playing") return@run failure(Error.gameEnded)
            if (user.user.id != play.playerId) return@run failure(Error.wrongAccount)
            if (play.playerId != game.playerW && play.playerId != game.playerB) return@run failure(Error.wrongGame)
            if ((board.turn.string == "W" && play.playerId != game.playerW) || board.turn.string == "B" && play.playerId != game.playerB) {
                return@run failure(Error.notYourTurn)
            }

            val turn =  if (play.swap != null && board.moves.size == 2 && board.variant == "Swap after 1st move") {
                            it.gameRepository.swapPlayers(gameId)
                            board.turn.other()
                        } else board.turn

            val newBoard = game.board.play(position, turn)
            if (newBoard is BoardRun && newBoard.moves == board.moves) return@run failure(Error.invalidPosition)

            if (newBoard == game.board) return@run failure(Error.gameEnded)
            val state = when (newBoard) {
                is BoardDraw -> "Ended D"
                is BoardWin -> "Ended ${board.turn}"
                else -> game.state
            }
            val newGame = it.gameRepository.updateGame(game.id, newBoard, state)
            success(newGame)
        }
    }
}

package com.example.demo.domain


import java.util.regex.Pattern
import kotlin.IllegalStateException
import kotlin.math.pow


const val SQUARE_DIM = 14

const val BOARD_DIM = SQUARE_DIM + 1

typealias Moves = Map<Position, Player>

private val MAX_MOVES = (BOARD_DIM + 1).toDouble().pow(2)

val INITIALMAP: Moves get() = mapOf()

sealed class Board(val moves: Moves) {

    /**
     * Função "equals" em que servirá para verificar se o objeto é o mesmo e efetuar a verificação de que se trata do mesmo objeto ou não.
     * @param other que representa o outro objeto com que queremos comparar esse Board com o other.
     * @return Boolean a representar se se trata do mesmo objeto ou não.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Board) return false
        if (this::class != other::class) return false
        return moves.size == other.moves.size
    }

    fun play(position: Position, player: Player): Board {
        when (this) {
            is BoardRun -> {
                require(player == turn) { "Not your turn" }
                require(position != Position.INVALID) { "Invalid position" }
                require(moves[position] == null) { "Position already occupied" }

                return isOver(this, position, moves + (position to player))
            }

            is BoardDraw, is BoardWin -> throw IllegalStateException()
        }

    }


    //TODO FALTA O CASO DE SER EFETUADA A ÚLTIMA JOGADA PARA EMPATE!
    private fun isOver(board: BoardRun, position: Position, newMoves: Moves): Board {
        if (newMoves.size.toDouble() == MAX_MOVES) return BoardDraw(newMoves)
        Direction.values().forEach { dir ->
            //Ver as peças numa certa direção
            if (cellsInDirection(moves, board.turn, position, dir) >= 5) return BoardWin(newMoves, board.turn)
            //Ver se estão 5 peças em linha da cor que se quer
        }
        return BoardRun(newMoves, board.turn.other())
    }


    //Função "hashCode" que será igual ao valor do hashcode de moves.
    override fun hashCode(): Int = moves.hashCode()

    override fun toString() = moves.toString()

}

/**
 * Classe "BoardRun" que representa o tabuleiro de jogo quando este está a ser jogado retornando o objeto do tipo "Board".
 * @property moves representa os moves desse board.
 * @property turn representa o turno do jogador que é a jogar ou não.
 * @return Board representa o Board que representa o nosso BoardRun.
 */
class BoardRun(moves: Moves, val turn: Player) : Board(moves) {

}

/**
 * Classe "BoardWin" que representa o tabuleiro de jogo quando existe um vencedor desse jogo.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @property winner representa o vencedor desse jogo.
 * @return Board representa o Board quando este acabou com um vencedor.
 */
class BoardWin(moves: Moves, val winner: Player) : Board(moves) {

}

/**
 * Classe "BoardDraw" que representa o tabuleiro de jogo quando este empatou.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @return Board representa o board quando este acabou em empate.
 */
class BoardDraw(moves: Moves) : Board(moves) {

}

fun fromString(boardString: String): Board {
    val boardMap = mutableMapOf<Position, Player>()

    val newBoardString = boardString.substring(1, boardString.length - 1)
    val pairs = newBoardString.split(",")
    for (i in pairs) {
        val player = i.substring(i.length - 5).toPlayer()
        val position = i.substring(0, i.length - 6).toPosition()
        boardMap[position] = player
    }

    return BoardRun(boardMap, Player.WHITE)
}

/**
 * Função "createBoard" responsável por criar o novo Board com os dados iniciais.
 * @param first representa o jogador que o utilizador irá ser nesse board.
 * @return BoardRun representa o nosso tabuleiro durante um jogo.
 */
fun createBoard(first: Player) = BoardRun(INITIALMAP, first)

fun main() {
    val board = BoardRun(INITIALMAP, Player.WHITE)
    val b = board.play(Position(1.indexToRow(), 1.indexToColumn()), Player.WHITE)
    println(b)
    //println(fromString("{2B:WHITE,4A:BLACK}").moves)
}


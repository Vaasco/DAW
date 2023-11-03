package com.example.demo.domain


import com.example.demo.service.PlayError
import kotlin.math.abs
import kotlin.math.pow

const val SQUARE_DIM = 14

const val BOARD_DIM = SQUARE_DIM + 1

typealias Moves = Map<Position, Player>

private val MAX_MOVES = (BOARD_DIM + 1).toDouble().pow(2)

val INITIAL_MAP: Moves get() = mapOf()

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
        return when (this) {
            is BoardRun -> {
                /*require(player == turn) { "Not your turn" }
                require(position != Position.INVALID) { "Invalid position" }
                require(moves[position] == null) { "Position already occupied" }*/
            }

            is BoardDraw, is BoardWin -> this
        }
    }

    private fun isOver(position: Position, newMoves: Moves): Board {
        if (newMoves.size.toDouble() == MAX_MOVES) return BoardDraw(newMoves, this.size, this.rules, this.variant)
        val board = this as BoardRun
        Direction.values().forEach { dir ->
            //Ver as peças numa certa direção
            if (cellsInDirection(moves, this.turn, position, dir) >= 5)
                return BoardWin(newMoves, this.size, this.rules, this.variant, board.turn)
            //Ver se estão 5 peças em linha da cor que se quer
        }
        return BoardRun(newMoves, this.size, this.rules, this.variant, board.turn.other())
    }

    //Função "hashCode" que será igual ao valor do hashcode de moves.
    override fun hashCode(): Int = moves.hashCode()

    override fun toString() = when (this) {
        is BoardRun -> turn.string + size.toString() + rules + variant + '\n'+ moves.toString()
        is BoardWin -> winner.string + size.toString() + rules + variant + '\n' + moves.toString()
        is BoardDraw -> moves.toString()
    }
}

/**
 * Classe "BoardRun" que representa o tabuleiro de jogo quando este está a ser jogado retornando o objeto do tipo "Board".
 * @property moves representa os moves desse board.
 * @property turn representa o turno do jogador que é a jogar ou não.
 * @return Board representa o Board que representa o nosso BoardRun.
 */
class BoardRun(moves: Moves, val turn: Player) : Board(moves)

/**
 * Classe "BoardWin" que representa o tabuleiro de jogo quando existe um vencedor desse jogo.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @property winner representa o vencedor desse jogo.
 * @return Board representa o Board quando este acabou com um vencedor.
 */
class BoardWin(moves: Moves, val winner: Player) : Board(moves)

/**
 * Classe "BoardDraw" que representa o tabuleiro de jogo quando este empatou.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @return Board representa o board quando este acabou em empate.
 */
class BoardDraw(moves: Moves) : Board(moves)

fun fromString(boardString: String): Board {
    val turn = boardString[0].toString().toPlayer()
    val size = boardString.substring(1, 3).toInt()
    val regex = Regex("(Pro|Long Pro)")
    val regex2 = Regex("Freestyle|Swap after 1st move")
    val rules = regex.find(boardString)!!.value
    val variant = regex2.find(boardString)!!.value
    if (boardString == "$turn$size$rules$variant\n{}") return BoardRun(emptyMap(), size, rules, variant, turn)
    val idx = boardString.indexOf('\n')
    val board = boardString.substring(idx + 2, boardString.length - 1)
    val boardMap = mutableMapOf<Position, Player>()
    val pairs = board.split(", ")
    for (i in pairs) {
        val str = i.last()
        val player = str.toString().toPlayer()
        val position = i.substring(0, 2).toPosition()
        boardMap[position] = player
    }
    return BoardRun(boardMap, size, rules, variant, turn)
}

/**
 * Função "createBoard" responsável por criar o novo Board com os dados iniciais.
 * @param first representa o jogador que o utilizador irá ser nesse board.
 * @return BoardRun representa o nosso tabuleiro durante um jogo.
 */
fun createBoard(first: Player) = BoardRun(INITIALMAP, first)

fun getCentralPosition(boardSize: Int): Position {
    val row = (boardSize / 2).indexToRow()
    val col = (boardSize / 2).indexToColumn()
    return Position(row, col)
}

fun isPositionsAway(piece1: Position, piece2: Position,distance:Int): Boolean {
    val rowDifference = Math.abs(piece1.row.index - piece2.row.index)
    val colDifference = Math.abs(piece1.col.index - piece2.col.index)

    return rowDifference > distance || colDifference > distance
}

fun main() {
    /*val board = BoardRun(INITIALMAP, Player.W)
    val b = board.play(Position(1.indexToRow(), 1.indexToColumn()), Player.W)
    println(b)*/
    println(isPositionsAway(Position(7.indexToRow(),7.indexToColumn()),Position(9.indexToRow(),9.indexToColumn()),3))
}

package com.example.demo.domain

import kotlin.math.abs
import kotlin.math.pow

enum class BoardSize(val size: Int) {
    SMALL(15),
    BIG(19)
}

typealias Moves = Map<Position, Player>

fun whichSize(size: Int) = if (size == 15) BoardSize.SMALL else BoardSize.BIG

fun maxMoves(size: Int): Double {
    val boardSize = whichSize(size)
    return (boardSize.size + 1).toDouble().pow(2)
}

val INITIAL_MAP: Moves get() = mapOf()

sealed class Board(val moves: Moves, private val size: Int, val rules: String, val variant: String) {

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

    fun play(position: Position, player: Player, swap: Int? = null): Board {
        return when (this) {
            is BoardRun -> {
                if (rules != "Default") {
                    val distance = if (rules == "Pro") 3 else 4
                    val centralPiece = getCentralPosition(size)
                    when (this.moves.size) {
                        0 -> if (centralPiece != position) return this
                        1 -> if (this.variant == "Swap after 1st move" && !isPositionsAway(centralPiece, position, distance)) return this
                        2 -> if (this.variant != "Swap after 1st move" && !isPositionsAway(centralPiece, position, distance)) return this
                    }
                }
                isOver(position, moves + (position to player), turn, size, swap)
            }
            is BoardDraw, is BoardWin -> this
        }
    }

    private fun isOver(position: Position, newMoves: Moves, turn: Player, boardSize: Int, swap: Int?): Board {
        if (newMoves.size.toDouble() == maxMoves(boardSize)) return BoardDraw(newMoves, size, rules, variant)
        val dirs = arrayOf(Direction.DOWN, Direction.DOWN_LEFT, Direction.DOWN_RIGHT, Direction.LEFT)
        dirs.forEach { dir ->
            if (cellsInDirection(moves, turn, position, dir) >= 5)
                return BoardWin(newMoves, size, rules, variant, turn)
        }
        return if (swap != null) BoardRun(newMoves, size, rules, variant, turn)
        else BoardRun(newMoves, size, rules, variant, turn.other())
    }

    override fun hashCode(): Int = moves.hashCode()

    override fun toString(): String {
        return when (this) {
            is BoardRun -> this.turn.toString() + size.toString() + rules + variant + '\n' + moves.toString()
            is BoardWin -> winner.string + size.toString() + rules + variant + '\n' + moves.toString()
            is BoardDraw -> moves.toString()
        }
    }
}

/**
 * Classe "BoardRun" que representa o tabuleiro de jogo quando este está a ser jogado retornando o objeto do tipo "Board".
 * @property moves representa os moves desse board.
 * @property turn representa o turno do jogador que é a jogar ou não.
 * @return Board representa o Board que representa o nosso BoardRun.
 */
class BoardRun(moves: Moves, size: Int, rules: String, variant: String, val turn: Player) :
    Board(moves, size, rules, variant)

/**
 * Classe "BoardWin" que representa o tabuleiro de jogo quando existe um vencedor desse jogo.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @property winner representa o vencedor desse jogo.
 * @return Board representa o Board quando este acabou com um vencedor.
 */
class BoardWin(moves: Moves, size: Int, rules: String, variant: String, val winner: Player) :
    Board(moves, size, rules, variant)

/**
 * Classe "BoardDraw" que representa o tabuleiro de jogo quando este empatou.
 * @property moves representa os movimentos efetuados nesse tabuleiro.
 * @return Board representa o board quando este acabou em empate.
 */
class BoardDraw(moves: Moves, size: Int, rules: String, variant: String) : Board(moves, size, rules, variant)

fun fromString(boardString: String): Board {
    val turn = boardString[0].toString().toPlayer()
    val size = boardString.substring(1, 3).toInt()

    Position.Factory(size).createPositions()

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
        val maxLength = if(i.substring(0, 2).toIntOrNull() == null) 2 else 3
        val position = i.substring(0, maxLength).toPosition(size)
        boardMap[position] = player
    }
    return BoardRun(boardMap, size, rules, variant, turn)
}

/**
 * Função "createBoard" responsável por criar o novo Board com os dados iniciais.
 * @param first representa o jogador que o utilizador irá ser nesse board.
 * @return BoardRun representa o nosso tabuleiro durante um jogo.
 */
fun createBoard(first: Player, size: Int, rules: String, variant: String) =
    BoardRun(INITIAL_MAP, size, rules, variant, first)

fun getCentralPosition(boardSize: Int): Position {
    val row = (boardSize / 2).indexToRow()
    val col = (boardSize / 2).indexToColumn()
    return Position(row, col, boardSize)
}

fun isPositionsAway(piece1: Position, piece2: Position, distance: Int): Boolean {
    val rowDifference = abs(piece1.row.index - piece2.row.index)
    val colDifference = abs(piece1.col.index - piece2.col.index)

    return rowDifference >= distance || colDifference >= distance
}
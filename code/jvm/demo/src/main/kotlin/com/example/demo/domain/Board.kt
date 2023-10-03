package com.example.demo.domain

const val BOARD_DIM = 5

data class Board(val positions: Map<Cell, Player?>, val turn: Player?) {
    companion object {
        val emptyBoard = Board(emptyMap(), null)
    }
}


fun Board.add(cell: Cell, player: Player): Board {
    require(cell != Cell.INVALID) { "This position doesn't exist" }
    return copy(positions = positions + (cell to player), turn = turn?.other())
}



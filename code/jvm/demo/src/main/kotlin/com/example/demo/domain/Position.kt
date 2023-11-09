package com.example.demo.domain

class Position private constructor(val row: Row, val col: Column) {

    override fun toString(): String = if (this == INVALID) "Invalid Cell" else "${this.row.number}${this.col.symbol}"

    companion object {
        lateinit var values: List<Position>
        lateinit var INVALID: Position

        operator fun invoke(rowIndex: Int, colIndex: Int, boardSize: Int): Position {
            return if (rowIndex in 0 until boardSize && colIndex in 0 until boardSize) {
                values[rowIndex * boardSize + colIndex]
            } else INVALID
        }
        operator fun invoke(row: Row, col: Column, boardSize: Int): Position = Position(row.index, col.index, boardSize)
    }

    class Factory(private val boardDim: Int) {
        fun createPositions() {
            val rows = Row.Factory(boardDim).createRows()
            val cols = Column.Factory(boardDim).createColumns()
            values = List(boardDim * boardDim) { Position(rows[it / boardDim], cols[it % boardDim]) }
            INVALID = Position(Row.Factory(boardDim).createRows().last(), Column.Factory(boardDim).createColumns().last())
        }
    }
}


fun String.toPosition(boardSize: Int): Position {
    val str = this.trim()
    return if (str.length == 2) {
        val row = str[0].digitToInt()
        val col = str[1]
        Position(row.toRow(), col.toColumn(), boardSize)
    }
    else{
        val row = str.substring(0,2).toInt()
        val col = str[2]
        Position(row.toRow(), col.toColumn(), boardSize)
    }
}

operator fun Position.plus(dir: Direction): Position = Position(row.index + dir.difRow, col.index + dir.difCol, row.boardSize)


fun cellsInDirection(moves: Moves, player: Player, from: Position, dir: Direction): Int {
    var counter = 1
    var currentCell = from + dir
    var currDir = dir
    while (currentCell != Position.INVALID && moves[currentCell] == player) {
        counter++
        currentCell += currDir
    }
    currDir = dir.reverse()
    currentCell = from + currDir
    while (currentCell != Position.INVALID && moves[currentCell] == player) {
        counter++
        currentCell += currDir
    }
    return counter
}
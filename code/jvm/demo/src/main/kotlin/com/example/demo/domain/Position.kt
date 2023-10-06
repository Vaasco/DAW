package com.example.demo.domain



class Position private constructor(val row: Row, val col: Column) {

    val rowIndex: Int = row.index
    val colIndex: Int = col.index

    override fun toString(): String = if (this== INVALID) "INVALID Cell" else "${this.row.number}${this.col.symbol}"

    companion object {
        private val values =
            List(BOARD_DIM * BOARD_DIM) { Position((it / BOARD_DIM).indexToRow(), (it % BOARD_DIM).indexToColumn()) }
        val INVALID = Position(-1, 1)

        operator fun invoke(rowIndex: Int, colIndex: Int): Position {
            return if (rowIndex in 0 until BOARD_DIM && colIndex in 0 until BOARD_DIM) {
                values[rowIndex * BOARD_DIM + colIndex]
            } else INVALID
        }

        operator fun invoke(row: Row, col: Column): Position = Position(row.index, col.index)
    }

}

operator fun Position.plus(dir: Direction): Position = Position(row.index + dir.difRow, col.index + dir.difCol)


fun cellsInDirection(moves: Moves, player: Player, from: Position, dir: Direction): Int {
    var counter = 1
    var currentCell = from + dir
    var currDir = dir
    while (currentCell != Position.INVALID && moves[currentCell] == player) {
        counter++
        currentCell += currDir
    }
    currDir = dir.invertDirection()
    currentCell = from + currDir
    while (currentCell != Position.INVALID && moves[currentCell] == player) {
        counter++
        currentCell += currDir
    }
    return counter
}
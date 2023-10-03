package com.example.demo.domain



class Cell private constructor(val row: Row, val col: Column) {

    val rowIndex: Int = row.index
    val colIndex: Int = col.index

    override fun toString(): String = if (this== INVALID) "INVALID Cell" else "${this.row.number}${this.col.symbol}"

    companion object {
        private val values =
            List(BOARD_DIM * BOARD_DIM) { Cell((it / BOARD_DIM).indexToRow(), (it % BOARD_DIM).indexToColumn()) }
        val INVALID = Cell(-1, 1)

        operator fun invoke(rowIndex: Int, colIndex: Int): Cell {
            return if (rowIndex in 0 until BOARD_DIM && colIndex in 0 until BOARD_DIM) {
                values[rowIndex * BOARD_DIM + colIndex]
            } else INVALID
        }

        operator fun invoke(row: Row, col: Column): Cell = Cell(row.index, col.index)
    }

}

operator fun Cell.plus(dir: Direction): Cell = Cell(row.index + dir.difRow, col.index + dir.difCol)


fun cellsInDirection(from: Cell, dir: Direction): List<Cell> {
    val line = mutableListOf<Cell>()
    var currentCell = from + dir
    while (currentCell != Cell.INVALID) {
        line.add(currentCell)
        currentCell += dir
    }
    return line
}
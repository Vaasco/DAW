package com.example.demo.domain

class Row private constructor(val number: Int, val boardSize: Int) {
    val index get() = number - 1

    companion object {
        lateinit var values: List<Row>

        operator fun invoke(number: Int) = values.first { it.number == number }
    }

    override fun toString() = "Row $number"

    class Factory(private val boardSize: Int) {
        fun createRows(): List<Row> {
            values = List(boardSize) { Row(it + 1, boardSize) } + Row(-1, boardSize)
            return values
        }
    }
}

fun Int.toRowOrNull() = Row.values.find { it.number == this }
fun Int.toRow() = this.toRowOrNull() ?: Row(-1) //throw IllegalArgumentException("Invalid row $this")

fun Int.indexToRowOrNull(): Row? = Row.values.find { this == it.index }
fun Int.indexToRow(): Row = this.indexToRowOrNull() ?: Row (-1) //throw IllegalArgumentException("Invalid row $this")
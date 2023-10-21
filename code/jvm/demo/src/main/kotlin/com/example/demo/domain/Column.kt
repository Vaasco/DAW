package com.example.demo.domain

class Column private constructor(val symbol: Char) {
    val index get() = values.indexOf(this)

    companion object {
        val values = List(BOARD_DIM) { Column('A' + it) } + Column('?')

        operator fun invoke(symbol: Char) = values.first { it.symbol == symbol }
    }

    override fun toString() = "Column $symbol"
}

fun Char.toColumnOrNull() = Column.values.find { it.symbol == this }
fun Char.toColumn() = this.toColumnOrNull() ?: Column('?') //throw IllegalArgumentException("Invalid column $this")

fun Int.indexToColumnOrNull(): Column? = Column.values.find { this == it.index }
fun Int.indexToColumn(): Column = this.indexToColumnOrNull() ?: Column('?')// throw IllegalArgumentException("Invalid column ?")
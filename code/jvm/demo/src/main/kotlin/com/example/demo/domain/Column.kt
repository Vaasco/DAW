package com.example.demo.domain

class Column private constructor(val symbol: Char, val boardSize: Int) {
    val index get() = symbol - 'A' //values.indexOf(this)

    companion object {
        lateinit var values: List<Column>

        operator fun invoke(symbol: Char) = values.first { it.symbol == symbol }
    }

    override fun toString() = "Column $symbol"

    class Factory(private val boardSize: Int){
        fun createColumns(): List<Column>  {
            values = List(boardSize) { Column('A' + it, boardSize) } + Column('?', boardSize)
            return values
        }
    }
}


fun Char.toColumnOrNull() = Column.values.find { it.symbol == this }
fun Char.toColumn() = this.toColumnOrNull() ?: Column('?') //throw IllegalArgumentException("Invalid column $this")

fun Int.indexToColumnOrNull(): Column? = Column.values.find { this == it.index }
fun Int.indexToColumn(): Column = this.indexToColumnOrNull() ?: Column('?')// throw IllegalArgumentException("Invalid column ?")
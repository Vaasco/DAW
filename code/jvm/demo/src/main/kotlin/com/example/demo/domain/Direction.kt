package com.example.demo.domain

enum class Direction(val difRow: Int, val difCol: Int) {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1),
    UP_LEFT(-1, -1), UP_RIGHT(-1, 1), DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    fun reverse() = when(this) {
        UP -> DOWN
        DOWN -> UP
        LEFT -> RIGHT
        RIGHT -> LEFT
        UP_LEFT -> DOWN_RIGHT
        UP_RIGHT -> DOWN_LEFT
        DOWN_LEFT -> UP_RIGHT
        DOWN_RIGHT -> UP_LEFT
    }
}

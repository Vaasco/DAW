package com.example.demo.domain

enum class Player(val string: String) {
    WHITE("W"),
    BLACK("B");

    fun other() = if (this == WHITE) BLACK else WHITE
}

fun String.toPlayer(): Player {
    return if (this == "WHITE") Player.WHITE
    else Player.BLACK
}
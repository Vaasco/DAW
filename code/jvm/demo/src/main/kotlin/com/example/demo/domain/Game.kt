package com.example.demo.domain

/*data class Game(
    val id: UUID,
    val board: Board,
    val playerWhite: Int,
    val playerBlack: Int
)*/




data class Game(
    val id: Int,
    val board: Board,
    val state: String,
    val rules: String,
    val variant: String
    //val playerWhite: Int,
    //val playerBlack: Int
)
package com.example.demo.domain

data class Game( //TODO("Maybe delete")
    val id: Int,
    val board: Board,
    val state: String,
    val rules: String,
    val variant: String
    //val playerWhite: Int,
    //val playerBlack: Int
)
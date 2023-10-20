package com.example.demo.http.model

data class GameModel(
    val id: Int,
    val board: String,
    val turn: String,
    val state: String,
    val playerB: Int,
    val playerW: Int,
    val rules: String,
    val variant: String,
    val boardSize: Int
)
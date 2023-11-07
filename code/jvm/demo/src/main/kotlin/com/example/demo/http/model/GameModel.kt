package com.example.demo.http.model

import com.example.demo.domain.Board

data class GameModel(
    val id: Int,
    val board: Board,
    val state: String,
    val playerB: Int,
    val playerW: Int,
    val boardSize: Int
)
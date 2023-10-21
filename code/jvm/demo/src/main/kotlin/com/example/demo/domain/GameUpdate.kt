package com.example.demo.domain

data class GameUpdate(
    val id: Int,
    val board: Board,
    val state: String,
)
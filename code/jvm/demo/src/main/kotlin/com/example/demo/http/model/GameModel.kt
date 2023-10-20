package com.example.demo.http.model

import com.example.demo.domain.Board

data class GameModel(
    val id:Int,
    val board: String,
    val turn: String,
    val state: String,
    val player_b:Int,
    val player_w:Int,
    val rules:String,
    val variant:String,
    val board_size:Int
)
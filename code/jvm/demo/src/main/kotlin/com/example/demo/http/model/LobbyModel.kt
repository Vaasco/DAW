package com.example.demo.http.model

data class LobbyModel(
    val playerId : Int,
    val rules : String,
    val variant: String,
    val boardSize : Int
)
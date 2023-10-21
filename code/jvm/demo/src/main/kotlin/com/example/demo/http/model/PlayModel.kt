package com.example.demo.http.model

import com.example.demo.domain.AuthenticatedUser

data class PlayModel(
    val playerId: AuthenticatedUser,
    val row:Int,
    val col: Int
)
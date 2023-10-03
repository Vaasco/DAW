package com.example.demo.domain

import java.util.UUID

data class Game(
    val id:UUID,
    val board:Board,
    val playerWhite:Int,
    val playerBlack:Int
)
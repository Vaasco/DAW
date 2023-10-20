package com.example.demo.http.model

data class StatisticsModel(
    val rank: Int,
    val playedGames: Int,
    val wonGames: Int,
    val lostGames: Int
)
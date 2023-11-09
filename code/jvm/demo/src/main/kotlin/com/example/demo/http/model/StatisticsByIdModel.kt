package com.example.demo.http.model

data class StatisticsByIdModel(
    val username: String,
    val rank: Int,
    val playedGames: Int,
    val wonGames: Int,
    val lostGames: Int
)

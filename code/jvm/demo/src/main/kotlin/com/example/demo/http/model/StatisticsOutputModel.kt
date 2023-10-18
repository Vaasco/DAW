package com.example.demo.http.model

data class StatisticsOutputModel(
    val rank: Int,
    val playedGames: Int,
    val wonGames: Int,
    val lostGames: Int
)
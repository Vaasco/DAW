package com.example.demo.repository

import com.example.demo.http.model.StatisticsOutputModel

interface UsersRepository {
    fun getById(id : Int) : String?

    fun createUser(username : String, password: String)

    fun getGameState(id : Int):String?

    fun getStatisticsById(id : Int):StatisticsOutputModel?
}
package com.example.demo.repository

import com.example.demo.http.model.StatisticsOutputModel
import com.example.demo.http.model.UserOutputModel

interface UsersRepository {
    fun getUserById(id : Int) : UserOutputModel?

    fun createUser(username : String, password: String)

    fun getStatisticsById(id : Int):StatisticsOutputModel?

    fun getGamesCount(id : Int): Int

    fun getUserByUsername(username : String): Int?

    fun getUserPassword(username: String): String
}
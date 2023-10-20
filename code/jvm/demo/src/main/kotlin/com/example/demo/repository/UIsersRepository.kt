package com.example.demo.repository

import com.example.demo.http.model.StatisticsOutputModel
import com.example.demo.http.model.UserModel
import com.example.demo.domain.Authentication
import java.sql.Date


interface UsersRepository {
    fun getUserById(id : Int) : UserModel?

    fun createUser(username : String, password: String)

    fun getStatisticsById(id : Int):StatisticsOutputModel?

    fun getGamesCount(id : Int): Int

    fun getUserByUsername(username : String): UserModel?

    fun getUserPassword(username: String): String

    fun createToken(token : Authentication)

    fun getCurrDate(): Date

    fun getToken(token: String): String?
}
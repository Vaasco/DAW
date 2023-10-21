package com.example.demo.repository

import com.example.demo.domain.Authentication
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserTemp
import java.sql.Date


interface UsersRepository {
    fun getUserById(id : Int) : UserModel?

    fun createUser(username : String, password: String): Int

    fun getStatisticsById(id : Int):StatisticsModel

    fun getGamesCount(id : Int): Int

    fun getUserByUsername(username : String): UserModel?

    fun getUserPassword(username: String): String

    fun createAuthentication(token: Authentication)

    fun getCurrDate(): Date

    fun getUserByToken(token: String): UserTemp?
}
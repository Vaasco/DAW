package com.example.demo.repository

import com.example.demo.domain.Authentication
import com.example.demo.domain.Token
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel
import java.sql.Date


interface UsersRepository {
    fun getUserById(id : Int) : UserOutputModel?

    fun createUser(username : String, password: String): Int

    fun getStatisticsById(id : Int):StatisticsModel

    fun getGamesCount(id : Int): Int

    fun getUserByUsername(username : String): UserOutputModel?

    fun getUserPassword(username: String): String

    fun createAuthentication(token: Authentication)

    fun getCurrDate(): Date

    fun getUserByToken(token: String): UserModel?

    fun getUserToken(username: String): Token
}
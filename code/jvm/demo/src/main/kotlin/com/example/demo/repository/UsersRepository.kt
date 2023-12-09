package com.example.demo.repository

import com.example.demo.domain.Authentication
import com.example.demo.domain.Token
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel
import java.sql.Date


interface UsersRepository {
    fun getUserById(id : Int) : UserOutputModel?

    fun createUser(username : String, password: String): Int

    fun getStatisticsByUsername(username: String): List<StatisticsByIdModel>

    fun getStatistics(): List<StatisticsModel>

    fun getGamesCount(id : Int): Int

    fun getUserByUsername(username: String): UserOutputModel?

    fun getUsersByUsername(username : String): List<UserOutputModel>?

    fun getUserPassword(username: String): String

    fun createAuthentication(token: Authentication)

    fun deleteAuthentication(username: String?): Boolean

    fun getCurrDate(): Date

    fun getUserByToken(token: String): UserModel?

    fun getUserToken(username: String): Token

    fun getUserTokens(): List<Token>
}
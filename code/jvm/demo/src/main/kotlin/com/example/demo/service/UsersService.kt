package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.http.errors.*
import com.example.demo.http.errors.UserIdFetchError
import com.example.demo.http.model.UserModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component
import java.time.Instant


private const val authors = "Vasco Branco - 48259\nJosé Borges - 48269\nSérgio Capela - 48080"

@Component
class UsersService(
    private val userDomain: UserDomain,
    private val transactionManager: TransactionManager
) {
    fun getUserById(id: Int?): UserIdFetchResult {
        return transactionManager.run {
            if(id == null){
                failure(UserIdFetchError.InvalidId)
            }
            else {
                val user = it.usersRepository.getUserById(id)
                if( user == null ){
                    failure(UserIdFetchError.NonExistingUser)
                }
                else success(user)
            }
        }
    }

    fun createUser(username: String?, password: String?): UserCreationResult {
        return transactionManager.run {
            when{
                username.isNullOrEmpty() -> failure(UserCreationError.InvalidUsername)
                password.isNullOrEmpty() -> failure(UserCreationError.InvalidPassword)
                getUserByUsername(username) is Success -> failure(UserCreationError.RepeatedUsername)
                else -> {
                    val userId = it.usersRepository.createUser(username, password)
                    createToken(userId)
                    success(userId)
                }
            }
        }
    }

    fun getStatisticsById(id: Int?): StatisticsFetchResult {
        return transactionManager.run {
            if (id == null) failure(StatisticsError.InvalidId)
            else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) failure(StatisticsError.InvalidId)//TODO("Change Statistics")
                else success(it.usersRepository.getStatisticsById(id))
            }
        }
    }

    fun getGamesCount(id: Int?): GamesCountFetchResult {
        return transactionManager.run {
            if (id == null) {
                failure(GamesCountError.InvalidId)
            } else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) failure(GamesCountError.InvalidId) //TODO("Change GamesCount")
                success(it.usersRepository.getGamesCount(id))
            }

        }
    }

    fun getUserByUsername(username: String?): UsernameFetchResult {
        return transactionManager.run {
            if (username == null) {
                failure(UsernameFetchError.InvalidUsername)
            } else {
                val user = it.usersRepository.getUserByUsername(username)
                if (user != null) success(user)
                else failure(UsernameFetchError.NonExistingUser)
            }
        }
    }

    fun getUserPassword(username: String?): String {
        return transactionManager.run {
            require(username != null) { "Invalid username" }
            require(it.usersRepository.getUserByUsername(username) != null) { "There's no user with the given username" }
            it.usersRepository.getUserPassword(username)
        }
    }

    fun getAuthors() = transactionManager.run {
        authors
    }

    fun createToken(id: Int) {
        return transactionManager.run {
            val token = Authentication(userDomain.generateTokenValue(), id, Instant.now(), Instant.now())
            it.usersRepository.createAuthentication(token)
        }
    }

    fun getUserByToken(token: String?): UserModel? {
        return transactionManager.run {
            require(token != null) { "Invalid token" }
            it.usersRepository.getUserByToken(token)
        }
    }
}


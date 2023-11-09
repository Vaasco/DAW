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
            if (id == null) {
                failure(UserIdFetchError.InvalidId)
            } else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) {
                    failure(UserIdFetchError.NonExistingUserId)
                } else success(user)
            }
        }
    }

    fun createUser(username: String?, password: String?): UserCreationResult {
        return transactionManager.run {
            when {
                username.isNullOrEmpty() -> failure(UserCreationError.InvalidUsername)
                password.isNullOrEmpty() -> failure(UserCreationError.InvalidPassword)
                getUserByUsername(username) is Success -> failure(UserCreationError.RepeatedUsername)
                else -> {
                    val userId = it.usersRepository.createUser(username, password)
                    val token = createToken(userId)
                    success(token)
                }
            }
        }
    }

    fun logIn(username: String?, password: String?): UserLoginResult {
        return transactionManager.run {
            when {
                username.isNullOrEmpty() -> failure(UserLoginError.InvalidUsername)
                password.isNullOrEmpty() -> failure(UserLoginError.InvalidPassword)
                getUserByUsername(username) is Failure -> failure(UserLoginError.NonExistentUsername)
                it.usersRepository.getUserPassword(username) != password -> failure(UserLoginError.WrongPassword)
                else -> {
                    val token = it.usersRepository.getUserToken(username)
                    success(token)
                }
            }
        }
    }

    fun getStatisticsById(id: Int?): StatisticsFetchResult {
        return transactionManager.run {
            if (id == null) failure(StatisticsError.InvalidId)
            else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) failure(StatisticsError.InvalidId)
                else success(it.usersRepository.getStatisticsById(id))
            }
        }
    }

    fun getStatistics(): StatisticsFetchResult {
        return transactionManager.run {
            success(it.usersRepository.getStatistics())
        }
    }

    fun getUserByUsername(username: String?): UsernameFetchResult {
        return transactionManager.run {
            if (username == null) {
                failure(UsernameFetchError.InvalidUsername)
            } else {
                val user = it.usersRepository.getUserByUsername(username)
                if (user != null) success(user)
                else failure(UsernameFetchError.NonExistingUsername)
            }
        }
    }

    fun getAuthors() = transactionManager.run {
        authors
    }

    fun createToken(id: Int): Token {
        return transactionManager.run {
            val authentication = Authentication(userDomain.generateTokenValue(), id, Instant.now(), Instant.now())
            it.usersRepository.createAuthentication(authentication)
            Token(authentication.token)
        }
    }

    fun getUserByToken(token: String): UserModel? {
        return transactionManager.run {
            it.usersRepository.getUserByToken(token)
        }
    }
}


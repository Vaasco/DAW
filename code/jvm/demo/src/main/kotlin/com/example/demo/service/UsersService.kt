package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.domain.Error
import com.example.demo.http.model.AuthorsModel
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component
import java.time.Instant

typealias UserIdFetchResult = Either<Error, UserOutputModel>

typealias UserCreationResult = Either<Error, Token>

typealias UserLoginResult = Either<Error, Token>

typealias UsernameFetchResult = Either<Error, UserOutputModel>

typealias StatisticsByIdFetchResult = Either<Error, StatisticsByIdModel>

typealias StatisticsFetchResult = Either<Error, List<StatisticsModel>>


private val authors = listOf(
    AuthorsModel("Vasco Branco", "48259"),
    AuthorsModel("José Borges", "48269"),
    AuthorsModel("Sérgio Capela", "46080")
)

@Component
class UsersService(
    private val userDomain: UserDomain,
    private val transactionManager: TransactionManager
) {
    fun getUserById(id: Int?): UserIdFetchResult {
        return transactionManager.run {
            if (id == null) {
                failure(Error.invalidId)
            } else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) {
                    failure(Error.nonExistentUserId)
                } else success(user)
            }
        }
    }

    fun createUser(username: String?, password: String?): UserCreationResult {
        return transactionManager.run {
            when {
                username.isNullOrEmpty() -> failure(Error.invalidUsername)
                password.isNullOrEmpty() -> failure(Error.invalidPassword)
                getUserByUsername(username) is Success -> failure(Error.repeatedUsername)
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
                username.isNullOrEmpty() -> failure(Error.invalidUsername)
                password.isNullOrEmpty() -> failure(Error.invalidPassword)
                getUserByUsername(username) is Failure -> failure(Error.nonExistentUsername)
                it.usersRepository.getUserPassword(username) != password -> failure(Error.wrongPassword)
                else -> {
                    val token = it.usersRepository.getUserToken(username)
                    success(token)
                }
            }
        }
    }

    fun getStatisticsByUsername(username: String?): StatisticsByIdFetchResult {
        return transactionManager.run {
            if (username == null) failure(Error.invalidId)
            else {
                val user = it.usersRepository.getUserByUsername(username)
                if (user == null) failure(Error.nonExistentUserId)
                else success(it.usersRepository.getStatisticsByUsername(username))
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
                failure(Error.invalidUsername)
            } else {
                val user = it.usersRepository.getUserByUsername(username)
                if (user != null) success(user)
                else failure(Error.nonExistentUsername)
            }
        }
    }

    fun getAuthors(): Either<Error, List<AuthorsModel>> {
        return transactionManager.run {
            success(authors)
        }
    }

    fun createToken(id: Int): Token {
        return transactionManager.run {
            var token = userDomain.generateTokenValue()
            while (it.usersRepository.getUserTokens().any { auth -> auth.token == token }) {
                token = userDomain.generateTokenValue()
            }
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


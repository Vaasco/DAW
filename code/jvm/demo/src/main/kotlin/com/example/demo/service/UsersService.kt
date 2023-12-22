package com.example.demo.service

import com.example.demo.domain.*
import com.example.demo.domain.Error
import com.example.demo.http.model.AuthorsModel
import com.example.demo.http.model.SignUpModel
import com.example.demo.http.model.StatisticsByIdModel
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel
import com.example.demo.repository.TransactionManager
import org.springframework.stereotype.Component
import java.time.Instant

typealias UserIdFetchResult = Either<Error, UserOutputModel>

typealias TokenResult = Either<Error, Token>

typealias SignUpResult = Either<Error, SignUpModel>

typealias UsernameFetchResult = Either<Error, List<UserOutputModel>>

typealias StatisticsByIdFetchResult = Either<Error, List<StatisticsByIdModel>>

typealias StatisticsFetchResult = Either<Error, List<StatisticsModel>>

typealias LogOutResult = Either<Error, Boolean>

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
                failure(Error.invalidUserId)
            } else {
                val user = it.usersRepository.getUserById(id)
                if (user == null) {
                    failure(Error.nonExistentUserId)
                } else success(user)
            }
        }
    }

    fun createUser(username: String?, password: String?): SignUpResult {
        return transactionManager.run {
            when {
                username.isNullOrEmpty() -> failure(Error.invalidUsername)
                password.isNullOrEmpty() -> failure(Error.invalidPassword)
                username.length < 3 || username.length > 30 -> failure(Error.invalidUsernameLength)
                password.length < 6 || password.length > 30 -> failure(Error.invalidPasswordLength)
                it.usersRepository.getUserByUsername(username) != null -> failure(Error.repeatedUsername)
                else -> {
                    val userId = it.usersRepository.createUser(username, password)
                    val token = createToken(userId)
                    val signUpModel = SignUpModel(token.token, userId)
                    success(signUpModel)
                }
            }
        }
    }

    fun logIn(username: String?, password: String?): TokenResult {
        return transactionManager.run {
            if (username.isNullOrEmpty()) return@run failure(Error.invalidUsername)
            val user = it.usersRepository.getUserByUsername(username)
            when {
                user == null -> failure(Error.nonExistentUsername)
                password.isNullOrEmpty() -> failure(Error.invalidPassword)
                !userDomain.validatePassword(password, it.usersRepository.getUserPassword(username)) -> failure(Error.wrongPassword)
                else -> {
                    it.usersRepository.deleteAuthentication(username)
                    val token = createToken(user.id)
                    success(token)
                }
            }
        }
    }

    fun logOut(username: String?): LogOutResult {
        return transactionManager.run {
            success(it.usersRepository.deleteAuthentication(username))
        }
    }

    fun getStatisticsByUsername(username: String?): StatisticsByIdFetchResult {
        return transactionManager.run {
            if (username == null) return@run failure(Error.invalidUsername)
            val users = it.usersRepository.getUsersByUsername(username)
            if (users == null) failure(Error.nonExistentUsername)
            else success(it.usersRepository.getStatisticsByUsername(username))

        }
    }

    fun getStatistics(): StatisticsFetchResult {
        return transactionManager.run {
            success(it.usersRepository.getStatistics())
        }
    }

    fun getUsersByUsername(username: String?): UsernameFetchResult {
        return transactionManager.run {
            if (username == null) {
                failure(Error.invalidUsername)
            } else {
                val users = it.usersRepository.getUsersByUsername(username)
                if (users != null) success(users)
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

    fun getUserToken(username: String): Token {
        return transactionManager.run {
            it.usersRepository.getUserToken(username)
        }
    }

    fun getUserByToken(token: String): UserModel? {
        return transactionManager.run {
            it.usersRepository.getUserByToken(token)
        }
    }
}


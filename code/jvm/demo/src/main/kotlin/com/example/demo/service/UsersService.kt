package com.example.demo.service

import com.example.demo.domain.UserDomain
import com.example.demo.http.model.UserOutputModel
import com.example.demo.repository.TransactionManager
import com.example.demo.repository.UsersRepository
import org.springframework.stereotype.Component
import java.time.Clock

sealed class TokenResult {
    object ValidToken : TokenResult()

    object InvalidToken : TokenResult()


}

@Component
class UsersService(
    private val userRepository: UsersRepository,
    private val transactionManager: TransactionManager,
    private val userDomain: UserDomain
) {

    fun getUserById(id: Int?): UserOutputModel? {
        require(id != null) { "Invalid id" }
        return userRepository.getUserById(id)
    }

    fun createUser(username: String?, password: String?) {
        require(!username.isNullOrEmpty()) { "Invalid username" }
        require(!password.isNullOrEmpty()) { "Invalid Password" }
        require(getUserByUsername(username) != null) { "Username is already being used" }
        return userRepository.createUser(username, password)
    }

    fun getStatisticsById(id: Int?) {
        require(id != null) { "Invalid id" }
        userRepository.getStatisticsById(id) //TODO("Temos de dar return")
    }

    fun getGamesCount(id: Int?): Int {
        require(id != null) { "Invalid id" }
        return userRepository.getGamesCount(id)
    }

    fun getUserByUsername(username: String?): Int? {
        require(username != null) { "Invalid username" }
        return userRepository.getUserByUsername(username)
    }

    fun getUserPassword(username: String?): String {
        require(username != null) { "Invalid username" }
        require(userRepository.getUserByUsername(username) != null) { "There's no user with the given username" }
        return userRepository.getUserPassword(username)
    }

    fun createToken(id: Int?): TokenResult {
        val user = getUserById(id)
        require(id != null) { "Invalid id" }
        require(getUserById(id) != null) { "Invalid user id" }
        return transactionManager.run {
            val userRepository = it.userRepository
            val user = userRepository.getUserById(id) ?: TODO()

            val tokenValue = userDomain.generateTokenValue()
            val now = Clock.now()
        }
    }
}

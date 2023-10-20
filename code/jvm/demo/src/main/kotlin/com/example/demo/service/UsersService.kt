package com.example.demo.service

import com.example.demo.domain.Authentication
import com.example.demo.domain.UserDomain
import com.example.demo.http.model.StatisticsModel
import com.example.demo.http.model.UserModel
import com.example.demo.repository.UsersRepository
import org.springframework.stereotype.Component
import java.time.Instant

sealed class TokenResult {
    object ValidToken : TokenResult()

    object InvalidToken : TokenResult()
}

private const val authors = "Vasco Branco - 48259\nJosé Borges - 48269\nSérgio Capela - 48080"

@Component
class UsersService(
    private val userRepository: UsersRepository,
    private val userDomain: UserDomain
) {

    fun getUserById(id: Int?): UserModel? {
        require(id != null) { "Invalid id" }
        return userRepository.getUserById(id)
    }

    fun createUser(username: String?, password: String?) {
        require(!username.isNullOrEmpty()) { "Invalid username" }
        require(!password.isNullOrEmpty()) { "Invalid Password" }
        require(getUserByUsername(username) == null) { "Username is already being used" }
        val userId = userRepository.createUser(username, password)
        createToken(userId)
    }

    fun getStatisticsById(id: Int?): StatisticsModel {
        require(id != null) { "Invalid id" }
        return userRepository.getStatisticsById(id)
    }

    fun getGamesCount(id: Int?): Int {
        require(id != null) { "Invalid id" }
        return userRepository.getGamesCount(id)
    }

    fun getUserByUsername(username: String?): UserModel? {
        require(username != null) { "Invalid username" }
        return userRepository.getUserByUsername(username)
    }

    fun getUserPassword(username: String?): String {
        require(username != null) { "Invalid username" }
        require(userRepository.getUserByUsername(username) != null) { "There's no user with the given username" }
        return userRepository.getUserPassword(username)
    }

    fun getAuthors() = authors

    fun createToken(id: Int){
        val token = Authentication(userDomain.generateTokenValue(), id, Instant.now(), Instant.now())
        userRepository.createAuthentication(token)
    }

    /*fun createToken(id: Int?): TokenResult {
        require(id != null) { "Invalid id" }
        require(getUserById(id) != null) { "Invalid user id" }

            val user = userRepository.getUserById(id) ?: TokenResult.InvalidToken

            if (user is TokenResult.InvalidToken) return TokenResult.InvalidToken

            val now = userRepository.getCurrDate()
            val newToken = Authentication(
                userDomain.generateTokenValue()
                (user as UserModel).id,
                now,
                now
            )
            userRepository.createToken(newToken)
            return TokenResult.ValidToken
    }*/

    fun authenticate(token: String?): Boolean {
        require(token != null){ "Invalid token" }

        return (userRepository.getToken(token) != null)
    }

    fun processAuthorizationHeaderValue(authorizationValue: String?): String? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return parts[1]
    }
    companion object {
        const val SCHEME = "bearer"
    }
}


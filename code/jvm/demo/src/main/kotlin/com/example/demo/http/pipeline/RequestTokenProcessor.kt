package com.example.demo.http.pipeline

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.service.UsersService
import org.springframework.stereotype.Component

@Component
class RequestTokenProcessor( val usersService: UsersService) {
    fun processAuthorizationHeaderValue(authorizationValue: String?): AuthenticatedUser? {
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
        return usersService.getUserByToken(parts[1])?.let {
            AuthenticatedUser(
                it,
                parts[1]
            )
        }
    }

    fun processAuthorizationCookieValue(authorizationValue: String?): AuthenticatedUser? {
        if (authorizationValue == "" || authorizationValue == null) {
            return null
        }

        return usersService.getUserByToken(authorizationValue)?.let {
            AuthenticatedUser(
                it,
                authorizationValue
            )
        }
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
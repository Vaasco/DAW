package com.example.demo.domain

import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserDomain {
    fun validatePassword(password: String, validationInfo: String) = password == validationInfo
    fun generateTokenValue() = UUID.randomUUID().toString()
}
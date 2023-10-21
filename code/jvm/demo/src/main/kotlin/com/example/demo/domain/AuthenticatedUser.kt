package com.example.demo.domain

import com.example.demo.http.model.UserTemp

class AuthenticatedUser(
    val user: UserTemp,
    val token: String
)
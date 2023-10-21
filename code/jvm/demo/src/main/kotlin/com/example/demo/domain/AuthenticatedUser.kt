package com.example.demo.domain

import com.example.demo.http.model.UserModel

class AuthenticatedUser(
    val user : UserModel,
    val token: String
)
package com.example.demo.http.model

data class UserModel(
    val username : String,
    val password : String
)

data class PasswordValidationInfo(
    val password : String
)

data class UserTemp(//TODO("Delete later")
    val id: Int,
    val username : String,
    val password : PasswordValidationInfo
)
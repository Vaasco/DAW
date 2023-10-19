package com.example.demo.domain

import java.util.UUID;

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val token: UUID
)

data class TempUser(val name:String) //Class created so that the Json parse works correctly


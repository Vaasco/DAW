package com.example.demo.domain

import java.time.Instant

class Token(
    val tokenValidationInfo: String,
    val userId : Int,
    val createdAt : Instant,
    val lastUsedAt : Instant
)
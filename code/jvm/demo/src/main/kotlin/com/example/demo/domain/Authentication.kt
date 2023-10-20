package com.example.demo.domain

import java.time.Instant

class Authentication(
    val token: String,
    val userId : Int,
    val createdAt : Instant,
    val lastUsedAt : Instant
)
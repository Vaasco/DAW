package com.example.demo.domain

import java.sql.Date

class Authentication(
    val token: String,
    val userId : Int,
    val createdAt : Date,
    val lastUsedAt : Date
)
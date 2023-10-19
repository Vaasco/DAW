package com.example.demo.http.model

data class UserOutputModel(
    val id:Int,
    val token:String,
    val username : String,
    val password : String
)
package com.example.demo.repository

import com.example.demo.domain.User

interface UsersRepository {
    fun getById(id : Int) : String?

    fun createUser(username : String, password: String)

    fun checkGameState(id : Int):String?
}
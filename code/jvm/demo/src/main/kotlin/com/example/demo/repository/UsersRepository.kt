package com.example.demo.repository

import com.example.demo.domain.User

interface UsersRepository{
    fun getById(id:Int) : User?
    fun insert(name:String)
}
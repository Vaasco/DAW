package com.example.demo.repository

import com.example.demo.repository.jbdi.JdbiGamesRepository


interface Transaction{
    val usersRepository : UsersRepository
    val gameRepository : JdbiGamesRepository
    fun rollback()
}
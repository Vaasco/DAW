package com.example.demo.service

import com.example.demo.domain.TempUser
import com.example.demo.repository.UsersRepository
import org.springframework.stereotype.Component


@Component
class UsersService(private val userRepository: UsersRepository){
    fun getById(id:Int) = userRepository.getById(id)
    fun createUser(username : TempUser) = userRepository.createUser(username)
}
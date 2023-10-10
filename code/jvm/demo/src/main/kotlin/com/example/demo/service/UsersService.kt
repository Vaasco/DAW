package com.example.demo.service

import com.example.demo.repository.UsersRepository
import com.example.demo.service.exception.NotFoundException
import org.springframework.stereotype.Component


@Component
class UsersService(private val userRepository: UsersRepository){
    fun getById(id:Int) = userRepository.getById(id) ?: throw NotFoundException() //TODO mudar isto!

    fun createUser(username:String) = userRepository.createUser(username) ?: throw NotFoundException() //TODO mudar isto!
}
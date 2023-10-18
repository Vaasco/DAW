package com.example.demo.service

import com.example.demo.domain.cellsInDirection
import com.example.demo.repository.UsersRepository
import org.springframework.stereotype.Component


@Component
class UsersService(private val userRepository: UsersRepository){
    fun getById(id:Int) = userRepository.getById(id)
    fun createUser(username : String, password : String) = userRepository.createUser(username, password)

    fun getGameState(id : Int) = userRepository.getGameState(id)

    fun getStatisticsById(id : Int) = userRepository.getStatisticsById(id)
}

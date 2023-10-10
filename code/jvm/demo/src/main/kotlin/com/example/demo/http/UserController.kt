package com.example.demo.http

import com.example.demo.domain.User
import com.example.demo.service.UsersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val usersService: UsersService) {

    @GetMapping(PathTemplate.USER_BY_ID)
    fun getById(@PathVariable id : Int) =  usersService.getById(id)


    @GetMapping(PathTemplate.CREATE_USER)
    fun createUser(@RequestBody username:String) = usersService.createUser(username)
}
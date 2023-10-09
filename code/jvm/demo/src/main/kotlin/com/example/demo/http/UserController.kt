package com.example.demo.http

import com.example.demo.service.UsersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val usersService: UsersService) {
    //@GetMapping(  )
}
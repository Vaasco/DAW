package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


//dummy list for now
//private val list: MutableList<User> = mutableListOf()

@RequestMapping("/gomoku")
@RestController
class Controller() {
    @GetMapping("/user")
    fun getUsers(@RequestParam user:Int) = "It works $user!"

}
package com.example.demo.http

import com.example.demo.service.UsersService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val usersService: UsersService) {

    @GetMapping(PathTemplate.USER_BY_ID)
    fun getById(@PathVariable id: Int?) = usersService.getUserById(id)

    @PostMapping(PathTemplate.CREATE_USER)
    fun createUser(@RequestBody username: String?, @RequestBody password : String?) =
        usersService.createUser(username, password)


    @GetMapping(PathTemplate.STATICS)
    fun getStatisticsById(@PathVariable id: Int?) = usersService.getStatisticsById(id)

    @GetMapping(PathTemplate.GAMES_NUMBER)
    fun getGamesCount(@PathVariable id: Int?) = usersService.getGamesCount(id)

    @GetMapping(PathTemplate.USER_BY_USERNAME)
    fun getByUsername(@PathVariable username: String?) = usersService.getUserByUsername(username)
}


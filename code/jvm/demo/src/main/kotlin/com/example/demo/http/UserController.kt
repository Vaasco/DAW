package com.example.demo.http

import com.example.demo.http.model.UserModel
import com.example.demo.service.*
import com.example.demo.http.siren.SirenMaker
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(private val usersService: UsersService) {

    @GetMapping(PathTemplate.USER_BY_ID)
    fun getUserById(@PathVariable id: Int?): ResponseEntity<*> {
        val res = usersService.getUserById(id)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetUserById(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }


    @PostMapping(PathTemplate.CREATE_USER)
    fun createUser(@RequestBody userModel: UserModel): ResponseEntity<*> {
        val res = usersService.createUser(userModel.username, userModel.password)
        return handleResponse(res) {
            val siren = SirenMaker().sirenLogIn(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @PostMapping(PathTemplate.LOGIN)
    fun login(@RequestBody userModel: UserModel): ResponseEntity<*> {
        val res = usersService.logIn(userModel.username, userModel.password)
        return handleResponse(res) {
            val siren = SirenMaker().sirenLogIn(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }


    @GetMapping(PathTemplate.STATISTICS_BY_ID)
    fun getStatisticsById(@PathVariable id: Int?): ResponseEntity<*> {
        val res = usersService.getStatisticsById(id)
        return handleResponse(res) {
            val siren = SirenMaker().sirenStatisticsById(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @GetMapping(PathTemplate.STATISTICS)
    fun getStatistics():ResponseEntity<*>{
        val res = usersService.getStatistics()
        return handleResponse(res) {
            val siren = SirenMaker().sirenStatistics(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @GetMapping(PathTemplate.USER_BY_USERNAME)
    fun getUserByUsername(@PathVariable username: String?) :ResponseEntity<*>{
        val res = usersService.getUserByUsername(username)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetUserByUsername(it)
            ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(siren)
        }
    }

    @GetMapping(PathTemplate.AUTHORS)
    fun getAuthors() = usersService.getAuthors()
}
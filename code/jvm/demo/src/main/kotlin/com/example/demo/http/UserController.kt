package com.example.demo.http

import com.example.demo.domain.Error
import com.example.demo.domain.Failure
import com.example.demo.domain.Success
import com.example.demo.http.errors.*
import com.example.demo.http.errors.UserIdFetchError
import com.example.demo.http.model.UserModel
import com.example.demo.service.*
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val usersService: UsersService) {

    @GetMapping(PathTemplate.USER_BY_ID)
    fun getUserById(@PathVariable id: Int?): ResponseEntity<*> {
        return when(val res = usersService.getUserById(id)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> when (res.value) {
                UserIdFetchError.InvalidId -> Error.response(Error.invalidId.code, Error.invalidId)
                UserIdFetchError.NonExistingUserId -> Error.response(Error.nonExistingUserId.code, Error.nonExistingUserId)
            }
        }
    }

    @PostMapping(PathTemplate.CREATE_USER)
    fun createUser(@RequestBody userModel: UserModel): ResponseEntity<*> {
        return when (val res = usersService.createUser(userModel.username, userModel.password)) {
            is Success -> ResponseEntity.status(201).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> when (res.value) {
                UserCreationError.InvalidPassword -> Error.response(Error.invalidPassword.code, Error.invalidPassword)
                UserCreationError.InvalidUsername -> Error.response(Error.invalidUsername.code, Error.invalidUsername)
                UserCreationError.RepeatedUsername -> Error.response(Error.repeatedUsername.code, Error.repeatedUsername)
            }

        }
    }

    @PostMapping(PathTemplate.LOGIN)
    fun login(@RequestBody userModel: UserModel): ResponseEntity<*> {
        return when (val res = usersService.logIn(userModel.username, userModel.password)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> when (res.value) {
                UserLoginError.InvalidPassword -> Error.response(Error.invalidPassword.code, Error.invalidPassword)
                UserLoginError.InvalidUsername -> Error.response(Error.invalidUsername.code, Error.invalidUsername)
                UserLoginError.NonExistentUsername -> Error.response(Error.nonExistingUsername.code, Error.nonExistingUsername)
                UserLoginError.WrongPassword -> Error.response(Error.wrongPassword.code, Error.wrongPassword)
            }
        }
    }


    @GetMapping(PathTemplate.STATISTICS_BY_ID)
    fun getStatisticsById(@PathVariable id: Int?): ResponseEntity<*> {
        return when (val res = usersService.getStatisticsById(id)) {
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> when (res.value) {
                StatisticsError.InvalidId -> Error.response(Error.invalidId.code, Error.invalidId)
                StatisticsError.NonExistingUser -> Error.response(Error.nonExistingUserId.code, Error.nonExistingUserId)
            }
        }
    }

    @GetMapping(PathTemplate.STATISTICS)
    fun getStatistics():ResponseEntity<*>{
        return when(val res = usersService.getStatistics()){
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> Error.response(Error.internalServerError.code, Error.internalServerError)
        }
    }

    @GetMapping(PathTemplate.USER_BY_USERNAME)
    fun getByUsername(@PathVariable username: String?) :ResponseEntity<*>{
        return when(val res = usersService.getUserByUsername(username)){
            is Success -> ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON)
                .body(res.value)
            is Failure -> when(res.value){
                UsernameFetchError.InvalidUsername -> Error.response(Error.invalidUsername.code, Error.invalidUsername)
                UsernameFetchError.NonExistingUsername -> Error.response(Error.nonExistingUsername.code, Error.nonExistingUsername)
            }
        }
    }
    @GetMapping(PathTemplate.AUTHORS)
    fun getAuthors() = usersService.getAuthors()

}
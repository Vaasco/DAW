package com.example.demo.http

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.domain.Either
import com.example.demo.domain.Token
import com.example.demo.http.model.UserModel
import com.example.demo.service.*
import com.example.demo.http.siren.SirenMaker
import com.example.demo.http.siren.response
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseCookie

@RestController
class UserController(private val usersService: UsersService) {

    @GetMapping(PathTemplate.USER_BY_ID)
    fun getUserById(@PathVariable id: Int?): ResponseEntity<*> {
        val res = usersService.getUserById(id)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetUserById(it)
            siren.response(200)
        }
    }


    @PostMapping(PathTemplate.CREATE_USER)
    fun createUser(@RequestBody userModel: UserModel, response: HttpServletResponse): ResponseEntity<*> {
        val res = usersService.createUser(userModel.username, userModel.password)

        if (res is Either.Right) {
            val token = usersService.getUserToken(userModel.username)
            val userId = usersService.getUserByToken(token.token)?.id

            val cookies = createCookies(userModel, token, userId)

            cookies.forEach { response.addHeader(HttpHeaders.SET_COOKIE, it.toString()) }
        }

        return handleResponse(res) {
            val siren = SirenMaker().sirenSignIn(it)
            siren.response(200)
        }
    }

    @PostMapping(PathTemplate.LOGIN)
    fun login(@RequestBody userModel: UserModel, response: HttpServletResponse): ResponseEntity<*> {
        val res = usersService.logIn(userModel.username, userModel.password)

        if (res is Either.Right) {
            val token = usersService.getUserToken(userModel.username)
            val userId = usersService.getUserByToken(token.token)?.id

            val cookies = createCookies(userModel, token, userId)
            cookies.forEach { response.addHeader(HttpHeaders.SET_COOKIE, it.toString()) }
        }

        return handleResponse(res) {
            val siren = SirenMaker().sirenLogIn(it)
            siren.response(200)
        }
    }

    @PostMapping(PathTemplate.LOGOUT)
    fun logout(user: AuthenticatedUser, response: HttpServletResponse): ResponseEntity<*> {
        val res = usersService.logOut(user.user.username)

        val tokenCookie = Cookie("token", null)
        tokenCookie.maxAge = 0
        tokenCookie.path = "/"
        tokenCookie.secure = false
        tokenCookie.isHttpOnly = true

        val usernameCookie = Cookie("username", null)
        usernameCookie.maxAge = 0
        usernameCookie.path = "/"
        usernameCookie.secure = false
        usernameCookie.isHttpOnly = false

        val idCookie = Cookie("id", null)
        idCookie.maxAge = 0
        idCookie.path = "/"
        idCookie.secure = false
        idCookie.isHttpOnly = false

        response.addCookie(tokenCookie)
        response.addCookie(usernameCookie)
        response.addCookie(idCookie)

        return handleResponse(res) {
            val siren = SirenMaker().sirenLogOut(it)
            siren.response(200)
        }
    }


    @GetMapping(PathTemplate.STATISTICS_BY_USERNAME)
    fun getStatisticsByUsername(@PathVariable username: String?): ResponseEntity<*> {
        val res = usersService.getStatisticsByUsername(username)
        return handleResponse(res) {
            val siren = SirenMaker().sirenStatisticsByUsername(it)
            siren.response(200)
        }
    }

    @GetMapping(PathTemplate.STATISTICS)
    fun getStatistics(): ResponseEntity<*> {
        val res = usersService.getStatistics()
        return handleResponse(res) {
            val siren = SirenMaker().sirenStatistics(it)
            siren.response(200)
        }
    }

    @GetMapping(PathTemplate.USER_BY_USERNAME)
    fun getUserByUsername(@PathVariable username: String?): ResponseEntity<*> {
        val res = usersService.getUsersByUsername(username)
        return handleResponse(res) {
            val siren = SirenMaker().sirenGetUsersByUsername(it)
            siren.response(200)
        }
    }

    @GetMapping(PathTemplate.AUTHORS)
    fun getAuthors(): ResponseEntity<*> {
        val res = usersService.getAuthors()
        return handleResponse(res) {
            val siren = SirenMaker().sirenAuthors(it)
            siren.response(200)
        }
    }

    @GetMapping(PathTemplate.COOKIES)
    fun getCookies(request: HttpServletRequest): Array<out Cookie>? {
        return request.cookies
    }
}

fun createCookies(userModel: UserModel, token: Token, userId: Int?): List<ResponseCookie> {
    val tokenCookie = ResponseCookie
        .from("token", token.token)
        .maxAge(3600)
        .path("/")
        .httpOnly(true)
        .secure(false)
        .build()

    val usernameCookie = ResponseCookie
        .from("username", userModel.username)
        .maxAge(3600)
        .path("/")
        .httpOnly(false)
        .secure(false)
        .build()

    val idCookie = ResponseCookie
        .from("id", userId.toString())
        .maxAge(3600)
        .path("/")
        .httpOnly(false)
        .secure(false)
        .build()

    return listOf(tokenCookie, usernameCookie, idCookie)
}
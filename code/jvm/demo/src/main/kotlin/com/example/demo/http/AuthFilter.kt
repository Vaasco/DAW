package com.example.demo.http

import com.example.demo.service.GamesService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import com.example.demo.service.UsersService
import org.springframework.stereotype.Component

@Component
class AuthFilter(private val usersService: UsersService) : HttpFilter() {
    val unAuthPaths = listOf(
        // rever com o stor
        PathTemplate.GAMES_NUMBER.dropLast(5),
        PathTemplate.STATICS.dropLast(5),
        PathTemplate.HOME,
        PathTemplate.AUTHORS
    )

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val path = request.requestURI.dropLastWhile { it != '/' }
        if (unAuthPaths.contains(path) ) chain.doFilter(request, response)
        else{
            val token  = request.getHeader("Authorization header")
            if (usersService.authenticate(token)) chain.doFilter(request, response)
            else response.status = HttpServletResponse.SC_UNAUTHORIZED
        }



    }

}
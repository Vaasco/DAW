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


    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val reqPath = request.requestURI.dropLastWhile { it != '/' }
        val isVariable = unAuthVariablePaths.contains(reqPath)
        val path =  if(isVariable) reqPath else request.requestURI
        if (isVariable || unAuthPaths.contains(path) ) chain.doFilter(request, response)
        else{
            val token  = usersService.processAuthorizationHeaderValue(
                request.getHeader(NAME_AUTHORIZATION_HEADER)
            )
            if (usersService.authenticate(token)) chain.doFilter(request, response)
            else response.status = HttpServletResponse.SC_UNAUTHORIZED
        }


    }
    companion object{
        val unAuthPaths = listOf(
            // rever com o stor
            PathTemplate.GAMES_NUMBER.dropLast(5),
            PathTemplate.STATICS.dropLast(5),
            PathTemplate.HOME,
            PathTemplate.AUTHORS
        )
        const val NAME_AUTHORIZATION_HEADER = "Authorization"
    }

}
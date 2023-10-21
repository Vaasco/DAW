package com.example.demo.http

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import com.example.demo.service.UsersService
import org.springframework.stereotype.Component

/*@Component
class AuthFilter(private val usersService: UsersService) : HttpFilter() {

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val bufferedRequest = HttpServletRequestWrapper(request).apply {
            inputStream = request.inputStream.buffered()
        }
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
        val unAuthVariablePaths = listOf(
            PathTemplate.GAMES_COUNT,
            PathTemplate.STATICS,
            PathTemplate.USER_BY_ID,
            PathTemplate.USER_BY_USERNAME
        ).map{ path -> path.dropLastWhile { it != '/' }}
        val unAuthPaths = listOf(
            PathTemplate.HOME,
            PathTemplate.AUTHORS,
            PathTemplate.CREATE_USER
        )
        const val NAME_AUTHORIZATION_HEADER = "Authorization"
    }
}*/
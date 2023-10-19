package com.example.demo.http

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import com.example.demo.service.UsersService
import org.springframework.stereotype.Component

@Component
class LogFilter : HttpFilter() {
    val unAuthPaths = listOf<String>()

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, usersService: UsersService) {
        val path = request.requestURI
        if ( unAuthPaths.contains(path) ){
            chain.doFilter(request, response)
        }
        else{
            val token  = request.getHeader("Authorization header")
            if (usersService.authenticate) chain.doFilter(request, response)
            else response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
        }



    }

}
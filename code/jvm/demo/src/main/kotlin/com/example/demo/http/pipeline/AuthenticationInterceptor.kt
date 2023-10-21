package com.example.demo.http.pipeline

import com.example.demo.domain.AuthenticatedUser

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthenticationInterceptor(
    private val authorizationHeaderProcessor: RequestTokenProcessor
) : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.methodParameters.any {
                it.parameterType == AuthenticatedUser::class.java
            }
        ) {
            val user = authorizationHeaderProcessor.processAuthorizationHeaderValue(request.getHeader("NAME_AUTORIZATION_HEADER"))
            return if(user == null) {
                response.status = 401
                response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, RequestTokenProcessor.SCHEME)
                false
            }
                else{

                    true
                }
            }
        return true
    }
    companion object {

        private val logger = LoggerFactory.getLogger(AuthenticationInterceptor::class.java)
        const val NAME_AUTHORIZATION_HEADER = "Authorization"
        private const val NAME_WWW_AUTHENTICATE_HEADER = "WWW-Authenticate"
    }
}
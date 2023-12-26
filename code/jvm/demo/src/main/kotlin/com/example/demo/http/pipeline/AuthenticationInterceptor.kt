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
        logger.info("Calling $handler")
        if ((handler is HandlerMethod) && handler.methodParameters.any {
                it.parameterType == AuthenticatedUser::class.java
            }
        ) {

            val cookie = if (request.cookies != null) request.cookies.find { it.name == "token" } else null
            val bearer = request.getHeader(NAME_AUTHORIZATION_HEADER)
            val user = if (cookie != null) authorizationHeaderProcessor.processAuthorizationCookieValue(cookie.value)
            else authorizationHeaderProcessor.processAuthorizationHeaderValue(bearer)

            return if (user == null) {
                response.status = 401
                response.addHeader(NAME_WWW_AUTHENTICATE_HEADER, RequestTokenProcessor.SCHEME)
                false
            } else {
                AuthenticatedUserArgumentResolver.addUserTo(user, request)
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
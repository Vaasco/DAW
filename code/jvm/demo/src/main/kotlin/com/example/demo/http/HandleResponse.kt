package com.example.demo.http

import com.example.demo.domain.AuthenticatedUser
import com.example.demo.domain.Failure
import com.example.demo.domain.Success
import com.example.demo.http.model.UserOutputModel
import com.example.demo.http.siren.SirenModel
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity

class HandleResponse {
    /*inline fun <reified T> handleResponse(siren: SirenModel<Result<T>>, res: Result<T>, status: Int): ResponseEntity<*> {
        return when (res) {
            is Success -> {
                if (res.value is AuthenticatedUser) {
                    val user = res.value as AuthenticatedUser
                    ResponseEntity.status(status).header("Authorization", "Bearer ${user.token}").body(siren)
                } else {
                    ResponseEntity.status(status).body(siren)
                }
            }
            is Failure -> ErrorType.response(res.value)
        }
    }*/
}
package com.example.demo.http

import com.example.demo.domain.Error
import com.example.demo.domain.Either
import com.example.demo.domain.Failure
import com.example.demo.domain.Success
import org.springframework.http.ResponseEntity

inline fun <reified T> handleResponse(res: Either<Error, T>, function: (T) -> ResponseEntity<*>): ResponseEntity<*> {
    return when (res) {
        is Success -> {
            function(res.value)
        }
        is Failure -> {
            Error.response(res.value)
        }
    }
}

package com.example.demo.http

import com.example.demo.domain.Either
import com.example.demo.domain.Error
import com.example.demo.domain.ErrorMessage
import com.example.demo.domain.failure

fun <T> handleDatabaseException(e: Exception): Either<Error, T> {
    return when {
        e is org.jdbi.v3.core.statement.UnableToExecuteStatementException -> {
            val constraintViolation = extractConstraintViolation(e)
            Error.databaseExceptions.forEach {
                if (constraintViolation.contains(it)) return failure(Error(400, ErrorMessage(it.replace("_", " "))))
            }
            failure(Error.internalServerError)
        }
        else -> failure(Error.internalServerError)
    }
}

fun extractConstraintViolation(e: Exception): String {
    return e.message ?: e.cause?.message ?: ""
}
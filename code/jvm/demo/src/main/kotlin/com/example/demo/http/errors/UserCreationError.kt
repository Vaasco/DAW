package com.example.demo.http.errors

import com.example.demo.domain.Either

sealed class UserCreationError {
    object InvalidUsername : UserCreationError()

    object InvalidPassword : UserCreationError()

    object RepeatedUsername : UserCreationError()

}

typealias UserCreationResult = Either<UserCreationError, Int>
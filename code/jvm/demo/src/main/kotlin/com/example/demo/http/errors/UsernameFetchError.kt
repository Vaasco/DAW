package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.UserModel

sealed class UsernameFetchError {
    object InvalidUsername : UsernameFetchError()

    object NonExistingUser : UsernameFetchError()
}

typealias UsernameFetchResult = Either<UsernameFetchError, UserModel?>
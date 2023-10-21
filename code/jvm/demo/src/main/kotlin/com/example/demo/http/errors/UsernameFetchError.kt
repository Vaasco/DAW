package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.UserModel

sealed class UsernameFetchError {
    object InvalidUsername : UsernameFetchError()

    object NonExistingUsername : UsernameFetchError()
}

typealias UsernameFetchResult = Either<UsernameFetchError, UserModel?>
package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.UserModel


typealias UserIdFetchResult = Either<UserIdFetchError, UserModel>

sealed class UserIdFetchError{
    object InvalidId : UserIdFetchError()

    object NonExistingUser : UserIdFetchError()

    object InvalidToken : UserIdFetchError()
}
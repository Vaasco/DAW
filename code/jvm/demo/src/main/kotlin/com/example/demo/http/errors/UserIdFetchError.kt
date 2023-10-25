package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.UserModel
import com.example.demo.http.model.UserOutputModel


typealias UserIdFetchResult = Either<UserIdFetchError, UserOutputModel>

sealed class UserIdFetchError{
    object InvalidId : UserIdFetchError()

    object NonExistingUserId : UserIdFetchError()

}
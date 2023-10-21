package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.UserModel

sealed class GetUserError {
    object InvalidToken : GetUserError()

    object NonExistingUser : GetUserError()
}

typealias GetUserResult = Either<GetUserError,UserModel>
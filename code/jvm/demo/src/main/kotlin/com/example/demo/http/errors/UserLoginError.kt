package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.domain.Token

sealed class UserLoginError {
    object InvalidUsername: UserLoginError()

    object InvalidPassword: UserLoginError()

    object NonExistentUsername: UserLoginError()

    object WrongPassword: UserLoginError()
}

typealias UserLoginResult = Either<UserLoginError, Token>

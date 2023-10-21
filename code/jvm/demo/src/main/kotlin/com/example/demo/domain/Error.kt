package com.example.demo.domain

import org.springframework.http.ResponseEntity

class Error{

    companion object{
        fun response(status: Int,message : Error) = ResponseEntity
            .status(status)
            .body<Any>(message)

        //Create User
        val invalidPassword = Error()
        val invalidUsername = Error()
        val repeatedUsername = Error()

        //Statistics by id
        val invalidId = Error()
        val nonExistingUser = Error()

        //get Token
        val invalidToken = Error()

        //Internal error
        val internalServerError = Error()

        //Get Game by Id
        val nonExistingGame = Error()

        //Create lobby
        val invalidBoardSize = Error()
        val invalidRules = Error()
        val invalidVariant = Error()

        //Play
        val notYourTurn = Error()
        val unauthorized = Error()
        val invalidRow = Error()
        val invalidCol = Error()
        val invalidPosition = Error()
        val invalidUserId = Error()
        val gameEnded = Error()
        val invalidGameId = Error()
        val wrongAccount = Error()
    }
}
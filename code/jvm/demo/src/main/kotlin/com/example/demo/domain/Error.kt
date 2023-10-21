package com.example.demo.domain

import org.springframework.http.ResponseEntity

class Error(val code: Int, val message: String) {

    companion object{
        fun response(status: Int,message : Error) = ResponseEntity
            .status(status)
            .body<Any>(message)

        //Create User
        val invalidPassword = Error(400, "Invalid password")
        val invalidUsername = Error(400, "Invalid username")
        val repeatedUsername = Error(400, "There's is already a user with that username")

        //Statistics by id
        val invalidId = Error(400, "Invalid id")
        val nonExistingUserId = Error(404, "There's no user with the given id")
        val nonExistingUsername = Error(404, "There's no user with the given username")

        //get Token
        val invalidToken = Error(400, "Invalid token")

        //Internal error
        val internalServerError = Error(500, "Internal server error")

        //Get Game by Id
        val nonExistingGame = Error(404,"There's no game with the given id")

        //Create lobby
        val invalidBoardSize = Error()
        val invalidRules = Error(400, "Invalid rules")
        val invalidVariant = Error(400, "Invalid variant")

        //Play
        val notYourTurn = Error(401, "It's not your turn")
        val unauthorized = Error(401, "Unauthorized")
        val invalidRow = Error(400, "Invalid row")
        val invalidCol = Error(400, "Invalid col")
        val invalidPosition = Error(400, "Invalid position")
        val invalidUserId = Error(400, "Invalid user id")
        val gameEnded = Error(400, "Game has already ended")
        val invalidGameId = Error(400, "Invalid game id")
        val wrongAccount = Error(401, "That's not your account")
        val positionOccupied = Error(400, "Position is already occupied")
    }
}
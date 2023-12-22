package com.example.demo.domain

import org.springframework.http.ResponseEntity

data class ErrorMessage(val error: String)

class Error(val code: Int, val message: ErrorMessage) {


    companion object{

        fun response(error: Error) = ResponseEntity
            .status(error.code)
            .header("content-type", "application/problem+json")
            .body<Any>(error.message)

        //Create User or Login
        val invalidPassword = Error(400, ErrorMessage("Invalid password"))
        val invalidUsername = Error(400, ErrorMessage("Invalid username"))
        val repeatedUsername = Error(400, ErrorMessage("There's is already a user with that username"))
        val wrongPassword = Error(400, ErrorMessage("Wrong Password"))
        val invalidUsernameLength = Error(400, ErrorMessage("Username must have between 3 and 30 characters"))
        val invalidPasswordLength = Error(400, ErrorMessage("Password must have between 6 and 30 characters"))

        //Get User
        val invalidUserId = Error(400, ErrorMessage("Invalid id"))
        val nonExistentUserId = Error(404, ErrorMessage("There's no user with the given id"))
        val nonExistentUsername = Error(404, ErrorMessage("There's no user with the given username"))

        //Internal error
        val internalServerError = Error(500, ErrorMessage("Internal server error"))

        //Get Game by id
        val nonExistentGame = Error(404,ErrorMessage("There's no game with the given id"))

        //Create lobby
        val invalidRules = Error(400, ErrorMessage("Invalid rules"))
        val invalidVariant = Error(400, ErrorMessage("Invalid variant"))
        val invalidBoardSize = Error(400, ErrorMessage("The board size must be either 15 or 19"))
        //val wrongAccount = Error(401,ErrorMessage("This is not your account"))

        //Play
        val notYourTurn = Error(401, ErrorMessage("It's not your turn"))
        val unauthorized = Error(401, ErrorMessage("Unauthorized"))
        val invalidRow = Error(400, ErrorMessage("Invalid row"))
        val invalidCol = Error(400, ErrorMessage("Invalid col"))
        val invalidPosition = Error(400, ErrorMessage("Invalid position"))
        val gameEnded = Error(400, ErrorMessage("Game has already ended"))
        val invalidGameId = Error(400, ErrorMessage("Invalid game id"))
        val positionOccupied = Error(400, ErrorMessage("Position is already occupied"))
        val wrongGame = Error(401, ErrorMessage("You can't play in this game"))
        val wrongPlace = Error(400, ErrorMessage("You can't place the piece here"))

        //Get Last Game
        val notPlaying = Error(400, ErrorMessage("This player is not playing a game at the moment"))

        //Database exceptions
        val databaseExceptions = listOf(
            "You cannot play two games at the same time",
            "The game has already ended!"
        )
    }
}
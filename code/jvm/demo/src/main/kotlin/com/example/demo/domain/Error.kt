package com.example.demo.domain

import org.springframework.http.ResponseEntity

class Error(val code: Int, val message: String) {

    companion object{
        fun response(error: Error) = ResponseEntity
            .status(error.code)
            .header("content-type", "application/problem+json")
            .body<Any>(error.message)

        //Create User or Login
        val invalidPassword = Error(400, "Invalid password")
        val invalidUsername = Error(400, "Invalid username")
        val repeatedUsername = Error(400, "There's is already a user with that username")
        val wrongPassword = Error(400, "Wrong Password")

        //Statistics by id
        val invalidId = Error(400, "Invalid id")
        val nonExistentUserId = Error(404, "There's no user with the given id")
        val nonExistentUsername = Error(404, "There's no user with the given username")

        //Internal error
        val internalServerError = Error(500, "Internal server error")

        //Get Game by id
        val nonExistentGame = Error(404,"There's no game with the given id")

        //Create lobby
        val invalidRules = Error(400, "Invalid rules")
        val twoGamesAtTheSameTime = Error(400, "You can't play two games at the same time")
        val invalidVariant = Error(400, "Invalid variant")
        val invalidBoardSize = Error(400, "The board size must be either 15 or 19")

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
        val wrongGame = Error(401, "You can't play in this game")
    }
}
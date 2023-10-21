package com.example.demo.http.errors

import com.example.demo.domain.Either

sealed class GamesCountError {
    object InvalidId : GamesCountError()
}

typealias GamesCountFetchResult = Either<GamesCountError, Int>
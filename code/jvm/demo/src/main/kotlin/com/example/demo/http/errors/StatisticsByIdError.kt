package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.StatisticsByIdModel

sealed class StatisticsByIdError {
    object InvalidId : StatisticsByIdError()

    object NonExistingUser : StatisticsByIdError()
}

typealias StatisticsByIdFetchResult = Either<StatisticsByIdError, StatisticsByIdModel>
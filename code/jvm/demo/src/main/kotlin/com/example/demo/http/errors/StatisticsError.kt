package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.StatisticsModel

sealed class StatisticsError {
    object InvalidId : StatisticsError()

    object NonExistingUser : StatisticsError()
}

typealias StatisticsFetchResult = Either<StatisticsError, StatisticsModel>
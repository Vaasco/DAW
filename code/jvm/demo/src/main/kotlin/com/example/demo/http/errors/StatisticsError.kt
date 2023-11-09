package com.example.demo.http.errors

import com.example.demo.domain.Either
import com.example.demo.http.model.StatisticsModel

sealed class StatisticsError {}

typealias StatisticsFetchResult = Either<StatisticsError, List<StatisticsModel>>
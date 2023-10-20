package com.example.demo.repository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


interface TransactionManager{
    fun<R> run(block: (Transaction) -> R) : R
}
package com.example.demo

import BoardMapper
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin

@SpringBootApplication
class DemoApplication {

    @Bean
    fun jdbi(): Jdbi {
        val password = System.getenv("PASSWORD")
        val jdbcDbURl = "jdbc:postgresql://localhost/postgres?user=postgres&password=${password}"
        val dataSrc = PGSimpleDataSource()
        dataSrc.setUrl(jdbcDbURl)

        return Jdbi.create(dataSrc)
            .installPlugin(KotlinPlugin())
        //.registerColumnMapper(BoardMapper())

    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
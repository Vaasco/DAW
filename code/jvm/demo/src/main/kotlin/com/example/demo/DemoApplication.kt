package com.example.demo

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
        val jdbcDbURl = System.getenv("jdbc:postgresql://localhost/postgres?user=postgres&password=postgres")
        val dataSrc = PGSimpleDataSource()
        dataSrc.setUrl(jdbcDbURl)

        return Jdbi.create(dataSrc)
            .installPlugin(KotlinPlugin())
        //.registerColumnMapper(BoardMapper())

    }

    fun main(args: Array<String>) {
        runApplication<DemoApplication>(*args)
    }
}



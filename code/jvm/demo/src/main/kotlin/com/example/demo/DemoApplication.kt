package com.example.demo

import BoardMapper
import com.example.demo.http.pipeline.AuthenticatedUserArgumentResolver
import com.example.demo.http.pipeline.AuthenticationInterceptor
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

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
            .registerColumnMapper(BoardMapper())
    }
}

@Configuration
class PipelineConfigurer(
    val authenticationInterceptor: AuthenticationInterceptor,
    val authenticatedUserArgumentResolver: AuthenticatedUserArgumentResolver
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authenticatedUserArgumentResolver)
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
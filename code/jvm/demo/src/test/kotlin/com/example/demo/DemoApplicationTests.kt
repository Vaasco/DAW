package pt.isel.daw.tictactoe.http

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import org.testng.Assert.assertEquals
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @LocalServerPort
    var port: Int = 0


    @Test
    fun exampleUsingHttpClientOK() {
        val client = HttpClient.newHttpClient()
        val response = client.send(
            HttpRequest
                .newBuilder()
                .uri(URI("http://localhost:$port/users/1"))
                .GET()
                .build(),
            BodyHandlers.ofString()
        )
        assertEquals(200, response.statusCode())
    }

    @Test
    fun exampleUsingHttpClientNotFound() {
        val client = HttpClient.newHttpClient()
        val response = client.send(
            HttpRequest
                .newBuilder()
                .uri(URI("http://localhost:$port/users/a"))
                .GET()
                .build(),
            BodyHandlers.ofString()
        )
        assertEquals(404, response.statusCode())
    }
}
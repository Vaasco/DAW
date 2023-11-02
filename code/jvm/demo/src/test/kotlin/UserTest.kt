import com.example.demo.domain.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpExampleTests {

    @LocalServerPort
    var port: Int = 8080


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

/*
@SpringBootTest
class UserTest {

    private lateinit var board: Board

    @LocalServerPort
    var port = 8080

    @Test
    fun `GET User information`() {
        board = board.play(Position(0.indexToRow(), 0.indexToColumn()), Player.W)
        assertTrue(board.moves[Position(0.indexToRow(), 0.indexToColumn())] == Player.W)
    }

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

}*/
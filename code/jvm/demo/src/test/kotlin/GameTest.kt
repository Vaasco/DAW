import com.example.demo.domain.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testng.Assert.assertTrue

@SpringBootTest
class GameTest {

    private lateinit var board: Board

    @BeforeEach
    fun setupBoard() {
        board = createBoard(Player.W)
    }

    @Test
    fun `Make a play`() {
        board = board.play(Position(0.indexToRow(), 0.indexToColumn()), Player.W)
        assertTrue(board.moves[Position(0.indexToRow(), 0.indexToColumn())] == Player.W)
    }

    @Test
    fun `Game win for player W`() {
        board = board.play(Position(0.indexToRow(), 0.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 0.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 1.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 1.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 2.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 2.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 3.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 3.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 4.indexToColumn()), Player.W)
        assertTrue(board is BoardWin && (board as BoardWin).winner == Player.W)
    }

}
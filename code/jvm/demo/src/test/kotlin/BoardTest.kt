import com.example.demo.domain.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testng.Assert

class BoardTest {

    private lateinit var board: Board

    @BeforeEach
    fun setupBoard() {
        board = createBoard(Player.W)

    }

    @Test
    fun `Game win`() {
        board = board.play(Position(0.indexToRow(), 0.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 0.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 1.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 1.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 2.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 2.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 3.indexToColumn()), Player.W)
        board = board.play(Position(1.indexToRow(), 3.indexToColumn()), Player.B)
        board = board.play(Position(0.indexToRow(), 4.indexToColumn()), Player.W)
        Assert.assertTrue(board is BoardWin)
    }

    @Test
    fun `Game draw`() {
        /*board = board.play(Position(0.indexToRow(), 0.indexToColumn()), Player.WHITE)
        board = board.play(Position(0.indexToRow(), 1.indexToColumn()), Player.BLACK)
        board = board.play(Position(0.indexToRow(), 2.indexToColumn()), Player.WHITE)
        board = board.play(Position(0.indexToRow(), 3.indexToColumn()), Player.BLACK)
        board = board.play(Position(0.indexToRow(), 4.indexToColumn()), Player.WHITE)

        board = board.play(Position(1.indexToRow(), 0.indexToColumn()), Player.BLACK)
        board = board.play(Position(1.indexToRow(), 1.indexToColumn()), Player.WHITE)
        board = board.play(Position(1.indexToRow(), 2.indexToColumn()), Player.BLACK)
        board = board.play(Position(1.indexToRow(), 3.indexToColumn()), Player.WHITE)
        board = board.play(Position(1.indexToRow(), 4.indexToColumn()), Player.BLACK)

        board = board.play(Position(2.indexToRow(), 0.indexToColumn()), Player.WHITE)
        board = board.play(Position(2.indexToRow(), 1.indexToColumn()), Player.BLACK)
        board = board.play(Position(2.indexToRow(), 2.indexToColumn()), Player.WHITE)
        board = board.play(Position(2.indexToRow(), 3.indexToColumn()), Player.BLACK)
        board = board.play(Position(2.indexToRow(), 4.indexToColumn()), Player.WHITE)

        board = board.play(Position(3.indexToRow(), 1.indexToColumn()), Player.BLACK)
        board = board.play(Position(3.indexToRow(), 0.indexToColumn()), Player.WHITE)
        board = board.play(Position(3.indexToRow(), 3.indexToColumn()), Player.BLACK)
        board = board.play(Position(3.indexToRow(), 3.indexToColumn()), Player.WHITE)
        board = board.play(Position(3.indexToRow(), 4.indexToColumn()), Player.BLACK)

        board = board.play(Position(4.indexToRow(), 0.indexToColumn()), Player.WHITE)
        board = board.play(Position(4.indexToRow(), 1.indexToColumn()), Player.BLACK)
        board = board.play(Position(4.indexToRow(), 2.indexToColumn()), Player.WHITE)
        board = board.play(Position(4.indexToRow(), 3.indexToColumn()), Player.BLACK)
        board = board.play(Position(4.indexToRow(), 4.indexToColumn()), Player.WHITE)

        Assert.assertTrue( board is BoardDraw)*/

    }
    


}
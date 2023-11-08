import com.example.demo.domain.*
import org.junit.jupiter.api.Test
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import kotlin.math.pow

class BoardTest {

    private lateinit var board: Board
    private lateinit var centralPosition: Position
    private val initialPosition = Position(0, 0)

    private fun setupBoard(rules: String = "Default", variant: String = "Freestyle") {
        board = createBoard(Player.B, 15, rules, variant)
        centralPosition = getCentralPosition(board.size)
    }

    /** Default opening rules and Freestyle variant */
    @Test
    fun `Make a play with default opening rules`() {
        setupBoard()
        board = board.play(initialPosition, Player.B)
        assertEquals(board.moves[initialPosition], Player.B)
    }

    @Test
    fun `Make a wrong play with default opening rules`() {
        //TODO()
    }

    @Test
    fun `Playing until player B wins with default opening rules`() {
        setupBoard()
        for (i in 0..4) {
            board = board.play(Position(0.indexToRow(), (4 - i).indexToColumn()), Player.B)
            if (i < 4) board = board.play(Position((4 - i).indexToRow(), 0.indexToColumn()), Player.W)
        }
        assertTrue(board is BoardWin)
        assertEquals((board as BoardWin).winner, Player.B)
    }

    @Test
    fun `Playing until there's a draw with default opening rules`() {
        //TODO("The way it's done one player wins before playing the entire game")
        setupBoard()
        val maxMoves = (board.size + 1).toDouble().pow(2)
        for (row in 0 until board.size) {
            for (col in 0 until board.size) {
                board = board.play(Position(row, col), if ((row + col) % 2 == 0) Player.B else Player.W)
            }
        }
        assertEquals(board.moves.size.toDouble(), maxMoves)
        assertTrue(board is BoardDraw)
    }

    /** Pro opening rules and Freestyle variant*/
    @Test
    fun `Make a play with Pro rules`() {
        setupBoard("Pro")
        board = board.play(centralPosition, Player.B)
        assertEquals(board.moves[centralPosition], Player.B)
    }

    @Test
    fun `Make a wrong starting play with Pro rules`() {
        setupBoard("Pro")
        board = board.play(initialPosition, Player.B)
        assertTrue(board.moves.isEmpty())
    }

    @Test
    fun `Make 3 plays`() {
        setupBoard("Pro")
        board = board.play(centralPosition, Player.B)
        board = board.play(initialPosition, Player.W)
        val nearbyPosition = Position(centralPosition.row.index, centralPosition.col.index + 4)
        board = board.play(nearbyPosition, Player.B)
        assertTrue(board.moves.size == 3)
        assertEquals(board.moves[centralPosition], Player.B)
        assertEquals(board.moves[initialPosition], Player.W)
        assertEquals(board.moves[nearbyPosition], Player.B)
    }

    @Test
    fun `Make a wrong third play`() {
        setupBoard("Pro")
        board = board.play(centralPosition, Player.B)
        board = board.play(initialPosition, Player.W)
        val wrongPosition = Position(centralPosition.row.index, centralPosition.col.index + 1)
        board = board.play(wrongPosition, Player.W)
        assertTrue(board.moves.size == 2)
        assertEquals(board.moves[centralPosition], Player.B)
        assertEquals(board.moves[initialPosition], Player.W)
    }

    /** General Tests*/

    @Test
    fun `Playing after the game ended does not change the game`() {
        setupBoard()
        for (i in 0..4) {
            board = board.play(Position(0.indexToRow(), (4 - i).indexToColumn()), Player.B)
            board = board.play(Position((4 - i).indexToRow(), 0.indexToColumn()), Player.W)
        }
        assertEquals(board.moves.size, 9)
    }
}
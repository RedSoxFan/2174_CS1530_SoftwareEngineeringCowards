import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;

import java.util.LinkedList;

import org.junit.Test;

public class BoardTest {
  /**
    Smoke test that board positions are in the correct start state on init.
   */
  @Test
  public void initPositionsTest() {

    try {
      Board board = new Board();
      assertEquals(Board.GridSquareState.EMPTY, board.square(0, 0));
      assertEquals(Board.GridSquareState.KING, board.square(5, 5));
      assertEquals(Board.GridSquareState.ATTACKER, board.square(5, 1));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Test that BadAsciiBoardFormatExceptions are thrown on null inputs.
   */
  @Test
  public void boardFromCharLoadNullTest() {
    // Null.
    char[][] badBoard = null;

    try {
      new Board(badBoard);
    } catch (BadAsciiBoardFormatException exception) {
      return;
    }

    fail();
  }

  /**
    Test that custom boards are loaded appropriately.
   */
  @Test
  public void boardFromCharLoadTest() {
    try {
      // Note that this isn't a valid configuration, but that isn't this
      // method's problem.
      Board board = new Board(new char[][]{
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'},
        {'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K'}
      });

      // Check that the board was loaded in correctly.
      for (int i = 0; i < 11; ++i) {
        for (int j = 0; j < 11; ++j) {
          assertEquals(Board.GridSquareState.KING, board.square(i, j));
        }
      }
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test that BadAsciiBoardFormatExceptions are thrown appropriately.
   */
  @Test
  public void badBoardFormatTest() {
    // This board is invalid because it doesn't fill the board properly.
    try {
      new Board(new char[][]{
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {'A', ' ', ' '},
        {'A', ' ', ' '},
        {'A', 'A', ' '},
        {'A', ' ', ' '},
        {'A', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '}
      });
    } catch (BadAsciiBoardFormatException exception) {
      // Pass.
      return;
    }

    fail();
  }

  /**
    Test that GridOutOfBoundsExceptions isn't thrown within normal parameters.
   */
  @Test
  public void boardGridNotOutOfBoundsTest() {
    // Make sure the valid edge case passes.
    try {
      Board board = new Board();
      board.square(Board.GRID_ROW_MAX, Board.GRID_COL_MAX);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Test that GridOutOfBoundsExceptions is thrown for the lower bound.
   */
  @Test
  public void boardGridOutOfBoundsLowerTest() {
    // Any call under 0 is bad.
    Board board = null;
    try {
      board = new Board();
      board.square(-1, 0);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      try {
        board.square(0, -1);
      } catch (GridOutOfBoundsException nestedException) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
    Test that GridOutOfBoundsExceptions is thrown for the upper bound.
   */
  @Test
  public void boardGridOutOfBoundsUpperTest() {
    Board board = null;
    // Make sure the valid edge case passes.
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }

    // Any call over the max is bad.
    try {
      board.square(Board.GRID_ROW_MAX + 1, 0);
    } catch (GridOutOfBoundsException exception) {
      try {
        board.square(0, Board.GRID_COL_MAX + 1);
      } catch (GridOutOfBoundsException nestedException) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
    Test gameOver getters and setters.
   */
  @Test
  public void gameOverTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }

    // Check for appropriate start state.
    assertFalse(board.isGameOver());

    // Try setting the state.
    board.setGameOver(true);
    assertTrue(board.isGameOver());
  }

  /**
    Test attackerTurn getters and setters.
   */
  @Test
  public void attackerTurnTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }

    // Check for appropriate start state.
    assertTrue(board.isAttackerTurn());

    // Try setting the state.
    board.setAttackerTurn(false);
    assertFalse(board.isAttackerTurn());
  }

  /**
    Helper method to cut down on duplication.
   */
  private void doCopyTestMoves(Board board) throws GridOutOfBoundsException {
    board.select(5, 9);
    board.move(5, 8);

    board.select(5, 7);
    board.move(4, 7);

    board.select(7, 10);
    board.move(8, 10);

    board.select(5, 6);
    board.move(5, 7);

    board.select(8, 10);
    board.move(9, 10);

    board.select(5, 5);
    board.move(5, 6);
  }

  /**
    Test that the copy constructor copies all of the state correctly.
   */
  @Test
  public void copyTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }

    try {
      /* Move just enough to properly test the copy constructor. */
      doCopyTestMoves(board);

      Board copy = new Board(board, false);

      /* Check all state that should be copied. */

      /* Spot check board config. */
      assertTrue(copy.square(5, 6).isKing());
      assertTrue(copy.square(9, 10).isAttacking());

      /* Make sure the attacker and defender move lists are preserved. */
      LinkedList<int []> att = copy.getAttMoves();
      LinkedList<int []> attOrig = board.getAttMoves();
      for (int i = 0; i < attOrig.size(); ++i) {
        assertArrayEquals(attOrig.get(i), att.get(i));
      }

      LinkedList<int []> def = copy.getAttMoves();
      LinkedList<int []> defOrig = board.getAttMoves();
      for (int i = 0; i < defOrig.size(); ++i) {
        assertArrayEquals(defOrig.get(i), def.get(i));
      }

      /* Check the rest of the board state. */
      assertEquals(6, copy.getMovesWoCapture());
      assertEquals(5, copy.getKingRow());
      assertEquals(6, copy.getKingCol());
      assertTrue(copy.isAttackerTurn());
      assertFalse(copy.isGameOver());
    } catch (GridOutOfBoundsException ex) {
      fail();
    }
  }

  /**
    Make sure that the state between a copy and original are completely
    unlinked.
   */
  @Test
  public void deepCopyTest() {
    try {
      Board board = new Board();
      Board copy  = new Board(board, false);
      doCopyTestMoves(board);

      assertFalse(copy.square(5, 6).isKing());
      assertFalse(copy.square(9, 10).isAttacking());
      assertEquals(0, copy.getAttMoves().size());
      assertEquals(0, copy.getDefMoves().size());
      assertEquals(0, copy.getMovesWoCapture());
      assertEquals(5, copy.getKingRow());
      assertEquals(5, copy.getKingCol());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      fail();
    }
  }

  /**
    Make sure the attacker moves are retrieved properly.
   */
  @Test
  public void getMovesAttTest() {
    try {
      Board board = new Board();
      board.select(5, 1);
      board.move(5, 2);
      int[] ans = board.getAttMoves().get(0);

      assertEquals(5, ans[0]);
      assertEquals(2, ans[1]);
    } catch (GridOutOfBoundsException gx) {
      fail();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Make sure the attacker moves are retrieved properly.
   */
  @Test
  public void getMovesDefTest() {
    try {
      Board board = new Board();
      board.setAttackerTurn(false);
      board.select(5, 3);
      board.move(5, 2);
      int[] ans = board.getDefMoves().get(0);

      assertEquals(5, ans[0]);
      assertEquals(2, ans[1]);
    } catch (GridOutOfBoundsException gx) {
      fail();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Test the getAttackers() method.
   */
  @Test
  public void getAttListTest() {
    try {
      Board board = new Board();
      assertEquals(24, board.getAttackers().size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Test the getDefenders() method.
   */
  @Test
  public void getDefListTest() {
    try {
      Board board = new Board();
      assertEquals(13, board.getDefenders().size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }
}

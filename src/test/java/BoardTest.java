import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;

import org.junit.Test;

public class BoardTest {
  /**
    Smoke test that board positions are in the correct start state on init.
   */
  @Test
  public void initPositionsTest() {
    Board board = new Board();

    try {
      assertEquals(Board.GridSquareState.EMPTY, board.square(0, 0));
      assertEquals(Board.GridSquareState.KING, board.square(5, 5));
      assertEquals(Board.GridSquareState.ATTACKER, board.square(5, 1));
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test that BadAsciiBoardFormatExceptions are thrown appropriately.
   */
  @Test
  public void boardFromCharLoadTest() {
    Board board = new Board();

    // Note that this isn't a valid configuration, but that isn't this method's
    // problem.
    char[][] badBoard = new char[][]{
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
    };
    try {
      board.loadBoardFromChar(badBoard);
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }

    // Check that the board was loaded in correctly.
    try {
      for (int i = 0; i < 11; ++i) {
        for (int j = 0; j < 11; ++j) {
          assertEquals(Board.GridSquareState.KING, board.square(i, j));
        }
      }
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test that BadAsciiBoardFormatExceptions are thrown appropriately.
   */
  @Test
  public void badBoardFormatTest() {
    Board board = new Board();

    // This board is bad because it doesn't fill the board properly.
    char[][] badBoard = new char[][]{
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
    };
    try {
      board.loadBoardFromChar(badBoard);
    } catch (BadAsciiBoardFormatException exception) {
      // pass
      return;
    }

    fail();
  }

  /**
    Test that GridOutOfBoundsExceptions are thrown appropriately.
   */
  @Test
  public void boardGridOutOfBoundsTest() {
    Board board = new Board();
    // Make sure the good edge case passes.
    try {
      board.square(Board.GRID_ROW_MAX, Board.GRID_COL_MAX);
    } catch (GridOutOfBoundsException exception) {
      fail();
    }

    // Any call over the max is bad.
    try {
      board.square(Board.GRID_ROW_MAX + 1, 0);
    } catch (GridOutOfBoundsException exception) {
      try {
        board.square(0, Board.GRID_COL_MAX + 1);
      } catch (GridOutOfBoundsException nestedException) {
        // pass
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
    Board board = new Board();

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
    Board board = new Board();

    // Check for appropriate start state.
    assertTrue(board.isAttackerTurn());

    // Try setting the state.
    board.setAttackerTurn(false);
    assertFalse(board.isAttackerTurn());
  }
}

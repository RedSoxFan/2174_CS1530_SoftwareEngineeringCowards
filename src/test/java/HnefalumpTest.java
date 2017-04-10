import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;
import cowards.Hnefalump;
import org.junit.Test;

/**
   Test methods associated with AI.
 */
public class HnefalumpTest {

  /**
    Test that the AI will capture when it is obvious.
   */
  @Test
  public void obviousCaptureTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', 'K', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      int[] move = Hnefalump.getNextMove(board, 1);
      assertEquals(8, move[0]);
      assertEquals(3, move[1]);
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test that the AI will exploit a shield wall possibility.
   */
  @Test
  public void shieldStrategyTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      int[] move = Hnefalump.getNextMove(board, 1);
      assertEquals(7, move[0]);
      assertEquals(0, move[1]);
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test that the AI will exploit a surround possibility.
   */
  @Test
  public void surroundStrategyTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', 'D', 'D', ' ', 'A', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', 'D', 'D', 'A', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', 'K', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      int[] move = Hnefalump.getNextMove(board, 1);
      assertEquals(5, move[0]);
      assertEquals(6, move[1]);
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test that the AI will detect a complex escape 2 moves ahead.
   */
  @Test
  public void twoMoveEscapeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'D', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'D', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'A', ' ', 'K', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.setAttackerTurn(false);
      int[] move = Hnefalump.getNextMove(board, 3);
      assertEquals(3, move[0]);
      assertEquals(4, move[1]);
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }
}

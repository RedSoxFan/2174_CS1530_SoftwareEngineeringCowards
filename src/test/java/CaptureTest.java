import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;
import org.junit.Test;

/**
  Test methods associated with capturing.
 */
public class CaptureTest {
  /**
   Test horizontal capture between two attackers.
   */
  @Test
  public void captureHorizontalTwoAtttackersTest() {
    try {
      Board board = new Board();

      board.select(3, 0);
      board.move(3, 4);

      board.select(5, 3);
      board.move(5, 2);

      board.select(3, 10);
      board.move(3, 6);

      assertEquals(board.square(3, 5), Board.GridSquareState.EMPTY);
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test vertical capture between two attackers.
   */
  @Test
  public void captureVerticalTwoAtttackersTest() {
    try {
      Board board = new Board();

      board.select(0, 3);
      board.move(4, 3);

      board.select(3, 5);
      board.move(2, 5);

      board.select(10, 3);
      board.move(6, 3);

      assertEquals(board.square(5, 3), Board.GridSquareState.EMPTY);
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }
  
  /**
    Test horizontal capture between two defenders.
   */
  @Test
  public void captureHorizontalTwoDefendersTest() {
    try {
      Board board = new Board();

      board.select(0, 4);
      board.move(3, 4);

      board.select(5, 3);
      board.move(3, 3);

      assertEquals(board.square(3, 4), Board.GridSquareState.EMPTY);
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test vertical capture between two defenders.
   */
  @Test
  public void captureVerticalTwoDefendersTest() {
    try {
      Board board = new Board();

      board.select(0, 3);
      board.move(4, 3);

      board.select(3, 5);
      board.move(3, 3);

      assertEquals(board.square(4, 3), Board.GridSquareState.EMPTY);
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }
  
  /**
    Test horizontal capture between a defender and king.
   */
  @Test
  public void captureHorizontalDefenderAndKingTest() {
    try {
      Board board = new Board();
      board.loadBoardFromChar(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'K', 'A', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);

      board.select(6, 7);
      board.move(5, 7);

      assertEquals(board.square(5, 6), Board.GridSquareState.EMPTY);
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test vertical capture between two defenders.
   */
  @Test
  public void captureVerticalDefenderAndKingTest() {
    try {
      Board board = new Board();
      board.loadBoardFromChar(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'K', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);

      board.select(7, 6);
      board.move(7, 5);

      assertEquals(board.square(6, 5), Board.GridSquareState.EMPTY);
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }
}

import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.BoardProcessor;
import cowards.GridOutOfBoundsException;

import java.util.*;
import org.junit.Test;

/**
   Test methods associated with selection and movement.
 */
public class BoardProcessorTest {
  /**
    Test that tooManyRepeats() finds a repeat when it should.
   */
  @Test
  public void repeatMovesTest() {
    LinkedList<int []> moves = new LinkedList<int []>();
    moves.add(new int[]{1, 2});
    moves.add(new int[]{3, 2});
    moves.add(new int[]{1, 2});
    moves.add(new int[]{3, 2});
    moves.add(new int[]{1, 2});
    moves.add(new int[]{3, 2});
    assertTrue(BoardProcessor.tooManyRepeats(moves.listIterator(0)));
  }

  /**
    Test that tooManyRepeats() doesn't find a repeat when it shouldn't.
   */
  @Test
  public void noRepeatMovesTest() {
    LinkedList<int []> moves = new LinkedList<int []>();
    moves.add(new int[]{1, 2});
    moves.add(new int[]{3, 2});
    moves.add(new int[]{1, 2});
    moves.add(new int[]{2, 2});
    moves.add(new int[]{1, 2});
    moves.add(new int[]{3, 2});
    assertFalse(BoardProcessor.tooManyRepeats(moves.listIterator(0)));
  }

  private static Board pathTestBoard() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
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
  }

  private static Board captureTestBoard() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', 'A', 'K', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
    Check to see if isPathClear() finds clear paths correctly.

    Tests isPathClear() directly (see MovementTest.java for other tests).
   */
  @Test
  public void pathClearTest() {
    try {
      assertTrue(BoardProcessor.isPathClear(pathTestBoard(), 8, 3, 8, 4));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isPathClear() finds obstructed paths correctly.

    Tests isPathClear() directly (see MovementTest.java for other tests).
   */
  @Test
  public void pathNotClearTest() {
    try {
      assertFalse(BoardProcessor.isPathClear(pathTestBoard(), 8, 1, 8, 4));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if kingCapture() detects a non capture move correctly.

    Tests kingCapture() directly (see CaptureTest.java for other tests).
   */
  @Test
  public void notKingCaptureTest() {
    try {
      assertFalse(BoardProcessor.kingCapture(pathTestBoard()));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if kingCapture() detects a capture move correctly.

    Tests kingCapture() directly (see CaptureTest.java for other tests).
   */
  @Test
  public void kingCaptureTest() {
    try {
      assertTrue(BoardProcessor.kingCapture(captureTestBoard()));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isValidMove() detects a valid move correctly.

    Tests isValidMove() directly (see MovementTest.java for other tests).
   */
  @Test
  public void validMoveTest() {
    try {
      Board board = pathTestBoard();
      assertTrue(BoardProcessor.isValidMove(board, 8, 3, 8, 4));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isValidMove() detects an invalid move correctly.

    Tests isValidMove() directly (see MovementTest.java for other tests).
   */
  @Test
  public void invalidMoveTest() {
    try {
      Board board = pathTestBoard();
      assertFalse(BoardProcessor.isValidMove(board, 8, 1, 8, 4));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isValidMove() throws when the invalid move is out of bounds.

    Tests isValidMove() directly (see MovementTest.java for other tests).
   */
  @Test
  public void outOfBoundsInvalidMoveTest() {
    try {
      Board board = pathTestBoard();
      BoardProcessor.isValidMove(board, -1, 1, 8, 4);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      return;
    }
  }

  /**
    Create a diamond board to use for testing if the defenders are surrounded
    with the exception of one piece. To complete, move (8,4) to (8,3).

    @return A diamond board.
   */
  private Board diamondBoard() throws BadAsciiBoardFormatException {
    return new Board(new char[][] {
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', 'A', 'A', 'A', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', 'A', ' ', ' ', ' ', 'A', ' ', ' ', ' '},
      {' ', ' ', 'A', ' ', ' ', 'D', ' ', ' ', 'A', ' ', ' '},
      {' ', 'A', ' ', ' ', 'D', 'D', 'D', ' ', ' ', 'A', ' '},
      {'A', 'A', ' ', 'D', 'D', 'K', 'D', 'D', ' ', 'A', 'A'},
      {' ', 'A', ' ', ' ', 'D', 'D', 'D', ' ', ' ', 'A', ' '},
      {' ', ' ', 'A', ' ', ' ', 'D', ' ', ' ', 'A', ' ', ' '},
      {' ', ' ', ' ', ' ', 'A', ' ', ' ', 'A', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', 'A', 'A', 'A', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
    });
  }

  /**
    Check to see if isSurrounded() returned false on the starting board.
   */
  @Test
  public void surroundedStartingTest() {
    try {
      Board board = new Board();
      assertFalse(BoardProcessor.isSurrounded(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Check to see if isSurrounded() returns true when surrounded in a diamond.
   */
  @Test
  public void surroundedDiamondTest() {
    try {
      Board board = diamondBoard();
      board.select(8, 4);
      board.move(8, 3);
      assertTrue(BoardProcessor.isSurrounded(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isSurrounded() returns false when one defender is outside
    a diamond.
   */
  @Test
  public void surroundedDiamondWithOutlierTest() {
    try {
      Board board = diamondBoard();
      board.setAttackerTurn(false);
      board.select(5, 3);
      board.move(10, 3);
      board.select(8, 4);
      board.move(8, 3);
      assertFalse(BoardProcessor.isSurrounded(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if isSurrounded() results in a game over when surrounded by a
    diamond.
   */
  @Test
  public void surroundedDiamondGameOverTest() {
    try {
      Board board = diamondBoard();
      board.select(8, 4);
      board.move(8, 3);
      assertTrue(board.isGameOver());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }
}

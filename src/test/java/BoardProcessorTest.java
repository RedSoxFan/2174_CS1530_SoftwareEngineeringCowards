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

  /**
    Check to see if areMovesAvailable() results in true for the starting board for
    the attacking side.
   */
  @Test
  public void movesStartingAttackerTest() {
    try {
      Board board = new Board();
      assertTrue(BoardProcessor.areMovesAvailable(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Check to see if areMovesAvailable() results in true for the starting board for
    the defending side.
   */
  @Test
  public void movesStartingDefenderTest() {
    try {
      Board board = new Board();
      board.setAttackerTurn(false);
      assertTrue(BoardProcessor.areMovesAvailable(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Check to see if areMovesAvailable() results in false and if the defenders cannot move.
   */
  @Test
  public void movesDefenderNoneTest() {
    try {
      Board board = new Board(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'A', 'K', 'A', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.select(9, 4);
      board.move(9, 3);
      assertTrue(board.isGameOver());
      board.setAttackerTurn(false);
      assertFalse(BoardProcessor.areMovesAvailable(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if areMovesAvailable() results in false and if the attackers cannot move.
   */
  @Test
  public void movesAttackerNoneTest() {
    try {
      Board board = new Board(new char[][] {
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'K', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', 'A', 'D', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);
      board.select(9, 4);
      board.move(9, 3);
      assertTrue(board.isGameOver());
      board.setAttackerTurn(true);
      assertFalse(BoardProcessor.areMovesAvailable(board));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Return the defensive position of the bottom most defender moved down one from
    the starting board.
   */
  private String getDefendingKey() {
    return ("00000000000"
          + "00000000000"
          + "00000000000"
          + "00000100000"
          + "00001110000"
          + "00011111000"
          + "00001110000"
          + "00000000000"
          + "00000100000"
          + "00000000000"
          + "00000000000");
  }

  /**
    Check to see if the defensive board position is being not stored after an attacker
    move.
   */
  @Test
  public void defensiveStoreAttackingTest() {
    try {
      Board board = new Board();
      assertEquals(0, board.getDefensiveBoardPositions().size());
      board.select(9, 5);
      board.move(8, 5);
      assertEquals(0, board.getDefensiveBoardPositions().size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if the defensive board position is being stored after a defender
    move.
   */
  @Test
  public void defensiveStoreDefendingTest() {
    try {
      Board board = new Board();
      assertEquals(0, board.getDefensiveBoardPositions().size());
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);
      assertEquals(1, board.getDefensiveBoardPositions().size());
      String key = getDefendingKey();
      assertTrue(board.getDefensiveBoardPositions().containsKey(key));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if the defensive board position is being updated to reflect the
    correct count.
   */
  @Test
  public void defensiveStoreUpdateTest() {
    try {
      Board board = new Board();
      final String key = getDefendingKey();

      // First move.
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);
      assertEquals(1, board.getDefensiveBoardPositions().size());
      assertEquals(new Integer(1), board.getDefensiveBoardPositions().get(key));

      // Second move.
      board.setAttackerTurn(false);
      board.select(8, 5);
      board.move(7, 5);
      assertEquals(2, board.getDefensiveBoardPositions().size());

      // Third move.
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);
      assertEquals(2, board.getDefensiveBoardPositions().size());
      assertEquals(new Integer(2), board.getDefensiveBoardPositions().get(key));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if the defensive board positions get cleared if there is a capture.
   */
  @Test
  public void defensiveStoreRemovalTest() {
    try {
      Board board = new Board();

      // Defending move 1.
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);
      assertEquals(1, board.getDefensiveBoardPositions().size());

      // Attacking move 1.
      board.select(10, 6);
      board.move(8, 6);
      assertEquals(1, board.getDefensiveBoardPositions().size());

      // Defending move 2.
      board.select(5, 7);
      board.move(8, 7);
      assertEquals(0, board.getDefensiveBoardPositions().size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Check to see if the game ends if the same defensive board position is
    repeated three times without a capture.
   */
  @Test
  public void defensiveStoreGameOverTest() {
    try {
      Board board = new Board();

      // Move. The count is now one.
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);

      // Move up.
      board.setAttackerTurn(false);
      board.select(8, 5);
      board.move(7, 5);

      // Move back. The count is now two.
      board.setAttackerTurn(false);
      board.select(7, 5);
      board.move(8, 5);

      // Move right.
      board.setAttackerTurn(false);
      board.select(8, 5);
      board.move(8, 6);

      // Move back. The count is now three.
      board.setAttackerTurn(false);
      board.select(8, 6);
      board.move(8, 5);

      // Verify that the game is over and attacker's win.
      assertTrue(board.isGameOver());
      assertTrue(board.isAttackerTurn());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }
}

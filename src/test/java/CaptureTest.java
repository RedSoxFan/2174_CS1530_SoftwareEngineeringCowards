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

      assertEquals(Board.GridSquareState.EMPTY, board.square(3, 5));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
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

      assertEquals(Board.GridSquareState.EMPTY, board.square(5, 3));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
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

      assertEquals(Board.GridSquareState.EMPTY, board.square(3, 4));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
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

      assertEquals(Board.GridSquareState.EMPTY, board.square(4, 3));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
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
      Board board = new Board(new char[][]{
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

      assertEquals(Board.GridSquareState.EMPTY, board.square(5, 6));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test vertical capture between a defender and a king.
   */
  @Test
  public void captureVerticalDefenderAndKingTest() {
    try {
      Board board = new Board(new char[][]{
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

      assertEquals(Board.GridSquareState.EMPTY, board.square(6, 5));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
     Test horizontal capture between an attacker and a special square.
   */
  @Test
  public void captureHorizontalAttackerAndSpecialTest() {
    try {
      Board board = new Board();

      board.select(5, 9);
      board.move(5, 8);

      board.select(7, 5);
      board.move(7, 9);

      board.move(5, 8);
      board.move(9, 8);

      board.select(7, 9);
      board.move(10, 9);

      board.select(9, 8);
      board.move(10, 8);

      assertEquals(Board.GridSquareState.EMPTY, board.square(10, 9));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
     Test vertical capture between an attacker and a special square.
   */
  @Test
  public void captureVerticalAttackerAndSpecialTest() {
    try {
      Board board = new Board();

      board.select(9, 5);
      board.move(8, 5);

      board.select(5, 7);
      board.move(9, 7);

      board.move(8, 5);
      board.move(8, 9);

      board.select(9, 7);
      board.move(9, 10);

      board.select(8, 9);
      board.move(8, 10);

      assertEquals(Board.GridSquareState.EMPTY, board.square(9, 10));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
     Test horizontal capture between a defender and a special square.
   */
  @Test
  public void captureHorizontalDefenderAndSpecialTest() {
    try {
      Board board = new Board();

      board.select(5, 9);
      board.move(10, 9);

      board.select(7, 5);
      board.move(7, 8);

      board.select(3, 10);
      board.move(2, 10);

      board.select(7, 8);
      board.move(10, 8);

      assertEquals(Board.GridSquareState.EMPTY, board.square(10, 9));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
     Test vertical capture between a defender and a special square.
   */
  @Test
  public void captureVerticalDefenderAndSpecialTest() {
    try {
      Board board = new Board();

      board.select(9, 5);
      board.move(9, 10);

      board.select(5, 7);
      board.move(8, 7);

      board.select(10, 3);
      board.move(10, 2);

      board.select(8, 7);
      board.move(8, 10);

      assertEquals(Board.GridSquareState.EMPTY, board.square(9, 10));
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }
 
  /**
    Test horizontal capture between a king and a special square.
   */
  @Test
  public void captureHorizontalKingAndSpecialTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);

      board.select(1, 2);
      board.move(0, 2);

      assertEquals(Board.GridSquareState.EMPTY, board.square(0, 1));
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
  public void captureVerticalKingAndSpecialTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);

      board.select(2, 1);
      board.move(2, 0);

      assertEquals(Board.GridSquareState.EMPTY, board.square(1, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure two attackers cannot horizontally capture
    a king.
   */
  @Test
  public void noCaptureKingHorizontalTwoAttackersTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', 'A', 'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.select(1, 3);
      board.move(0, 3);

      assertEquals(Board.GridSquareState.KING, board.square(0, 2));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure two attackers cannot vertically capture
    a king.
   */
  @Test
  public void noCaptureKingVerticalTwoAttackersTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.select(3, 1);
      board.move(3, 0);

      assertEquals(Board.GridSquareState.KING, board.square(2, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure an attacker and a special square cannot horizontally
    capture a king.
   */
  @Test
  public void noCaptureKingHorizontalAttackerAndSpecialTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.select(2, 1);
      board.move(2, 0);

      assertEquals(Board.GridSquareState.KING, board.square(1, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure an attacker and a special square cannot vertically
    capture a king.
   */
  @Test
  public void noCaptureKingVerticalAttackerAndSpecialTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.select(2, 1);
      board.move(2, 0);

      assertEquals(Board.GridSquareState.KING, board.square(1, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure an attacker can pass through two defenders.
   */
  @Test
  public void noCaptureAttackerPassthroughTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.select(2, 1);
      board.move(2, 0);

      assertEquals(Board.GridSquareState.ATTACKER, board.square(2, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure a defender can pass through two attackers.
   */
  @Test
  public void noCaptureDefenderPassthroughTest() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(false);

      board.select(2, 1);
      board.move(2, 0);

      assertEquals(Board.GridSquareState.DEFENDER, board.square(2, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }
  
  /**
    Test to make sure the king can be captured.
   */
  @Test
  public void captureKingBasic() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'A', 'K', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(true);

      board.select(2, 3);
      board.move(2, 2);

      assertEquals(Board.GridSquareState.EMPTY, board.square(2, 1));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }

  /**
    Test to make sure the king can be captured at the throne.
   */
  @Test
  public void captureKingAtThrone() {
    try {
      Board board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', 'K', ' ', 'A', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });
      board.setAttackerTurn(true);

      board.select(5, 7);
      board.move(5, 6);

      assertEquals(Board.GridSquareState.EMPTY, board.square(5, 5));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException exception) {
      fail();
    }
  }  
}

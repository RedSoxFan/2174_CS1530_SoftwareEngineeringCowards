import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;
import cowards.Minimax;
import cowards.Node;
import java.util.*;
import org.junit.Test;

/**
   Test methods associated with minimax tree.
 */
public class MinimaxTest {

  /**
   * Retrieve a board with only one king, sitting between two corners.
   */
  private Board getLeftMiddleKing() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', 'D', ' ', ' ', 'D', ' ', ' ', ' ', ' '},
      {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
   * Retrieve a board with only one king, sitting close to a corner.
   */
  private Board getCornerKing() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'K', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
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
  }

  /**
   * Retrieve a board with just a King in the center.
   */
  private Board getLonelyKing() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'K', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
   * Retrieve a board with just a King that could win the game.
   */
  private Board getNearSurroundedKing() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'K', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
   * Retrieve a board with just a King that could win the game.
   */
  private Board getNearSurroundedKing2() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'K', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /** Retrieve a board in a next move capture scenario. */
  private Board getNextMoveCapture() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', 'A', 'K', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
    Tests if the kingBestCorner() calculation works when the king is between
    all of the corners.
   */
  @Test
  public void bestCornerMiddleTest() {
    try {
      Board board = new Board();
      // 7.071 = sqrt(50)
      assertEquals(7.071, Minimax.kingBestCorner(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the kingBestCorner() calculation works when the king is between
    two of the corners.
   */
  @Test
  public void bestCornerLeftMiddleTest() {
    try {
      Board board = getLeftMiddleKing();
      assertEquals(5, Minimax.kingBestCorner(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the kingBestCorner() calculation works when the king is 
    clearly closest to one corner.
   */
  @Test
  public void bestCornerObviousTest() {
    try {
      Board board = getCornerKing();
      assertEquals(1, Minimax.kingBestCorner(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the utility() calculation works when the king is 
    clearly closest to one corner, and only one attacker remains.
   */
  @Test
  public void utilityCornerSimpleTest() {
    try {
      Board board = getCornerKing();
      assertEquals(6.071, Minimax.utility(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the utility() calculation works when the king is 
    clearly closest to one corner, and only defenders remain.
   */
  @Test
  public void utilityDefenderHeavyTest() {
    try {
      Board board = getLeftMiddleKing();
      assertEquals(6.071, Minimax.utility(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the utility() calculation works when the board is 
    in its start state.
   */
  @Test
  public void utilityStartTest() {
    try {
      Board board = new Board();
      assertEquals(-11, Minimax.utility(board), .001);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests if the startV() returns the right starting V for the max.
   */
  @Test
  public void startVeeMaxTest() {
    assertEquals(Double.NEGATIVE_INFINITY, Minimax.startV(true).utility(), .001);
  }

  /**
    Tests if the startV() returns the right starting V for the min.
   */
  @Test
  public void startVeeMinTest() {
    assertEquals(Double.POSITIVE_INFINITY, Minimax.startV(false).utility(), .001);
  }

  /**
    Tests that the expandMove() method adds the move specified.
   */
  @Test
  public void expandMoveGoodTest() {
    try {
      Board board = getLonelyKing();
      board.setAttackerTurn(false);

      Node node = new Node(board, null, 0);
      LinkedList<Node> store = new LinkedList<Node>();
      Minimax.expandMove(node, new Board(board, false), store, new int [] {5, 5}, 5, 0);
      assertEquals(1, store.size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Tests that the expandMove() method doesn't add the invalid move specified.
   */
  @Test
  public void expandMoveBadTest() {
    try {
      Board board = getLonelyKing();
      board.setAttackerTurn(false);

      Node node = new Node(board, null, 0);
      LinkedList<Node> store = new LinkedList<Node>();
      Minimax.expandMove(node, new Board(board, false), store, new int [] {5, 5}, 0, 0);
      assertEquals(0, store.size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    } catch (GridOutOfBoundsException gx) {
      fail();
    }
  }

  /**
    Tests that the expand() method creates the right number of children for a
    simple move set. Where a simple move set is just moves for one piece.
   */
  @Test
  public void expandSimpleTest() {
    try {
      Board board = getLonelyKing();
      board.setAttackerTurn(false);
      Node node = new Node(board, null, 0);
      assertEquals(20, Minimax.expand(node).size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the expand() method creates the right number of children for a
    move set when there are two opposing pieces on the board.

    There should only be the possible number of moves for the king returned.
   */
  @Test
  public void expandOpposeTest() {
    try {
      Board board = getCornerKing();
      board.setAttackerTurn(false);
      Node node = new Node(board, null, 5);
      assertEquals(14, Minimax.expand(node).size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the expand() method creates the right number of children for a
    move set where an attacker is moving through the throne.

    There should only be the possible number of moves for the king returned.
   */
  @Test
  public void expandThroughThroneAttackerTest() {
    try {
      Board board = getCornerKing();
      Node node = new Node(board, null, 0);
      assertEquals(18, Minimax.expand(node).size());
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the minimaxDescision() method guides the attackers to capture
    the King.
   */
  @Test
  public void captureOneLookTest() {
    try {
      Board board = getNextMoveCapture();
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 1);
      int[] move = result.getMove();
      assertEquals(5, move[0]);
      assertEquals(3, move[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /*
    THESE AREN'T IMPLEMENTED YET. When the no moves rule is implemented readd this.
  /**
    Tests that the minimaxDescision() method guides the attackers to capture
    the King.
  @Test
  public void captureOneLookTest() {
    try {
      Board board = getNearSurroundedKing();
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 1);
      int[] move = result.getMove();
      assertEquals(4, move[0]);
      assertEquals(0, move[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the minimaxDescision() method guides the attackers to capture
    the King.
  @Test
  public void secondCaptureOneLookTest() {
    try {
      Board board = getNearSurroundedKing2();
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 1);
      int[] move = result.getMove();
      assertEquals(6, move[0]);
      assertEquals(0, move[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }
   */

  /**
    Tests that the minimaxDescision() method guides the attackers to capture
    the King when looking ahead 5 moves.
   */
  @Test
  public void captureFiveLookTest() {
    try {
      Board board = getNearSurroundedKing();
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 5);
      int[] move = result.getMove();
      assertEquals(4, move[0]);
      assertEquals(0, move[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the minimaxDecision() method guides the King into a corner when
    initially in a tight spot, and when the lookahead is just 1 move.
   */
  @Test
  public void escapeCaptureOneLookTest() {
    try {
      Board board = getNearSurroundedKing();
      board.setAttackerTurn(false);
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 1);
      assertEquals(0, result.getMove()[0]);
      assertEquals(0, result.getMove()[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }

  /**
    Tests that the minimaxDecision() method guides the King into a corner when
    initially in a tight spot, and when the lookahead is 5 moves.
   */
  @Test
  public void escapeCaptureFiveLookTest() {
    try {
      Board board = getNearSurroundedKing();
      board.setAttackerTurn(false);
      Node node = new Node(board, null, 0);
      Node result = Minimax.minimaxDecision(node, 5);
      assertEquals(0, result.getMove()[0]);
      assertEquals(0, result.getMove()[1]);
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }
  }
}

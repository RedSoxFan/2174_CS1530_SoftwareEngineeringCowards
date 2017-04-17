import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.GridOutOfBoundsException;
import org.junit.Test;

/**
   Test methods associated with selection and movement.
 */
public class MovementTest {

  /**
     Verify there is no selection when the game starts.
   */
  @Test
  public void noStartingSelectionTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    assertFalse(board.hasSelection());
  }

  /**
     Verify the attacking side can select an attacker.
   */
  @Test
  public void selectAttackerAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(5, 0);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertTrue(board.hasSelection());
  }

  /**
     Verify the selected row is reported correctly.
   */
  @Test
  public void selectGetSelectedRowTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(5, 0);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertEquals(5, board.getSelectedRow());
  }

  /**
     Verify the selected column is reported correctly.
   */
  @Test
  public void selectGetSelectedColumnTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(5, 10);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertEquals(10, board.getSelectedColumn());
  }

  /**
     Verify the attacking side cannot select an attacker.
   */
  @Test
  public void selectDefenderAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(5, 4);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertFalse(board.hasSelection());
  }

  /**
     Verify the attacking side cannot select the king.
   */
  @Test
  public void selectKingAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(5, 5);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertFalse(board.hasSelection());
  }

  /**
     Verify the attacking side cannot select an empty square.
   */
  @Test
  public void selectEmptyAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 0);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertFalse(board.hasSelection());
  }

  /**
     Verify the attacking side cannot select a piece above or to the left
     of the board.
   */
  @Test
  public void selectLowerBoundAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(-1, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, -1);
      } catch (GridOutOfBoundsException ex2) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
     Verify the attacking side cannot select a piece below or to the
     right of the board.
   */
  @Test
  public void selectUpperBoundAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(11, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, 11);
      } catch (GridOutOfBoundsException ex2) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
     Verify the defending side cannot select an attacker.
   */
  @Test
  public void selectAttackerAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 0);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertFalse(board.hasSelection());
  }

  /**
     Verify the defending side can select a defender.
   */
  @Test
  public void selectDefenderAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 4);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertTrue(board.hasSelection());
  }

  /**
     Verify the defending side can select the king.
   */
  @Test
  public void selectKingAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertTrue(board.hasSelection());
  }

  /**
     Verify the defending side cannot select an empty square.
   */
  @Test
  public void selectEmptyAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(0, 0);
    } catch (GridOutOfBoundsException ex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    assertFalse(board.hasSelection());
  }

  /**
     Verify the defending side cannot select a square above or to the left
     of the board.
   */
  @Test
  public void selectLowerBoundAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(-1, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, -1);
      } catch (GridOutOfBoundsException ex2) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
     Verify the defending side cannot select a square below or to the right
     of the board.
   */
  @Test
  public void selectUpperBoundAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(11, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, 11);
      } catch (GridOutOfBoundsException ex2) {
        // Pass.
        return;
      }
    }

    fail();
  }

  /**
     Verify movement fails for the attacking side when there is no selection.
   */
  @Test
  public void moveNoSelectionAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left one square works for the attacking side.
   */
  @Test
  public void moveLeftAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(0, 2));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right one square works for the attacking side.
   */
  @Test
  public void moveRightAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 7);
      assertTrue(board.move(0, 8));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up one square works for the attacking side.
   */
  @Test
  public void moveUpAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(9, 5);
      assertTrue(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down one square works for the attacking side.
   */
  @Test
  public void moveDownAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(1, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left multiple squares works for the attacking side.
   */
  @Test
  public void moveLeftMultipleAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(1, 5);
      assertTrue(board.move(1, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right multiple squares works for the attacking side.
   */
  @Test
  public void moveRightMutipleAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(1, 5);
      assertTrue(board.move(1, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up multiple squares works for the attacking side.
   */
  @Test
  public void moveUpMulitpleAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(10, 3);
      assertTrue(board.move(7, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down multiple squares works for the attacking side.
   */
  @Test
  public void moveDownMultipleAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(3, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveLeftBarricadeAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(0, 1));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveRightBarricadeAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(0, 9));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveUpBarricadeAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(10, 5);
      assertFalse(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveDownBarricadeAsAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify movement fails for the defending side when there is no selection.
   */
  @Test
  public void moveNoSelectionAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left one square works for the defending side.
   */
  @Test
  public void moveLeftAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right one square works for the defending side.
   */
  @Test
  public void moveRightAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 6));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up one square works for the defending side.
   */
  @Test
  public void moveUpAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down one square works for the defending side.
   */
  @Test
  public void moveDownAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(7, 5);
      assertTrue(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left multiple squares works for the defending side.
   */
  @Test
  public void moveLeftMultipleAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 1));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right multiple squares works for the defending side.
   */
  @Test
  public void moveRightMutipleAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 9));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up multiple squares works for the defending side.
   */
  @Test
  public void moveUpMulitpleAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(4, 4);
      assertTrue(board.move(2, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down multiple squares works for the defending side.
   */
  @Test
  public void moveDownMultipleAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(6, 4);
      assertTrue(board.move(9, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving left fails when there is a barricade for the defending side.
   */
  @Test
  public void moveLeftBarricadeAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(5, 2));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving right fails when there is a barricade for the defending side.
   */
  @Test
  public void moveRightBarricadeAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(5, 8));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving up fails when there is a barricade for the defending side.
   */
  @Test
  public void moveUpBarricadeAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving down fails when there is a barricade for the defending side.
   */
  @Test
  public void moveDownBarricadeAsDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
   * Retrieve a board with one king and one attacker. The attacker is
   * at (8, 8) and is only needed to allow for one move to be available
   * for the attacking side to prevent a game over state.
   */
  private Board getBoardWithOneKing() throws BadAsciiBoardFormatException {
    return new Board(new char[][]{
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'A', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    });
  }

  /**
     Verify that the king can move to the uppper left corner.
  */
  @Test
  public void moveKingToTopLeft() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      assertTrue(board.move(0, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king can move to the uppper right corner.
  */
  @Test
  public void moveKingToTopRight() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(5, 10);
     
      board.setAttackerTurn(false);

      board.select(5, 10);
      assertTrue(board.move(0, 10));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king can move to the lower left corner.
  */
  @Test
  public void moveKingToBottomLeft() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      assertTrue(board.move(10, 0));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king can move to the lower right corner.
  */
  @Test
  public void moveKingToBottomRight() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(5, 10);

      board.setAttackerTurn(false);

      board.select(5, 10);
      assertTrue(board.move(10, 10));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king can move to the throne.
  */
  @Test
  public void moveKingToThrone() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      assertTrue(board.move(5, 5));
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king causes a game over when moved the top left.
  */
  @Test
  public void gameKingToTopLeft() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(0, 0);

      assertTrue(board.isGameOver());
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king causes a game over when moved to the top right.
  */
  @Test
  public void gameKingToTopRight() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(5, 10);
     
      board.setAttackerTurn(false);

      board.select(5, 10);
      board.move(0, 10);

      assertTrue(board.isGameOver());
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king causes a game over when moved to the bottom left.
  */
  @Test
  public void gameKingToBottomLeft() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(10, 0);

      assertTrue(board.isGameOver());
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king causes a game over when moved to the bottom right.
  */
  @Test
  public void gameKingToBottomRight() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(5, 10);

      board.setAttackerTurn(false);

      board.select(5, 10);
      board.move(10, 10);

      assertTrue(board.isGameOver());
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify that the king does not cause a game over when moved to the throne.
  */
  @Test
  public void noGameKingToThrone() {
    try {
      Board board = getBoardWithOneKing();
      board.setAttackerTurn(false);

      board.select(5, 0);
      board.move(5, 5);

      assertFalse(board.isGameOver());
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify attacking side cannot move to upper left corner.
  */
  @Test
  public void moveAttackerToTopLeft() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertFalse(board.move(0, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify attacking side cannot move to upper right corner.
  */
  @Test
  public void moveAttackerToTopRight() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(0, 7);
      assertFalse(board.move(0, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify attacking side cannot move to lower left corner.
  */
  @Test
  public void moveAttackerToBottomLeft() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(10, 3);
      assertFalse(board.move(10, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify attacking side cannot move to lower right corner.
  */
  @Test
  public void moveAttackerToBottomRight() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(true);

    try {
      board.select(10, 7);
      assertFalse(board.move(10, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify defending side cannot move to upper left corner.
  */
  @Test
  public void moveDefenderToTopLeft() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Navigate defending piece to corner.
      board.select(3, 5);
      board.move(3, 1);
      board.setAttackerTurn(false);
      board.select(3, 1);
      board.move(0, 1);
      board.setAttackerTurn(false);
      board.select(0, 1);
      assertFalse(board.move(0, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify defending side cannot move to upper right corner.
  */
  @Test
  public void moveDefenderToTopRight() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Navigate defending piece to corner.
      board.select(3, 5);
      board.move(3, 9);
      board.setAttackerTurn(false);
      board.select(3, 9);
      board.move(0, 9);
      board.setAttackerTurn(false);
      board.select(0, 9);
      assertFalse(board.move(0, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify defending side cannot move to lower left corner.
  */
  @Test
  public void moveDefenderToBottomLeft() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Navigate defending piece to corner.
      board.select(7, 5);
      board.move(7, 1);
      board.setAttackerTurn(false);
      board.select(7, 1);
      board.move(10, 1);
      board.setAttackerTurn(false);
      board.select(10, 0);
      assertFalse(board.move(0, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify defending side cannot move to lower right corner.
  */
  @Test
  public void moveDefenderToBottomRight() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Navigate defending piece to corner.
      board.select(7, 5);
      board.move(7, 9);
      board.setAttackerTurn(false);
      board.select(7, 9);
      board.move(10, 9);
      board.setAttackerTurn(false);
      board.select(10, 9);
      assertFalse(board.move(10, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  } 

  /**
     Verify normal defending piece cannot move to throne.
  */
  @Test
  public void moveDefenderToThrone() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Navigate defending piece to throne.
      board.select(4, 6);
      board.move(4, 8);
      board.setAttackerTurn(false);
      board.select(4, 5);
      board.move(4, 6);
      board.setAttackerTurn(false);
      board.select(5, 5);
      board.move(4, 5);
      board.setAttackerTurn(false);
      board.select(5, 6);
      assertFalse(board.move(5, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }  

  /**
     Verify attacking piece cannot move to throne.
  */
  @Test
  public void moveAttackerToThrone() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    board.setAttackerTurn(false);

    try {
      // Move defenders out of the way.
      board.select(3, 5);
      board.move(3, 9);
      board.setAttackerTurn(false);
      board.select(4, 5);
      board.move(2, 5);
      board.setAttackerTurn(false);
      board.select(2, 5);
      board.move(2, 0);
      board.setAttackerTurn(false);
      board.select(5, 5);
      board.move(3, 5);
      board.setAttackerTurn(false);
      board.select(3, 5);
      board.move(3, 7);
      // Try moving attacker to throne.
      board.select(1, 5);
      assertFalse(board.move(5, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }  

  /**
     Verify moving back and forth as the defender too many times loses the game.
   */
  @Test
  public void backAndForthDefenderTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }

    try {
      // Move back and forth 3 times.
      board.setAttackerTurn(false);
      board.select(5, 7);
      assertTrue(board.move(5, 8));
      board.setAttackerTurn(false);
      board.select(5, 8);
      assertTrue(board.move(5, 7));
      board.setAttackerTurn(false);
      board.select(5, 7);
      assertTrue(board.move(5, 8));
      board.setAttackerTurn(false);
      board.select(5, 8);
      assertTrue(board.move(5, 7));
      board.setAttackerTurn(false);
      board.select(5, 7);
      assertTrue(board.move(5, 8));
      board.setAttackerTurn(false);
      board.select(5, 8);
      assertTrue(board.move(5, 7));

      // Check game over state.
      assertTrue(board.isGameOver());
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
     Verify moving back and forth as the attacker too many times loses the game.
   */
  @Test
  public void backAndForthAttackerTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }

    try {
      // Move back and forth 3 times.
      board.select(5, 9);
      assertTrue(board.move(4, 9));
      board.setAttackerTurn(true);
      board.select(4, 9);
      assertTrue(board.move(5, 9));
      board.setAttackerTurn(true);
      board.select(5, 9);
      assertTrue(board.move(4, 9));
      board.setAttackerTurn(true);
      board.select(4, 9);
      assertTrue(board.move(5, 9));
      board.setAttackerTurn(true);
      board.select(5, 9);
      assertTrue(board.move(4, 9));
      board.setAttackerTurn(true);
      board.select(4, 9);
      assertTrue(board.move(5, 9));

      // Check game over state.
      assertTrue(board.isGameOver());
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  private void do50Moves(Board b) throws GridOutOfBoundsException {
    // First move.
    b.select(5, 9);
    assertTrue(b.move(5, 8));

    int le = 7;
    int ri = 8;
    int tar = 1;
    int cur = 5;

    // Another 8 moves. Total = 9
    for (; tar < cur; --cur) {
      b.select(cur, le);
      assertTrue(b.move(cur - 1, le));
      b.select(cur, ri);
      assertTrue(b.move(cur - 1, ri));
    }

    tar = 9;
    // Another 16 moves. Total = 25
    for (; tar > cur; ++cur) {
      b.select(cur, le);
      assertTrue(b.move(cur + 1, le));
      b.select(cur, ri);
      assertTrue(b.move(cur + 1, ri));
    }

    // Move on other side to prevent repeating defensive positions.
    b.select(5, 3);
    assertTrue(b.move(5, 2));

    // Reset values.    
    le = 1;
    ri = 2;
    tar = 1;
    cur = 5;

    // Another 8 moves. Total = 34
    for (; tar < cur; --cur) {
      b.select(cur, le);
      assertTrue(b.move(cur - 1, le));
      b.select(cur, ri);
      assertTrue(b.move(cur - 1, ri));
    }

    tar = 9;
    // Another 16 moves. Total = 50
    for (; tar > cur; ++cur) {
      b.select(cur, le);
      assertTrue(b.move(cur + 1, le));
      b.select(cur, ri);
      assertTrue(b.move(cur + 1, ri));
    }
  }

  /**
     Verify 50 move draw rule work as expected.
   */
  @Test
  public void drawTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }

    try {
      // Check game over state.
      do50Moves(board);
      assertTrue(board.isGameOver());
      assertTrue(board.isDraw());
    } catch (GridOutOfBoundsException ex) {
      // This should not happen.
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // Pass.
    return;
  }

  /**
    Test that corners are properly identified by inCornerLocation().
   */
  @Test
  public void identifyCornersTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    assertTrue(board.inCornerLocation(0,0));
    assertTrue(board.inCornerLocation(10,0));
    assertTrue(board.inCornerLocation(0,10));
    assertTrue(board.inCornerLocation(10,10));
  }

  /**
    Test that special squares are properly identified by inSpecialLocation().
   */
  @Test
  public void identifySpecialTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    assertTrue(board.inSpecialLocation(0,0));
    assertTrue(board.inSpecialLocation(5,5));
    assertTrue(board.inSpecialLocation(10,0));
    assertTrue(board.inSpecialLocation(0,10));
    assertTrue(board.inSpecialLocation(10,10));
  }

  /**
    Test that isPathClear() correctly identifies when the path isn't clear when
    an attacker is immediately adjacent.
   */
  @Test
  public void pathNotClearAdjacentTest() {
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
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', 'K', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.setAttackerTurn(false);
      board.select(8,2);

      assertFalse(board.isPathClear(6,2));
      assertFalse(board.isPathClear(10,2));
      assertFalse(board.isPathClear(8,0));
      assertFalse(board.isPathClear(8,4));
    } catch (GridOutOfBoundsException gex) {
      fail();
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test that isPathClear() correctly identifies when the path isn't clear when
    an attacker is on the target square.
   */
  @Test
  public void pathNotClearOnTargetTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', 'K', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.setAttackerTurn(false);
      board.select(8,2);

      assertFalse(board.isPathClear(6,2));
      assertFalse(board.isPathClear(10,2));
      assertFalse(board.isPathClear(8,0));
      assertFalse(board.isPathClear(8,4));
    } catch (GridOutOfBoundsException gex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    } catch (BadAsciiBoardFormatException bex) {
      System.out.println("A BadAsciiBoardFormatException has been thrown");
      fail();
    }
  }

  /**
    Test that isPathClear() correctly identifies when the path is clear.
   */
  @Test
  public void pathClearTest() {
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
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      board.setAttackerTurn(false);
      board.select(8,2);

      assertTrue(board.isPathClear(6,2));
      assertTrue(board.isPathClear(10,2));
      assertTrue(board.isPathClear(8,0));
      assertTrue(board.isPathClear(8,4));
    } catch (GridOutOfBoundsException gex) {
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    } catch (BadAsciiBoardFormatException bex) {
      System.out.println("A BadAsciiBoardFormatException has been thrown");
      fail();
    }
  }

  /**
    Test to see if square is along the edge.
  */
  @Test
  public void identifyEdgesTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException exception) {
      fail();
    }
    assertEquals("Left", board.inEdgeLocation(5, 0));
    assertEquals("Bottom", board.inEdgeLocation(10, 5));
    assertEquals("Right", board.inEdgeLocation(5, 10));
    assertEquals("Top", board.inEdgeLocation(0, 5));
    assertEquals("NE", board.inEdgeLocation(5, 5));
  }

  /**
    Test to see if exit fort is detected.    
  */
  @Test
  public void exitFortBottomEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', 'K', 'D', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Another test for detecting a valid exit fort.
  */ 
  @Test
  public void exitFortLeftEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', 'D', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see edge fort is not falsely detected.
  */ 
  @Test
  public void falseExitFortTopEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'D', 'K', 'D', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'D', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertFalse(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Another test to see edge fort is not falsely detected.
  */ 
  @Test
  public void falseExitFortRightEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', 'D', 'D'},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', 'D', 'K'},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', 'D', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertFalse(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test to check if uncapturable wall is present.
  */ 
  @Test
  public void uncapturableWallLeftEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'K', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Another test to check if uncapturable wall is present.
  */ 
  @Test
  public void uncapturableWallTopEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', 'A', ' ', 'D', 'K', 'D', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', 'D', ' ', 'D', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', 'D', 'D', 'D', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test to check if capturable wall fails.
  */ 
  @Test
  public void falseFortWallBottomEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'A', ' ', ' ', 'D', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'D', ' ', 'D', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'D', 'K', 'D', ' ', ' ', ' '}
      });

      // TODO: Test fails due to Defender being capturable.
      assertFalse(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to check if capturable wall fails.
  */ 
  @Test
  public void falseFortWallRightEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', 'A'},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', 'D', ' '},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', 'D', 'K'},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', 'D'},
        {' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertFalse(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }     

  /**
    Another test for detecting a valid exit fort.
  */ 
  @Test
  public void exitFortComplexLeftEdgeTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', 'D', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', 'D', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', 'D', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', 'D', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if checkKingMove() correctly finds an immobile king.
  */ 
  @Test
  public void kingMoveFailTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'K', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '}
      });

      assertFalse(board.checkKingMove());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if checkKingMove() correctly finds a mobile king.
  */ 
  @Test
  public void kingMoveLeftTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', 'D', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.checkKingMove());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if a safe king with space from the walls is still safe.
  */ 
  @Test
  public void exitFortSpacedTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if a safe king in a weird fort is secure.
  */
  @Test
  public void weirdExitFortTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if a safe king in a weird fort is secure.
  */
  @Test
  public void weirdExitFortTest2() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', 'D', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  } 

  /**
    Test to see if an unsafe king with a few attackers is not seen as an edge
    fort.
  */
  @Test
  public void fewDangerousAttackersTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', 'A', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertFalse(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test to see if a safe king with two attackers is seen as an edge fort.
  */
  @Test
  public void fewAttackersEdgeFortTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'A', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }

  /**
    Test to see if a cross edge fort is detected.
  */
  @Test
  public void crossEdgeFortTest() {
    Board board = null;
    try {
      board = new Board(new char[][]{
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', 'A', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {'K', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', 'D', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
      });

      assertTrue(board.isExitFort());
    } catch (BadAsciiBoardFormatException bex) {
      fail();
    }
  }
}

import static org.junit.Assert.*;

import cowards.Board;
import cowards.GridOutOfBoundsException;
import org.junit.Test;

/**
 * Test methods associated with selection and movement.
 */
public class MovementTest {

  /**
   * Verify that there is no selection when the game starts.
   */
  @Test
  public void noStartingSelectionTest() {
    Board board = new Board();
    assertFalse(board.hasSelection());
  }

  /**
   * Verify that the attacking side can select an attacker.
   */
  @Test
  public void selectAttackerAsAttackerTest() {
    Board board = new Board();
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
   * Verify that the attacking side cannot select an attacker.
   */
  @Test
  public void selectDefenderAsAttackerTest() {
    Board board = new Board();
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
   * Verify that the attacking side cannot select the king.
   */
  @Test
  public void selectKingAsAttackerTest() {
    Board board = new Board();
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
   * Verify that the attacking side cannot select an empty square.
   */
  @Test
  public void selectEmptyAsAttackerTest() {
    Board board = new Board();
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
   * Verify that the attacking side cannot select a piece above or to the left
   * of the board.
   */
  @Test
  public void selectLowerBoundAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(-1, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, -1);
      } catch (GridOutOfBoundsException ex2) {
        // pass
        return;
      }
    }

    fail();
  }

  /**
   * Verify that the attacking side cannot select a piece below or to the
   * right of the board.
   */
  @Test
  public void selectUpperBoundAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(11, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, 11);
      } catch (GridOutOfBoundsException ex2) {
        // pass
        return;
      }
    }

    fail();
  }

  /**
   * Verify the defending side cannot select an attacker.
   */
  @Test
  public void selectAttackerAsDefenderTest() {
    Board board = new Board();
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
   * Verify the defending side can select a defender.
   */
  @Test
  public void selectDefenderAsDefenderTest() {
    Board board = new Board();
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
   * Verify the defending side can select the king.
   */
  @Test
  public void selectKingAsDefenderTest() {
    Board board = new Board();
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
   * Verify the defending side cannot select an empty square.
   */
  @Test
  public void selectEmptyAsDefenderTest() {
    Board board = new Board();
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
   * Verify the defending side cannot select a square above or to the left
   * of the board.
   */
  @Test
  public void selectLowerBoundAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(-1, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, -1);
      } catch (GridOutOfBoundsException ex2) {
        // pass
        return;
      }
    }

    fail();
  }

  /**
   * Verify the defending side cannot select a square below or to the right
   * of the board.
   */
  @Test
  public void selectUpperBoundAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(11, 0);
    } catch (GridOutOfBoundsException ex) {
      try {
        board.select(0, 11);
      } catch (GridOutOfBoundsException ex2) {
        // pass
        return;
      }
    }

    fail();
  }

  /**
   * Verify movement fails for the attacking side when there is no selection.
   */
  @Test
  public void moveNoSelectionAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left one square works for the attacking side.
   */
  @Test
  public void moveLeftAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(0, 2));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right one square works for the attacking side.
   */
  @Test
  public void moveRightAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 7);
      assertTrue(board.move(0, 8));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up one square works for the attacking side.
   */
  @Test
  public void moveUpAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(9, 5);
      assertTrue(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down one square works for the attacking side.
   */
  @Test
  public void moveDownAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(1, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left multiple squares works for the attacking side.
   */
  @Test
  public void moveLeftMultipleAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(1, 5);
      assertTrue(board.move(1, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right multiple squares works for the attacking side.
   */
  @Test
  public void moveRightMutipleAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(1, 5);
      assertTrue(board.move(1, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up multiple squares works for the attacking side.
   */
  @Test
  public void moveUpMulitpleAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(10, 3);
      assertTrue(board.move(7, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down multiple squares works for the attacking side.
   */
  @Test
  public void moveDownMultipleAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertTrue(board.move(3, 3));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveLeftBarricadeAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(0, 1));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveRightBarricadeAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(0, 9));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveUpBarricadeAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(10, 5);
      assertFalse(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down fails when there is a barricade for the attacking side.
   */
  @Test
  public void moveDownBarricadeAsAttackerTest() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 5);
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify movement fails for the defending side when there is no selection.
   */
  @Test
  public void moveNoSelectionAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left one square works for the defending side.
   */
  @Test
  public void moveLeftAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right one square works for the defending side.
   */
  @Test
  public void moveRightAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 6));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up one square works for the defending side.
   */
  @Test
  public void moveUpAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down one square works for the defending side.
   */
  @Test
  public void moveDownAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(7, 5);
      assertTrue(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left multiple squares works for the defending side.
   */
  @Test
  public void moveLeftMultipleAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 1));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right multiple squares works for the defending side.
   */
  @Test
  public void moveRightMutipleAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(3, 5);
      assertTrue(board.move(3, 9));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up multiple squares works for the defending side.
   */
  @Test
  public void moveUpMulitpleAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(4, 4);
      assertTrue(board.move(2, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down multiple squares works for the defending side.
   */
  @Test
  public void moveDownMultipleAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(6, 4);
      assertTrue(board.move(9, 4));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving left fails when there is a barricade for the defending side.
   */
  @Test
  public void moveLeftBarricadeAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(5, 2));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving right fails when there is a barricade for the defending side.
   */
  @Test
  public void moveRightBarricadeAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(5, 8));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving up fails when there is a barricade for the defending side.
   */
  @Test
  public void moveUpBarricadeAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(2, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving down fails when there is a barricade for the defending side.
   */
  @Test
  public void moveDownBarricadeAsDefenderTest() {
    Board board = new Board();
    board.setAttackerTurn(false);

    try {
      board.select(5, 5);
      assertFalse(board.move(8, 5));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
     Verify attacking side cannot move to upper left corner.
  */
  @Test
  public void moveAttackerToTopLeft() {
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 3);
      assertFalse(board.move(0, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
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
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(0, 7);
      assertFalse(board.move(0, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
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
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(10, 3);
      assertFalse(board.move(10, 0));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
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
    Board board = new Board();
    board.setAttackerTurn(true);

    try {
      board.select(10, 7);
      assertFalse(board.move(10, 10));
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
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
    Board board = new Board();
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
      // This should not happen
      System.out.println("A GridOutOfBoundsException has been thrown");
      fail();
    }

    // Pass.
    return;
  }  

  /**
   * Verify moving back and forth as the defender too many times loses the game.
   */
  @Test
  public void backAndForthDefenderTest() {
    Board board = new Board();

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
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify moving back and forth as the attacker too many times loses the game.
   */
  @Test
  public void backAndForthAttackerTest() {
    Board board = new Board();

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
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }

  /**
   * Verify 50 move draw rule work as expected.
   */
  @Test
  public void drawTest() {
    Board board = new Board();

    try {
      // First move.
      board.select(5, 9);
      assertTrue(board.move(5, 8));

      int le = 7;
      int ri = 8;
      int tar = 1;
      int cur = 5;

      // Another 8 moves. Total = 9
      for (; tar < cur; --cur) {
        board.select(cur, le);
        assertTrue(board.move(cur - 1, le));
        board.select(cur, ri);
        assertTrue(board.move(cur - 1, ri));
      }

      tar = 9;
      // Another 16 moves. Total = 25
      for (; tar > cur; ++cur) {
        board.select(cur, le);
        assertTrue(board.move(cur + 1, le));
        board.select(cur, ri);
        assertTrue(board.move(cur + 1, ri));
      }

      tar = 1;
      // Another 16 moves. Total = 41
      for (; tar < cur; --cur) {
        board.select(cur, le);
        assertTrue(board.move(cur - 1, le));
        board.select(cur, ri);
        assertTrue(board.move(cur - 1, ri));
      }

      tar = 5;
      // Another 8 moves. Total = 49
      for (; tar > cur; ++cur) {
        board.select(cur, le);
        assertTrue(board.move(cur + 1, le));
        board.select(cur, ri);
        assertTrue(board.move(cur + 1, ri));
      }

      // Last move, should draw here.
      board.select(cur, le);
      assertTrue(board.move(cur + 1, le));

      // Check game over state.
      assertTrue(board.isGameOver());
      assertTrue(board.isDraw());
    } catch (GridOutOfBoundsException ex) {
      // This should not happen
      System.out.println("Movement failed due to GridOutOfBoundsException");
      fail();
    }

    // pass
    return;
  }
}

import static org.junit.Assert.*;

import cowards.Board;
import cowards.GridOutOfBoundsException;
import org.junit.Test;

/**
 * Test methods associated with selection and movement
 */
public class MovementTest {

  /**
   * Verify that there is no selection when the game starts
   */
  @Test
  public void noStartingSelectionTest() {
    Board board = new Board();
    assertFalse(board.hasSelection());
  }

  /**
   * Verify that the attacking side can select an attacker
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
   * Verify that the attacking side cannot select an attacker
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
   * Verify that the attacking side cannot select the king
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
   * Verify that the attacking side cannot select an empty square
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
   * of the board
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
   * right of the board
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
   * Verify the defending side cannot select an attacker
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
   * Verify the defending side can select a defender
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
   * Verify the defending side can select the king
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
   * Verify the defending side cannot select an empty square
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
   * of the board
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
   * of the board
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
   * Verify movement fails for the attacking side when there is no selection
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
   * Verify moving left one square works for the attacking side
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
   * Verify moving right one square works for the attacking side
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
   * Verify moving up one square works for the attacking side
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
   * Verify moving down one square works for the attacking side
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
   * Verify moving left multiple squares works for the attacking side
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
   * Verify moving right multiple squares works for the attacking side
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
   * Verify moving up multiple squares works for the attacking side
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
   * Verify moving down multiple squares works for the attacking side
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
   * Verify moving left fails when there is a barricade for the attacking side
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
   * Verify moving right fails when there is a barricade for the attacking side
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
   * Verify moving up fails when there is a barricade for the attacking side
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
   * Verify moving down fails when there is a barricade for the attacking side
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
   * Verify movement fails for the defending side when there is no selection
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
   * Verify moving left one square works for the defending side
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
   * Verify moving right one square works for the defending side
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
   * Verify moving up one square works for the defending side
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
   * Verify moving down one square works for the defending side
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
   * Verify moving left multiple squares works for the defending side
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
   * Verify moving right multiple squares works for the defending side
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
   * Verify moving up multiple squares works for the defending side
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
   * Verify moving down multiple squares works for the defending side
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
   * Verify moving left fails when there is a barricade for the defending side
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
   * Verify moving right fails when there is a barricade for the defending side
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
   * Verify moving up fails when there is a barricade for the defending side
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
   * Verify moving down fails when there is a barricade for the defending side
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
}

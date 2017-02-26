import static org.junit.Assert.*;

import cowards.Board;
import org.junit.Test;

public class SaveLoadTest {
  /**
    Test saving a game board in general.
   */
  @Test
  public void saveTest() {
    try {
      Board board = new Board();
      String fileName = "TEST";
      assertTrue(board.saveBoard(fileName));
    } catch (Exception ex) {
      fail();
    }
  }
  
  /**
    Test loading a game board in general.
   */
  @Test
  public void loadTest() {
    try {
      Board board = new Board();
      String fileName = "TEST";
      assertTrue(board.loadBoardFromSave(fileName));
    } catch (Exception ex) {
      fail();
    }
  }
}

import static org.junit.Assert.*;

import cowards.Board;
import java.io.*;
import java.util.*;
import org.junit.Test;

public class SaveLoadTest {
  /**
    Test saving a game board and comparing its saved contents with a known save
    file that executes the same moves.
   */
  @Test
  public void saveTest() {
    try {
      Board board = new Board();
      board.select(3, 0);
      board.move(3, 4);
      board.select(5, 3);
      board.move(5, 2);
      String fileName = "SAVETEST";
      assertTrue(board.saveBoard(fileName));
      Scanner saveReader = new Scanner(new File("saved_games/SAVETEST.txt"));
      Scanner testReader = new Scanner(new File("saved_games/TEST.txt"));
      
      while (saveReader.hasNextLine() && testReader.hasNextLine()) {
        String line1 = saveReader.nextLine();
        String line2 = testReader.nextLine();
        
        if (!line1.equals(line2)) {
          fail();
        }
      }
    } catch (Exception ex) {
      fail();
    }
  }
  
  /**
    Test loading a known game save file and ensuring the contents are read properly.
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

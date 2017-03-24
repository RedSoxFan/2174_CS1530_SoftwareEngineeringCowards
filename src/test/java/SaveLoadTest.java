import static org.junit.Assert.*;

import cowards.Board;
import cowards.BoardLayout;
import cowards.BoardLoader;
import cowards.BoardWriter;
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
      String saveTestPath = "saved_games/SAVETEST.txt";
      String testPath = "saved_games/TEST.txt";
      assertTrue(BoardWriter.saveBoard(fileName, board));
      Scanner saveReader = new Scanner(new File(saveTestPath));
      Scanner testReader = new Scanner(new File(testPath));
      
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
    Test loading a known save file and ensuring the contents are read properly.
   */
  @Test
  public void loadTest() {
    try {
      String fileName = "TEST";
      Board board = BoardLoader.loadBoardFromSave(fileName);
      assertNotNull(board);
    } catch (Exception ex) {
      fail();
    }
  }

  /**
    Test that loading a bad file gives us null.
   */
  @Test
  public void badLoadTest() {
    String fileName = "DOESNTEXIST";
    Board board = BoardLoader.loadBoardFromSave(fileName);
    assertNull(board);
  }


  // For shortening lines using this class.
  private BoardLayout.GridSquareState gss;

  /**
    Test the conversion of expected chars to GridSquareState.
   */
  @Test
  public void charToStateConversionTest() {
    assertEquals(gss.ATTACKER, BoardLoader.charToState('A'));
    assertEquals(gss.DEFENDER, BoardLoader.charToState('D'));
    assertEquals(gss.KING, BoardLoader.charToState('K'));
    assertEquals(gss.EMPTY, BoardLoader.charToState(' '));
  }

  /**
    Test the conversion of unexpected chars to GridSquareState.
   */
  @Test
  public void unusualCharToStateConversionTest() {
    assertEquals(gss.EMPTY, BoardLoader.charToState('F'));
    assertEquals(gss.EMPTY, BoardLoader.charToState('Q'));
    assertEquals(gss.EMPTY, BoardLoader.charToState('`'));
  }
}

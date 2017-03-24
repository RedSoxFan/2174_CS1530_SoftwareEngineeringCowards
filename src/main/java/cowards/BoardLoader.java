package cowards;

import java.io.*;
import java.util.*;

public class BoardLoader extends BoardLayout {

  /**
    Returns the GridSquareState for the piece represented by the character ch.
   */
  public static GridSquareState charToState(char ch) {
    if (ch == 'A') {
      return GridSquareState.ATTACKER;
    }
    if (ch == 'D') {
      return GridSquareState.DEFENDER;
    }
    if (ch == 'K') {
      return GridSquareState.KING;
    }

    return GridSquareState.EMPTY;
  }

  /**
    Fetches the LinkedList of moves that was stored in  the save file.
   */
  public static LinkedList<int []> getNextMoveList(Scanner sc, int numAttacks) {
    LinkedList<int []> ret = new LinkedList<int []>();
    for (int i = 0; i < numAttacks; ++i) {
      String currLine = sc.nextLine();
      int row = Character.getNumericValue(currLine.charAt(1));
      int col = Character.getNumericValue(currLine.charAt(2));
      ret.add(new int[] {row, col});
    }
    return ret;
  }

  /**
    Loads the characters representing the board and reconstructs them into the
    GridSquareState AoA.
   */
  private static GridSquareState[][] loadInnerBoard(Scanner sc) {
    int rowSize = GRID_ROW_MAX + 1;
    int colSize = GRID_COL_MAX + 1;
    GridSquareState[][] ret = new GridSquareState[rowSize][colSize];
    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      String currLine = sc.nextLine();
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        ret[r][c] = charToState(currLine.charAt(c));
      }
    }
    return ret;
  }

  /**
    Creates and return a Board instance from a savefile.
    
    @param fileName String of board to load from saved file.
   */
  public static Board loadBoardFromSave(String fileName) {
    String pathName = "saved_games/" + fileName + ".txt";
    
    try {
      Scanner fileReader = new Scanner(new File(pathName));

      final int mwoCap = Integer.parseInt(fileReader.nextLine());
      final boolean at = Boolean.parseBoolean(fileReader.nextLine());
      int numAttacks   = Integer.parseInt(fileReader.nextLine());
      int numDefends   = Integer.parseInt(fileReader.nextLine());

      // Get each of the move lists for the two players.
      LinkedList<int []> am = getNextMoveList(fileReader, numAttacks);
      LinkedList<int []> dm = getNextMoveList(fileReader, numDefends);

      int kr = Integer.parseInt(fileReader.nextLine());
      int kc = Integer.parseInt(fileReader.nextLine());

      // Get the inner board found in the file.
      GridSquareState[][] innerBoard = loadInnerBoard(fileReader);

      fileReader.close();
      return new Board(innerBoard, am, dm, mwoCap, kr, kc, at);
    } catch (FileNotFoundException ex) {
      /* Fall through */
    }
    
    /* Return a uninitialized board state. */
    return (Board)null;
  }
}

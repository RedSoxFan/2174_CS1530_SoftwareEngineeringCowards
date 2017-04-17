package cowards;

import java.io.*;
import java.util.*;

public class BoardProcessor extends BoardLayout {
  /**
    There can be no more than 3 back-and-forth motions (6 moves total).
   */
  private static final int maxRepeatMoves = 6;

  /**
    Check if the current opposing player loses on account of repeat moves.

    @return Whether or not the opponent loses on repeat moves.
    */
  public static boolean tooManyRepeats(ListIterator<int []> moves) {
    // Check if the last six moves are back and fourth.
    int [] first = new int [4];
    int [] second = new int [4];

    // Look through the list of previous moves and see if there were more than
    // the max allowable back-and-forth motions.
    // Note: max motions = maxRepeatedMoves / 2
    //
    // To avoid losing the player must avoid cycling between two squares over
    // the course of maxRepeatedMoves moves.
    for (int i = 0; i < maxRepeatMoves && moves.hasNext(); ++i) {
      int [] nxt = moves.next();
      if (i == 0) {
        // First move in sequence.
        first[0] = nxt[0];
        first[1] = nxt[1];
        first[2] = nxt[2];
        first[3] = nxt[3];
      } else if (i == 1) {
        // Second move in sequence.
        second[0] = nxt[0];
        second[1] = nxt[1];
        second[2] = nxt[2];
        second[3] = nxt[3];
      } else if (i % 2 == 0 && (first[0] != nxt[0] || first[1] != nxt[1]
          || first[2] != nxt[2] || first[3] != nxt[3])) {
        // Deviation from the first move in the sequence.
        return false;
      } else if (i % 2 == 1 && (second[0] != nxt[0] || second[1] != nxt[1]
          || second[2] != nxt[2] || first[3] != nxt[3])) {
        // Deviation from the second move in the sequence.
        return false;
      } else if (i == maxRepeatMoves - 1) {
        return true;
      }
    }
    return false;
  }

  /**
    Check if the path is clear to the specified square.

    @param board The board being checked.
    @param row The row of the square.
    @param col The column of the square.
    @param selRow The row of the selected square.
    @param selCol The column of the selected square.

    @return Whether or not the path is clear.
    */
  public static boolean isPathClear(
      Board board, int row, int col, int selRow, int selCol)
      throws GridOutOfBoundsException {
    // Determine the top, bottom, left, and right.
    // Either top and bottom or left and right will be the same.
    int top    = (row < selRow) ? row    : selRow;
    int bottom = (row < selRow) ? selRow : row;
    int left   = (col < selCol) ? col    : selCol;
    int right  = (col < selCol) ? selCol : col;

    // Walk the path to make sure it is clear.
    for (int r = top; r <= bottom; r++) {
      for (int c = left; c <= right; c++) {
        // Ignore the selected square.
        if (r != selRow || c != selCol) {
          // If the square is not empty, stop walking.
          if (!board.square(r, c).isEmpty()) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
    Check to see if the king was captured.

    @param board The board being checked.

    @return Whether or not king was captured.
   */
  public static boolean kingCapture(Board board) throws GridOutOfBoundsException {
    boolean captured = false;

    int kingRow = board.getKingRow();
    int kingCol = board.getKingCol();
    //Check if king is not near edge of board.
    if (kingRow != 0 && kingRow != 10 && kingCol != 0 && kingCol != 10) {
      // Check if king is surrounded by attackers.
      if (board.square(kingRow - 1, kingCol).isAttacking()
            && board.square(kingRow + 1, kingCol).isAttacking()
            && board.square(kingRow, kingCol - 1).isAttacking()
            && board.square(kingRow, kingCol + 1).isAttacking()) {
        captured = true;
      }
    }

    return captured;
  }

  /**
    Return if the proposed move is valid.

    @param board The board being checked.
    @param row The row of the square.
    @param col The column of the square.
    @param selRow The row of the selected square.
    @param selCol The column of the selected square.

    @return If the move is valid.
  */
  public static boolean isValidMove(
      Board board, int row, int col, int selRow, int selCol)
      throws GridOutOfBoundsException {
    // Make sure piece other than king isn't moving to throne or four corners.
    if (!board.square(selRow, selCol).isKing() && board.inSpecialLocation(row, col)) {
      return false;
    }

    // Check for a clear path.
    if (!isPathClear(board, row, col, selRow, selCol)) {
      return false;
    }

    return true;
  }

  /**
    Return whether or not the defending side is surrounded by the attackers.

    @param board The board being checked.

    @return Whether or not the defending side is surrounded.
   */
  public static boolean isSurrounded(Board board) {
    // Perform a flood fill. If exited the board, return false.
    boolean[][] fill = new boolean[GRID_ROW_MAX + 1][GRID_COL_MAX + 1];
    if (!surroundFloodFill(board, fill, board.getKingRow(), board.getKingCol())) {
      return false;
    }

    // Make sure all defenders (including the king) are in a filled square.
    for (int r = 0; r < fill.length; r++) {
      for (int c = 0; c < fill[r].length; c++) {
        if (!fill[r][c] && board.safeSquare(r, c).isDefending()) {
          return false;
        }
      }
    }

    // Surrounded.
    return true;
  }

  /**
    Perform a flood fill. Attackers will be treated as barriers. If the fill attempts
    to leave the board, this method will exit without completing the fill.

    @param board The board being processed.
    @param fill The fill state of each square.
    @param row The current row.
    @param col The current column.

    @return Whether or not the flood fill completed without exiting the board.
   */
  private static boolean surroundFloodFill(Board board, boolean[][] fill, int row, int col) {
    // Test the current square.
    try {
      // If the square is already filled, nothing to do.
      if (fill[row][col]) {
        return true;
      }

      // Treat attacking pieces as barriers.
      if (!board.square(row, col).isAttacking()) {
        // Fill the square.
        fill[row][col] = true;

        // Check the four adjancent squares clockwise, starting at the top.
        // If any of them exit the board, propigate the failure.
        if (!surroundFloodFill(board, fill, row - 1, col)) {
          return false;
        }
        if (!surroundFloodFill(board, fill, row, col + 1)) {
          return false;
        }
        if (!surroundFloodFill(board, fill, row + 1, col)) {
          return false;
        }
        if (!surroundFloodFill(board, fill, row, col - 1)) {
          return false;
        }
      }
    } catch (ArrayIndexOutOfBoundsException aoobex) {
      // Exited the board.
      return false;
    } catch (GridOutOfBoundsException goobex) {
      // Exited the board.
      return false;
    }

    // So far, the fill has stayed within the board.
    return true;
  }

  /**
    Returns if it is possible for an attacker to get on opposite adjacent sides
    of this piece.

    All sides must be open due to the nature of the fort.

    @param row Row of desired square.
    @param col Column of desired square.
  */
  public static boolean capturableGuard(Board board, int row, int col) {
    try {
      return board.square(row, col).isDefending()
          && board.square(row - 1, col).isEmpty()
          && board.square(row + 1, col).isEmpty()
          && board.square(row, col - 1).isEmpty()
          && board.square(row, col + 1).isEmpty();
    } catch (GridOutOfBoundsException gx) {
      return false;
    }
  }

  /**
    Returns if a fort can be broken.

    @param board The board being checked.
    @param fill The fill array being checked against.

    @return Whether or not the fort holds.
   */
  public static boolean isFortSolid(Board board, boolean[][] fill) {
    int filledEmpty = 0;
    for (int r = 0; r < fill.length; r++) {
      for (int c = 0; c < fill[r].length; c++) {
        // It may be the case that we flooded the entire board (in the case no attackers remain).
        if (fill[r][c]) {
          ++filledEmpty;
        }
        if (fill[r][c] && board.safeSquare(r, c).isAttacking()) {
          return false;
        }
      }
    }

    // Surrounded.
    return true;
  }

  /**
    Return whether or not the king is surrounded by the defenders.

    @param board The board being checked.

    @return Whether or not the king is guarded.
   */
  public static boolean isKingGuarded(Board board) {
    // Perform a flood fill.
    boolean[][] fill = new boolean[GRID_ROW_MAX + 1][GRID_COL_MAX + 1];
    
    guardFloodFill(board, fill, board.getKingRow(), board.getKingCol());

    // It may be the case that we flooded the entire board (in the case no attackers remain).
    // If the fort isn't solid, but the king is uncaptureable it is guarded (ie
    // attackers fewer than 2 pieces).
    return isFortSolid(board, fill) || board.getAttackers().size() <= 2;
  }

  /**
    Perform a flood fill. Defenders and edges will be treated as barriers.

    @param board The board being processed.
    @param fill The fill state of each square.
    @param row The current row.
    @param col The current column.

    @return Whether or not the flood fill completed without exiting the board.
   */
  private static void guardFloodFill(Board board, boolean[][] fill, int row, int col) {
    // Test the current square.
    if (!inBounds(new int[] {row, col})) {
      return;
    }

    // If the square is already filled, nothing to do.
    if (fill[row][col]) {
      return;
    }

    // Treat uncapturable defending pieces as barriers.
    if (!board.safeSquare(row, col).isDefender() || capturableGuard(board, row, col)) {
      fill[row][col] = true;

      // Check the four adjancent squares clockwise, starting at the top.
      // If any of them exit the board, propagate the failure.
      guardFloodFill(board, fill, row - 1, col);
      guardFloodFill(board, fill, row, col + 1);
      guardFloodFill(board, fill, row + 1, col);
      guardFloodFill(board, fill, row, col - 1);
    }
  }

  /**
    Returns whether or not there are there are moves available.

    @param board The board being processed.

    @return Whether or not the there are moves available.
   */
  public static boolean areMovesAvailable(Board board) {
    // Determine whose turn it is.
    boolean attacking = board.isAttackerTurn();

    // Iterate over each square.
    for (int [] p : attacking ? board.getAttackers() : board.getDefenders()) {
      // If the piece is the same side, check for moves.
      if ((attacking && board.safeSquare(p[0], p[1]).isAttacking())
          || (!attacking && board.safeSquare(p[0], p[1]).isDefending())) {
        // Check to see if any of the four adjacent squares are empty.
        // If any of the squares are empty, a move exists.
        if (isEmpty(board, p[0] - 1, p[1])) {
          return true;
        }
        if (isEmpty(board, p[0], p[1] - 1)) {
          return true;
        }
        if (isEmpty(board, p[0] + 1, p[1])) {
          return true;
        }
        if (isEmpty(board, p[0], p[1] + 1)) {
          return true;
        }
      }
    }
    // No moves.
    return false;
  }

  /**
    Returns whether or not a square is empty. This is a convience method so that
    GridOutOfBoundsException does not need to be interwined with the actual method
    logic. Unlike safeSquare, this method will NOT treat out of bounds as empty.

    @param board The board being processed.
    @param row The row of the square to check.
    @param col The column of the square to check.

    @return Whether or not the square is empty.
   */
  private static boolean isEmpty(Board board, int row, int col) {
    try {
      return board.square(row, col).isEmpty();
    } catch (GridOutOfBoundsException oobex) {
      return false;
    }
  }

  /** Returns a linked list containing the positions of every attacker. */
  public static LinkedList<int []> findAllAttackers(GridSquareState[][] board) {
    if (board == null) {
      return new LinkedList<int []>();
    }

    LinkedList<int []> ret = new LinkedList<int []>();
    for (int r = 0; r < 11; ++r) {
      for (int c = 0; c < 11; ++c) {
        if (board[r][c].isAttacking()) {
          ret.add(new int[] {r, c});
        }
      }
    }

    return ret;
  }

  /** Returns a linked list containing the positions of every defender. */
  public static LinkedList<int []> findAllDefenders(GridSquareState[][] board) {
    if (board == null) {
      return new LinkedList<int []>();
    }

    LinkedList<int []> ret = new LinkedList<int []>();
    for (int r = 0; r < 11; ++r) {
      for (int c = 0; c < 11; ++c) {
        if (board[r][c].isDefending()) {
          ret.add(new int[] {r, c});
        }
      }
    }

    return ret;
  }

  /**
    Stores the defensive board position. If a piece has been captured, the
    count for the position is reset to zero. Otherwise, it is incremented by
    one. If the count reaches three, then the defending side loses.

    @param board The board to process.

    @return Whether or not the game is over due to a draw fort.
   */
  public static boolean storeDefensiveBoard(Board board) {
    // Get the current defensive board position and number of defenders.
    Map.Entry<String, Integer> posAndCount = defensivePosition(board);
    String position = posAndCount.getKey();

    // If there are less than 5 pieces (king + defenders), then the rule is void.
    if (posAndCount.getValue() < 5) {
      return false;
    }
    
    // Retrieve the defensive board positions and whether or not there was a capture.
    HashMap<String, Integer> defensive = board.getDefensiveBoardPositions();
    boolean capture = board.getMovesWoCapture() == 0;

    // If a piece has been captured, reset the defensive boards and return that
    // no game over state has been reached.
    if (capture) {
      defensive.clear();
      return false;
    }
    
    // Find the number of times without a capture that the position has been used.
    int times = 0;
    if (defensive.containsKey(position)) {
      times = defensive.get(position).intValue();

      // If this is the third time, there is a game over state.
      if (times == 2) {
        return true;
      }
    }

    // Increment the number of times the position has been used. 
    defensive.put(position, new Integer(times + 1));

    // No game over state has been reached.
    return false;
  }

  /**
    Retrieves the defensive board position. This is a 121 character bit string
    where 1 is a defender and 0 is not.

    @param board The board being processed.

    @return The bit string containing the defensive board position along with the
      number of defending pieces.
   */
  private static Map.Entry<String, Integer> defensivePosition(Board board) {
    StringBuffer buff = new StringBuffer();
    int count = 0;
    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        int bit = board.safeSquare(r, c).isDefending() ? 1 : 0;
        count += bit;
        buff.append(bit);
      }
    }
    return new AbstractMap.SimpleEntry<String, Integer>(buff.toString(), count);
  }

  /**
    Check if the given square is in bounds.

    @param square The square to check.

    @return Whether or not the piece is in bounds.
  */
  public static boolean inBounds(int [] square) {
    return square[0] >= 0 && square[1] >= 0 && square[0] < 11 && square[1] < 11;
  }

}

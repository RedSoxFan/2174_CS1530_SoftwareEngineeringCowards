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
    int [] first = new int [2];
    int [] second = new int [2];

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
      } else if (i == 1) {
        // Second move in sequence.
        second[0] = nxt[0];
        second[1] = nxt[1];
      } else if (i % 2 == 0 && (first[0] != nxt[0] || first[1] != nxt[1])) {
        // Deviation from the first move in the sequence.
        return false;
      } else if (i % 2 == 1 && (second[0] != nxt[0] || second[1] != nxt[1])) {
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
    Returns whether or not there are there are moves available.

    @param board The board being processed.

    @return Whether or not the there are moves available.
   */
  public static boolean areMovesAvailable(Board board) {
    // Determine whose turn it is.
    boolean attacking = board.isAttackerTurn();

    // Iterate over each square.
    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        // If the piece is the same side, check for moves.
        if ((attacking && board.safeSquare(r, c).isAttacking())
            || (!attacking && board.safeSquare(r, c).isDefending())) {
          // Check to see if any of the four adjacent squares are empty.
          // If any of the squares are empty, a move exists.
          if (isEmpty(board, r - 1, c)) {
            return true;
          }
          if (isEmpty(board, r, c - 1)) {
            return true;
          }
          if (isEmpty(board, r + 1, c)) {
            return true;
          }
          if (isEmpty(board, r, c + 1)) {
            return true;
          }
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
}

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

    @param row The row of the square.
    @param col The column of the square.

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

}

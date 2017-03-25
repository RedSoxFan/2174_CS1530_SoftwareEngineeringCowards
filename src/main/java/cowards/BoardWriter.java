package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class BoardWriter extends BoardLayout {

  /**
    Write out the simple information about the board state.

    @param pw Where we are writing to.
    @param board The Board who's information we are writing.
  */
  private static void writeState(PrintWriter pw, Board board) {
    pw.println(board.getMovesWoCapture());
    pw.println(board.isAttackerTurn());
  }

  /**
    Writes out all of the recorded moves thus far. Used for loading in data
    required for calculating repeat moves.

    @param pw Where we are writing to.
    @param board The Board who's information we are writing.
  */
  private static void writeStoredMoves(PrintWriter pw, Board board) {
    LinkedList<int []> attackerMoves = board.getAttMoves();
    LinkedList<int []> defenderMoves = board.getDefMoves();
    pw.println(attackerMoves.size());
    pw.println(defenderMoves.size());

    // Write attacker moves.
    for (int i = 0; i < attackerMoves.size(); i++) {
      int[] currMove = attackerMoves.get(i);
      pw.print("A");
      pw.print(currMove[0]);
      pw.print(currMove[1]);
      pw.println();
    }

    // Write defender moves.
    for (int i = 0; i < defenderMoves.size(); i++) {
      int[] currMove = defenderMoves.get(i);
      pw.print("D");
      pw.print(currMove[0]);
      pw.print(currMove[1]);
      pw.println();
    }
  }

  /**
    Writes out an ASCII representation of the current board layout.

    @param pw Where we are writing to.
    @param board The Board who's information we are writing.
  */
  private static void writeAsciiBoard(PrintWriter pw, Board board) {
    GridSquareState[][] innerBoard = board.getBoard();
    pw.println(board.getKingRow());
    pw.println(board.getKingCol());

    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        if (innerBoard[r][c].isAttacking()) {
          pw.print('A');
        } else if (innerBoard[r][c].isDefender()) {
          pw.print('D');
        } else if (innerBoard[r][c].isKing()) {
          pw.print('K');
        } else {
          pw.print('E');
        }
      }
      pw.println();
    }
  }

  /**
    Save the current instance of the board to a text file.
    
    @param fileName Filename of the file to which we are saving.
    @param board The Board who's information we are writing.
  */
  public static boolean saveBoard(String fileName, Board board) {
    File dir = new File("saved_games");
    String pathName = "saved_games/" + fileName + ".txt";
    
    // Create the saved_games directory if it doesn't exist.
    if (!dir.exists()) {
      boolean success = dir.mkdir();
      if (!success) {
        return false;
      }
    }
    
    try {
      PrintWriter pw = new PrintWriter(pathName);
      writeState(pw, board);
      writeStoredMoves(pw, board);
      writeAsciiBoard(pw, board);
      
      pw.close();
    } catch (Exception ex) {
      return false;
    }
    
    return true;
  }
}

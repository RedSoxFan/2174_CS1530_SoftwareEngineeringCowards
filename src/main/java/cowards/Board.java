package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Board implements Serializable {

  public static enum GridSquareState {
    EMPTY, KING, DEFENDER, ATTACKER
  }
  
  private static final long serialVersionUID = 7526472295622776147L;
  public static int GRID_ROW_MAX = 10;
  public static int GRID_COL_MAX = 10;

  public static final char[][] INITIAL_BOARD = new char[][]{
      {' ', ' ', ' ', 'A', 'A', 'A', 'A', 'A', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'A', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ', 'A'},
      {'A', ' ', ' ', ' ', 'D', 'D', 'D', ' ', ' ', ' ', 'A'},
      {'A', 'A', ' ', 'D', 'D', 'K', 'D', 'D', ' ', 'A', 'A'},
      {'A', ' ', ' ', ' ', 'D', 'D', 'D', ' ', ' ', ' ', 'A'},
      {'A', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ', 'A'},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', 'A', 'A', 'A', 'A', 'A', ' ', ' ', ' '}
  };

  private GridSquareState[][] board = new GridSquareState[11][11];
  private boolean attackerTurn = true;
  private boolean gameOver = false;

  private int selRow = -1;
  private int selCol = -1;

  /**
    Constructor.
  */
  public Board() {
    reset();
  }

  /**
    Getter for attackerTurn.
   */
  public boolean isAttackerTurn() {
    return attackerTurn;
  }

  /**
    Setter for attackerTurn.

    @param turn Boolean of whether it is the attacker's turn or not.
   */
  public void setAttackerTurn(boolean turn) {
    attackerTurn = turn;
  }

  /**
    Getter for gameOver.
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
    Setter for gameOver.

    @param turn Boolean of whether the game is over.
   */
  public void setGameOver(boolean turn) {
    gameOver = turn;
  }

  /**
   * Whether or not a square is selected.
   */
  public boolean hasSelection() {
    return selRow != -1 && selCol != -1;
  }

  /**
   * Getter for selRow.
   */
  public int getSelectedRow() {
    return selRow;
  }

  /**
   * Getter for selCol.
   */
  public int getSelectedColumn() {
    return selCol;
  }

  /**
   * Attempt to select a piece.
   *
   * @param row The row of the piece to select.
   * @param col The column of the piece to select.
   */
  public void select(int row, int col) throws GridOutOfBoundsException {
    switch (square(row, col)) {
      // The attacking side can only select attacker pieces.
      case ATTACKER:
        if (isAttackerTurn()) {
          selRow = row;
          selCol = col;
        }
        break;

        // The defending side can only select defender pieces and the king.
      case DEFENDER:
      case KING:
        if (!isAttackerTurn()) {
          selRow = row;
          selCol = col;
        }
        break;

        // For clarity sake, include the default no-op case.
      default:
        break;
    }
  }

  /**
   * Attempt to move a piece.
   *
   * @param row The row of the square to move to.
   * @param col The column of the square to move to.
   *
   * @return Whether or not the selected piece was moved.
   */
  public boolean move(int row, int col) throws GridOutOfBoundsException {
    boolean moved = false;
    // Verify there is a selection and only one axis differs.
    if (hasSelection() && ((row != selRow) ^ (col != selCol))) {
      // TODO: Check the reserved squares that only the king can move to.
      moved = true;
      // Determine the top, bottom, left, and right.
      // Either top and bottom or left and right will be the same.
      int top;
      int bottom;
      int left;
      int right;
      if (row < selRow) {
        top = row;
        bottom = selRow;
      } else {
        top = selRow;
        bottom = row;
      }
      if (col < selCol) {
        left = col;
        right = selCol;
      } else {
        left = selCol;
        right = col;
      }
      // Walk the path to make sure it is clear.
      for (int r = top; r <= bottom; r++) {
        for (int c = left; c <= right; c++) {
          // Ignore the selected square.
          if (r != selRow || c != selCol) {
            // If the square is not empty, stop walking.
            if (!square(r, c).equals(GridSquareState.EMPTY)) {
              moved = false;
              break;
            }
          }
        }
        // If a barricade was found, stop walking.
        if (!moved) {
          break;
        }
      }
      // If there is no conflict, move the piece, deselect, and end turn.
      if (moved) {
        board[row][col] = square(selRow, selCol);
        board[selRow][selCol] = GridSquareState.EMPTY;
        selRow = -1;
        selCol = -1;
        // TODO: Check for captures.
        setAttackerTurn(!isAttackerTurn());
      }
    }
    // Return whether the selected piece was moved.
    return moved;
  }

  /**
    Reset the board to starting conditions.
   */
  public void reset() {
    // Initialize the board.
    try {
      loadBoardFromChar(INITIAL_BOARD);
    } catch (BadAsciiBoardFormatException exception) {
      // This should never fire, as the starting board is pre-configured.
      System.out.println(
          "CRITICAL ERROR: Bad initial board configuration."
      );

      // TODO: Refactor critical error messages into a single critical error
      // function.
      System.exit(1);
    }
    attackerTurn = true;
    gameOver = false;
  }

  /**
    Takes a char AoA and interprets the characters as pieces and spaces.

    Throws an exception if the board is poorly formatted.

    @param charBoard Character AoA for ascii representation of board.
    */
  public void loadBoardFromChar(char[][] charBoard)
      throws BadAsciiBoardFormatException {

    if (charBoard == null
        || charBoard.length < GRID_ROW_MAX + 1
        || charBoard[0].length < GRID_COL_MAX + 1) {
      throw new BadAsciiBoardFormatException();
    }
    for (int r = 0; r < 11; r++) {
      for (int c = 0; c < 11; c++) {
        char square = charBoard[r][c];
        if (square == 'A') {
          board[r][c] = GridSquareState.ATTACKER;
        } else if (square == 'D') {
          board[r][c] = GridSquareState.DEFENDER;
        } else if (square == 'K') {
          board[r][c] = GridSquareState.KING;
        } else {
          board[r][c] = GridSquareState.EMPTY;
        }
      }
    }
  }
  /**
    Save the current instance of the board to a text file.
    
    @param fileName String to save the board to
  */
  public void saveBoard(String fileName) {
    File dir = new File("saved_games");
    String pathName = "saved_games/" + fileName + ".txt";
    
    //create the saved_games directory if it doesn't exists
    if (!dir.exists()) {
      boolean success = dir.mkdir();
      if (success) {
        JOptionPane.showMessageDialog(null, "Created a saved_games directory for save files.");
      } else {
        JOptionPane.showMessageDialog(null, "Error creating saved_games directory.");
        return;
      }
    }
    
    try {
      PrintWriter pw = new PrintWriter(pathName);
      pw.println(attackerTurn);
      for (int r = 0; r < 11; r++) {
        for (int c = 0; c < 11; c++) {
          if (board[r][c] == GridSquareState.ATTACKER) {
            pw.print('A');
          } else if (board[r][c] == GridSquareState.DEFENDER) {
            pw.print('D');
          } else if (board[r][c] == GridSquareState.KING) {
            pw.print('K');
          } else {
            pw.print('E');
          }
        }
        
        pw.println();
      }
      
      pw.close();
      JOptionPane.showMessageDialog(null, "Successfully saved game to text file.");
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, "An error occurred when saving the game to a text file");
    }
  }
  
  /**
    Takes an instance of a board from a saved game.
    
    @param fileName String of board to load from saved file
  */
  public void loadBoardFromSave(String fileName) {
    String pathName = "saved_games/" + fileName + ".txt";
    
    try {
      Scanner fileReader = new Scanner(new File(pathName));
      attackerTurn = fileReader.nextBoolean();
      fileReader.nextLine();
      for (int r = 0; r < 11; r++) {
        String currLine = fileReader.nextLine();
        
        for (int c = 0; c < 11; c++) {
          char currChar = currLine.charAt(c);
          
          if (currChar == 'A') {
            board[r][c] = GridSquareState.ATTACKER;
          } else if (currChar == 'D') {
            board[r][c] = GridSquareState.DEFENDER;
          } else if (currChar == 'K') {
            board[r][c] = GridSquareState.KING;
          } else {
            board[r][c] = GridSquareState.EMPTY;
          }
        }
      }
      fileReader.close();
      JOptionPane.showMessageDialog(null, "Successfully loaded game from save file.");
    } catch (FileNotFoundException ex) {
      JOptionPane.showMessageDialog(null, "Cannot find the file specified.");
    }
  }
  
  /**
    Returns the state of the square at the row and column provided.

    @param row Row of desired square.
    @param column Column of desired square.
   */
  public GridSquareState square(int row, int column) throws GridOutOfBoundsException {
    if (row > GRID_ROW_MAX || column > GRID_COL_MAX
        || row < 0 || column < 0) {
      throw new GridOutOfBoundsException();
    }

    return board[row][column];
  }
}

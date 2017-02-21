package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import javax.swing.*;

public class Board implements Serializable {

  public static enum GridSquareState {
    EMPTY, KING, DEFENDER, ATTACKER;

    public boolean isAttacking() {
      return this.equals(ATTACKER);
    }

    public boolean isDefending() {
      return this.equals(DEFENDER) || this.equals(KING);
    }

    public boolean isEmpty() {
      return this.equals(EMPTY);
    }
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

  public static final int[][] SPECIAL_SQUARE_POSITIONS = new int[][]{
    {0, 0},
    {0, 10},
    {10, 0},
    {10, 10},
    {5, 5}
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
    Whether or not a square is selected.
   */
  public boolean hasSelection() {
    return selRow != -1 && selCol != -1;
  }

  /**
    Getter for selRow.
   */
  public int getSelectedRow() {
    return selRow;
  }

  /**
    Getter for selCol.
   */
  public int getSelectedColumn() {
    return selCol;
  }

  /**
    Attempt to select a piece.

    @param row The row of the piece to select.
    @param col The column of the piece to select.
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
    Attempt to move a piece.

    @param row The row of the square to move to.
    @param col The column of the square to move to.

    @return Whether or not the selected piece was moved.
   */
  public boolean move(int row, int col) throws GridOutOfBoundsException {
    boolean moved = false;
    // Verify there is a selection and only one axis differs.
    if (hasSelection() && ((row != selRow) ^ (col != selCol))) {
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
      // Make sure piece other than king isn't moving to throne or four corners.
      if (!square(selRow, selCol).equals(GridSquareState.KING)) {
        for (int i = 0; i < SPECIAL_SQUARE_POSITIONS.length; i++) {
          int specialRow = SPECIAL_SQUARE_POSITIONS[i][0];
          int specialCol = SPECIAL_SQUARE_POSITIONS[i][1];
          if (row == specialRow && col == specialCol) {
            moved = false;
            break;
          }
        }
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
        capture(row, col);
        // TODO: Check to see if move was winning move.
        setAttackerTurn(!isAttackerTurn());
      }
    }
    // Return whether the selected piece was moved.
    return moved;
  }

  /**
     Check to see if there is any captures as a result of a move.

     @param row The destination row of the move.
     @param column The destination column of the move.
   */
  public int capture(int row, int column) throws GridOutOfBoundsException {

    int captured = 0;

    // Try the basic captures.
    captured += basicCapture(row, column, row - 2, column);
    captured += basicCapture(row, column, row + 2, column);
    captured += basicCapture(row, column, row, column - 2);
    captured += basicCapture(row, column, row, column + 2);

    // TODO: King captures.
    // TODO: Fort captures.

    return captured;
  }

  /**
    Check to see if there is a basic capture between two pieces.

    @param rowA The row of the first piece.
    @param colA The second of the second piece.
    @param rowB The row of the second piece.
    @param colB The column of the second piece.
  */
  private int basicCapture(int rowA, int colA, int rowB, int colB) {
    int captured = 0;
    
    // Make sure this pieces for a valid basic capture.
    if ((Math.abs(rowA - rowB) == 2 && colA == colB)
        || (Math.abs(colA - colB) == 2 && rowA == rowB)) {

      // Retrieve the state of the squares. If they are out
      // of bounds, EMPTY will be returned. This is fine
      // since and will be quietly ignored to simplify the
      // code.
      GridSquareState pieceA = safeSquare(rowA, colA);
      GridSquareState pieceB = safeSquare(rowB, colB);

      // Retrieve the state of the piece that could be captured.
      GridSquareState pieceC;
      int rowC;
      int colC;
      if (rowA == rowB) {
        rowC = rowA;
        colC = colA < colB ? colA + 1 : colB + 1;
      } else {
        rowC = rowA < rowB ? rowA + 1 : rowB + 1;
        colC = colA;
      }
      pieceC = safeSquare(rowC, colC);

      // Check capture possibilities.
      if (pieceA.isAttacking() && pieceB.isAttacking()
          && pieceC.equals(GridSquareState.DEFENDER)) {
        // Two Attackers capturing Defender.
        captured++;
        board[rowC][colC] = GridSquareState.EMPTY;
      } else if (pieceA.isDefending() && pieceB.isDefending()
          && pieceC.isAttacking()) {
        // Two Defenders or Defender+King capturing Attacker.
        captured++;
        board[rowC][colC] = GridSquareState.EMPTY;
      }

      // TODO: Allow specials to take place.
    }

    return captured;
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
    Takes an instance of a board from a saved game.

    @param loadedBoard Board from saved file
  */
  public void loadBoardFromSave(Board loadedBoard) {
    //TODO this method
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

  /**
    Returns the state of the square at the row and column provided. If the square
    is out of bounds, it will be treated as empty instead of throwing an error.

    @param row Row of desired square.
    @param column Column of desired square.
  */
  public GridSquareState safeSquare(int row, int column) {
    GridSquareState state = GridSquareState.EMPTY;

    try {
      state = square(row, column);
    } catch (GridOutOfBoundsException exception) {
      // Quietly ignore. Treat as default (empty).
    }

    return state;
  }
}

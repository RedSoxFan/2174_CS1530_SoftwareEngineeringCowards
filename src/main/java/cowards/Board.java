package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Board extends BoardLayout {
  private GridSquareState[][] board = new GridSquareState[11][11];
  private boolean attackerTurn = true;
  private boolean gameOver = false;

  // No more than 3 back-and-forth motions (6 moves total).
  private final int maxRepeatMoves = 6;
  private LinkedList<int []> attackerMoves;
  private LinkedList<int []> defenderMoves;

  private final int maxMovesWoCapture = 50;
  private int movesWoCapture = 0;

  private int selRow = -1;
  private int selCol = -1;

  // Keep track of king to make checking for king captures easier.
  private int kingRow = 5;
  private int kingCol = 5;

  /**
    Constructor.
  */
  public Board() {
    attackerMoves = new LinkedList<int []>();
    defenderMoves = new LinkedList<int []>();
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
    Check if the specified square is a corner.

    @param row The row of the square.
    @param col The column of the square.

    @return Whether or not the square is a corner.
    */
  private boolean inCornerLocation(int row, int col) {
    for (int i = 0; i < CORNER_SQUARE_POSITIONS.length; i++) {
      int cornerRow = CORNER_SQUARE_POSITIONS[i][0];
      int cornerCol = CORNER_SQUARE_POSITIONS[i][1];
      if (row == cornerRow && col == cornerCol) {
        return true;
      }
    }

    return false;
  }

  /**
    Check if the specified square is a king only position.

    @param row The row of the square.
    @param col The column of the square.

    @return Whether or not the square is special.
    */
  private boolean inSpecialLocation(int row, int col) {
    for (int i = 0; i < SPECIAL_SQUARE_POSITIONS.length; i++) {
      int specialRow = SPECIAL_SQUARE_POSITIONS[i][0];
      int specialCol = SPECIAL_SQUARE_POSITIONS[i][1];
      if (row == specialRow && col == specialCol) {
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
  private boolean isPathClear(int row, int col) throws GridOutOfBoundsException {
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
          if (!square(r, c).isEmpty()) {
            return false;
          }
        }
      }
    }

    return true;
  }

  /**
    Check if the current opposing player loses on account of repeat moves.

    @return Whether or not the opponent loses on repeat moves.
    */
  private boolean tooManyRepeats() {
    // Check if the last six moves are back and fourth.
    ListIterator<int []> moves = !isAttackerTurn()
        ? attackerMoves.listIterator(0) : defenderMoves.listIterator(0);
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
    Return if too many moves have been made without capture.

    @return Whether the game is a draw.
    */
  public boolean isDraw() {
    return movesWoCapture >= maxMovesWoCapture;
  }

  /**
    Return if the proposed move is valid.

    @return If the move is valid.
  */
  private boolean isValidMove(int row, int col) throws GridOutOfBoundsException {
    // Make sure piece other than king isn't moving to throne or four corners.
    if (!square(selRow, selCol).isKing() && inSpecialLocation(row, col)) {
      return false;
    }

    // Check for a clear path.
    if (!isPathClear(row, col)) {
      return false;
    }

    return true;
  }

  /**
    Executes the move and tracks state related to the move.

    @param row The row of the square.
    @param col The column of the square.
  */
  private void processMove(int row, int col) throws GridOutOfBoundsException {
    // If there is no conflict, move the piece, deselect, and end turn.
    board[row][col] = square(selRow, selCol);
    board[selRow][selCol] = GridSquareState.EMPTY;

    // If piece is king and no conflict, update king location.
    if (square(selRow, selCol).isKing()) {
      kingRow = row;
      kingCol = col;
    }
    // Track the move.
    LinkedList<int []> moves = isAttackerTurn() ? attackerMoves : defenderMoves;
    if (moves.size() > 5) {
      moves.removeFirst();
      moves.add(new int [] {row, col});
    } else {
      moves.add(new int [] {row, col});
    }
  }

  /**
    Handles if a game winning or losing move was made.

    @param row The row of the square.
    @param col The column of the square.
  */
  private void handleEndMove(int row, int col) throws GridOutOfBoundsException {
    // Check to see if move was winning move.
    if (isGameOver()) {
      // King was captured.
    } else if (square(row, col).isKing() && inCornerLocation(row, col)) {
      // If the king escaped we won.
      setGameOver(true);
    } else {
      setAttackerTurn(!isAttackerTurn());

      // If we made too many repeat moves the enemy wins.
      if (tooManyRepeats()) {
        setGameOver(true);
      }

      // If we made 50 moves without a capture, end the game due to a draw.
      if (isDraw()) {
        setGameOver(true);
      }
    }
  }

  /**
    Attempt to move a piece.

    @param row The row of the square to move to.
    @param col The column of the square to move to.

    @return Whether or not the selected piece was moved.
   */
  public boolean move(int row, int col) throws GridOutOfBoundsException {
    // Verify there is a selection and only one axis differs.
    if (!hasSelection() || ((row != selRow) == (col != selCol))) {
      return false;
    }

    if (!isValidMove(row, col)) {
      return false;
    }

    processMove(row, col);

    // Try to capture pieces. If nothing was captured, increment the counter
    // for moves without a capture.
    if (!capture(row, col)) {
      ++movesWoCapture;
    }

    handleEndMove(row, col);

    // Reset selection.
    selRow = -1;
    selCol = -1;

    return true;
  }

  /**
     Check to see if there is any captures as a result of a move.

     @param row The destination row of the move.
     @param column The destination column of the move.
   */
  public boolean capture(int row, int column) throws GridOutOfBoundsException {
    boolean captured = false;

    // Try the basic captures.
    captured |= basicCapture(row, column, row - 2, column);
    captured |= basicCapture(row, column, row + 2, column);
    captured |= basicCapture(row, column, row, column - 2);
    captured |= basicCapture(row, column, row, column + 2);

    captured |= kingCapture();
    // TODO: Fort captures.

    return captured;
  }

  /**
    Check to see if the king was captured.

    @return Whether or not king was captured.
   */
  private boolean kingCapture() throws GridOutOfBoundsException {
    boolean captured = false;

    //Check if king is not near edge of board.
    if (kingRow != 0 && kingRow != 10 && kingCol != 0 && kingCol != 10) {
      // Check if king is surrounded by attackers.
      if (square(kingRow - 1, kingCol).isAttacking()
            && square(kingRow + 1, kingCol).isAttacking()
            && square(kingRow, kingCol - 1).isAttacking() 
            && square(kingRow, kingCol + 1).isAttacking()) {
        captured = true;
      }
    }  

    if (captured) {
      board[kingRow][kingCol] = GridSquareState.EMPTY;
      kingRow = -1;
      kingCol = -1;
      setGameOver(true);
    }

    return captured;
  }

  /**
    Check to see if there is a basic capture between two pieces.

    @param rowA The row of the first piece.
    @param colA The second of the second piece.
    @param rowB The row of the second piece.
    @param colB The column of the second piece.
  */
  private boolean basicCapture(int rowA, int colA, int rowB, int colB) {
    // Make sure the two pieces differ by exactly two on one axis
    // and that the other axis is unchanged
    if ((Math.abs(rowA - rowB) != 2 || colA != colB)
        && (Math.abs(colA - colB) != 2 || rowA != rowB)) {
      return false;
    }

    // Retrieve the state of the squares. If they are out
    // of bounds, EMPTY will be returned.
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

    // Test to see if pieceC can be captured.
    boolean specialA = inSpecialLocation(rowA, colA);
    boolean specialB = inSpecialLocation(rowB, colB);
    boolean captured = basicCaptureTest(pieceA, pieceB, pieceC, specialA, specialB);

    // If captured is greater than zero, capture the square.
    if (captured) {
      board[rowC][colC] = GridSquareState.EMPTY;
    }
    
    return captured;
  }

  /**
     Test to see if two pieces (or a piece and an empty special square) can capture
     a piece.
     
     @param pieceA One of the two capturing pieces (must be EMPTY for special captures).
     @param pieceB One of the two capturing pieces (must be EMPTY for special captures).
     @param pieceC The piece to try to capture.
     @param specialA Whether or not A is a special square.
     @param specialB Whether or not B is a special square.
   */
  private boolean basicCaptureTest(GridSquareState pieceA, GridSquareState pieceB,
      GridSquareState pieceC, boolean specialA, boolean specialB) {
    
    // Two attackers capturing a defender.
    if (pieceA.isAttacking() && pieceB.isAttacking() && pieceC.isDefender()) {
      return true;
    }
    
    // Two defenders or a defender and king capturing an attacker.
    if (pieceA.isDefending() && pieceB.isDefending() && pieceC.isAttacking()) {
      return true; 
    }
    
    // An attacker and a special capturing a defender.
    if (pieceA.isEmpty() && specialA && pieceB.isAttacking() && pieceC.isDefender()) {
      return true;
    }
      
    // A defender and a special or a king and a special capturing an attacker.
    if (pieceA.isEmpty() && specialA && pieceB.isDefending() && pieceC.isAttacking()) {
      return true;
    }
    
    // An attacker and a special capturing a defender.
    if (pieceB.isEmpty() && specialB && pieceA.isAttacking() && pieceC.isDefender()) {
      return true;
    }
    
    // A defender and a special or a king and a special capturing an attacker.
    if (pieceB.isEmpty() && specialB && pieceA.isDefending() && pieceC.isAttacking()) {
      return true;
    }

    return false;
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
    attackerMoves.clear();
    defenderMoves.clear();
    movesWoCapture = 0;
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
          kingRow = r;
          kingCol = c;
        } else {
          board[r][c] = GridSquareState.EMPTY;
        }
      }
    }
  }

  /**
    Write out the simple information about the board state.
  */
  private void writeState(PrintWriter pw) {
    pw.println(movesWoCapture);
    pw.println(attackerTurn);
  }

  /**
    Writes out all of the recorded moves thus far. Used for loading in data
    required for calculating repeat moves.
  */
  private void writeStoredMoves(PrintWriter pw) {
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
  */
  private void writeAsciiBoard(PrintWriter pw) {
    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        if (board[r][c].isAttacking()) {
          pw.print('A');
        } else if (board[r][c].isDefender()) {
          pw.print('D');
        } else if (board[r][c].isKing()) {
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
    
    @param fileName String to save the board to
  */
  public boolean saveBoard(String fileName) {
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
      writeState(pw);
      writeStoredMoves(pw);
      writeAsciiBoard(pw);
      
      pw.close();
    } catch (Exception ex) {
      return false;
    }
    
    return true;
  }
  
  /**
    Loads basic state information from the scanner provided.
  */
  private void readState(Scanner sc) {
    movesWoCapture = Integer.parseInt(sc.nextLine());
    attackerTurn = Boolean.parseBoolean(sc.nextLine());
  }

  /**
    Loads recorded moves from the scanner provided. Used for calculating
    repeat moves.
  */
  private void readStoredMoves(Scanner sc) {
    attackerMoves.clear();
    defenderMoves.clear();

    int numAttacks = Integer.parseInt(sc.nextLine());
    int numDefends = Integer.parseInt(sc.nextLine());

    for (int i = 0; i < numAttacks; i++) {
      String currLine = sc.nextLine();
      int row = Character.getNumericValue(currLine.charAt(1));
      int col = Character.getNumericValue(currLine.charAt(2));
      attackerMoves.add(new int[] {row, col});
    }

    for (int i = 0; i < numDefends; i++) {
      String currLine = sc.nextLine();
      int row = Character.getNumericValue(currLine.charAt(1));
      int col = Character.getNumericValue(currLine.charAt(2));
      defenderMoves.add(new int[] {row, col});
    }
  }

  /**
    Reads in the board layout from the scanner provided.
  */
  private void readAsciiBoard(Scanner sc) {
    for (int r = 0; r < GRID_ROW_MAX + 1; r++) {
      String currLine = sc.nextLine();
      for (int c = 0; c < GRID_COL_MAX + 1; c++) {
        char currChar = currLine.charAt(c);

        if (currChar == 'A') {
          board[r][c] = GridSquareState.ATTACKER;
        } else if (currChar == 'D') {
          board[r][c] = GridSquareState.DEFENDER;
        } else if (currChar == 'K') {
          board[r][c] = GridSquareState.KING;
          kingRow = r;
          kingCol = c;
        } else {
          board[r][c] = GridSquareState.EMPTY;
        }
      }
    }
  }

  /**
    Takes an instance of a board from a saved game.
    
    @param fileName String of board to load from saved file
  */
  public boolean loadBoardFromSave(String fileName) {
    String pathName = "saved_games/" + fileName + ".txt";
    
    try {
      Scanner fileReader = new Scanner(new File(pathName));
      readState(fileReader);
      readStoredMoves(fileReader);
      readAsciiBoard(fileReader);
      
      fileReader.close();
    } catch (FileNotFoundException ex) {
      return false;
    } catch (Exception ex) {
      return false;
    }
    
    return true;
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

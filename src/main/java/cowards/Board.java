package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Board extends BoardLayout {
  /**
    The amount of seconds to initialize the timers to. By default, this
    is 300 seconds or 5 minutes.
   */
  private static int TIMER_INITIAL = 300;

  /**
    The amount of seconds to append to the timers after a turn has been
    ended. By default, this is 3 seconds.
   */
  private static int TIMER_APPEND = 3;

  /**
    Set the amount of seconds to initialize timers to. Any value less than
    or equal to zero will be set as the default of 300 seconds.

    @param initial The new amount of seconds to initialize timers to.
   */
  public static void setTimerInitial(int initial) {
    TIMER_INITIAL = initial > 0 ? initial : 300;
  }  
  
  /**
    Set the amount of seconds to append to the timers after each turn. Any
    negative value will be treated as the default of 3 seconds.

    NOTE: Unlike setTimerInitial, zero is not treated as the default value
    since zero is a valid amount of seconds to append.

    @param append The new amount of seconds to initialize timers to.
   */
  public static void setTimerAppend(int append) {
    TIMER_APPEND = append >= 0 ? append : 3;
  }

  /**
    The internal game board states.
   */
  private GridSquareState[][] board;

  /**
    Whether or not it is the attacker's turn.
   */
  private boolean attackerTurn = true;

  /**
    Whether or not the game is over.
   */
  private boolean gameOver = false;

  /**
    The list of attacker's moves.
   */
  private LinkedList<int []> attackerMoves;

  /**
    The list of defender's moves.
   */
  private LinkedList<int []> defenderMoves;

  /**
    The list of attackers.
   */
  private LinkedList<int []> attackers;

  /**
    The list of defenders.
   */
  private LinkedList<int []> defenders;

  /**
    The maximum number of moves allowed without a capture.
   */
  private final int maxMovesWoCapture = 50;

  /**
    The current number of moves without a capture.
   */
  private int movesWoCapture = 0;

  /**
    The currently selected row.
   */
  private int selRow = -1;

  /**
    The currently selected column.
   */
  private int selCol = -1;

  /**
    The current row containing the king.
   */
  private int kingRow = 5;

  /**
    The current column containing the king.
   */
  private int kingCol = 5;

  /**
    The timer for the attacking side.
   */
  private BoardTimer attackerTimer;

  /**
    The timer for the defending side.
   */
  private BoardTimer defenderTimer;

  /**
    The defensive board positions and their usage counts.
   */
  private HashMap<String, Integer> defensive;

  /**
    Constructor.
  */
  public Board() throws BadAsciiBoardFormatException {
    this(INITIAL_BOARD);
    initializeTimers(TIMER_INITIAL, TIMER_INITIAL, TIMER_APPEND, attackerTurn);
  }

  /**
    Copy constructor.

    @param orig The board from which we are copying.
    @param createTimer If the board should be equipped with a timer.
   */
  public Board(Board orig, boolean createTimer) {
    board          = orig.getBoard();
    attackerMoves  = orig.getAttMoves();
    defenderMoves  = orig.getDefMoves();
    attackers      = orig.getAttackers();
    defenders      = orig.getDefenders();
    movesWoCapture = orig.getMovesWoCapture();
    kingRow        = orig.getKingRow();
    kingCol        = orig.getKingCol();
    attackerTurn   = orig.isAttackerTurn();
    gameOver       = orig.isGameOver();
    defensive      = new HashMap<String, Integer>(orig.getDefensiveBoardPositions());

    // The AI does not need to track every child board's timer.
    if (createTimer) {
      initializeTimers(orig.getAttackerTimer().getTimeRemaining(),
          orig.getDefenderTimer().getTimeRemaining(),
          orig.getAttackerTimer().getTimeAppended(),
          attackerTurn);
    }
  }

  /**
    Constructor.
   */
  public Board(GridSquareState[][] innerBoard, LinkedList<int []> am,
      LinkedList<int []> dm, int mwoCap, int kr, int kc, boolean at,
      int atc, int dtc, int append, HashMap<String, Integer> dbp) {
    board = innerBoard;

    attackerMoves = am;
    defenderMoves = dm;

    attackers = BoardProcessor.findAllAttackers(board);
    defenders = BoardProcessor.findAllDefenders(board);

    movesWoCapture = mwoCap;
    kingRow        = kr;
    kingCol        = kc;
    attackerTurn   = at;
    gameOver       = false;

    defensive      = dbp;

    initializeTimers(atc, dtc, append, attackerTurn);
  }

  /**
    Constructor for building a board out of an initial configuration.

    Throws an exception if the board is poorly formatted.

    @param charBoard Character AoA for ascii representation of board.
    */
  public Board(char[][] charBoard) throws BadAsciiBoardFormatException {
    board = new GridSquareState[GRID_ROW_MAX + 1][GRID_COL_MAX + 1];
    attackerMoves = new LinkedList<int []>();
    defenderMoves = new LinkedList<int []>();
    if (charBoard == null
        || charBoard.length < GRID_ROW_MAX + 1
        || charBoard[0].length < GRID_COL_MAX + 1) {
      throw new BadAsciiBoardFormatException();
    }

    attackerTurn = true;
    gameOver = false;
    attackerMoves.clear();
    defenderMoves.clear();

    movesWoCapture = 0;
    kingRow = 5;
    kingCol = 5;
    defensive = new HashMap<String, Integer>();

    for (int r = 0; r < 11; ++r) {
      for (int c = 0; c < 11; ++c) {
        char square = charBoard[r][c];
        board[r][c] = BoardLoader.charToState(square);
        if (square == 'K') {
          kingRow = r;
          kingCol = c;
        }
      }
    }

    attackers = BoardProcessor.findAllAttackers(board);
    defenders = BoardProcessor.findAllDefenders(board);

    initializeTimers(TIMER_INITIAL, TIMER_INITIAL, TIMER_APPEND, attackerTurn);
  }

  /**
    Initialize the timers.

    @param curAtt The time to start the attacker's timer at.
    @param curDef The time to start the defender's timer at.
    @param append The amount of time to append after each turn.
    @param stAtt Whether to start the attacker's (true) or defender's (false) timer.
   */
  private void initializeTimers(int curAtt, int curDef, int append, boolean stAtt) {
    // Initialize timers.
    attackerTimer = new BoardTimer(curAtt, append);
    defenderTimer = new BoardTimer(curDef, append);

    // Start the appropriate timer.
    if (stAtt) {
      attackerTimer.start();
    } else if (!stAtt) {
      defenderTimer.start();
    }

    // Check to make sure neither timer hits zero.
    final java.util.Timer timer = new java.util.Timer();
    timer.scheduleAtFixedRate(new java.util.TimerTask() {
      public void run() {
        // If paused, return.
        if (isPaused()) {
          return;
        }

        // If the game is over, kill the timers, kill this timer, and return.
        if (isGameOver()) {
          attackerTimer.kill();
          defenderTimer.kill();
          timer.cancel();
          return;
        }

        // If either timer hits zero, end the game.
        if (isAttackerTurn() && attackerTimer.getTimeRemaining() <= 0) {
          gameOver = true;
          attackerTurn = false;
        } else if (!isAttackerTurn() && defenderTimer.getTimeRemaining() <= 0) {
          gameOver = true;
          attackerTurn = true;
        }
      }
    }, 1, 500);
  }

  /**
    Pause the timers. This is useful for save and load game operations where a
    dialog has focus.
    */
  public void pauseTimers() {
    if (attackerTimer != null) {
      attackerTimer.stop(true);
    }
    if (defenderTimer != null) {
      defenderTimer.stop(true);
    }
  }

  /**
    Resume the timers. This is useful for save and load game operations where a
    dialog has focus.
   */
  public void resumeTimers() {
    if (!gameOver) {
      BoardTimer timer = attackerTurn ? attackerTimer : defenderTimer;
      if (timer != null) {
        timer.start();
      }
    }
  }

  /**
    Check whether the timers are paused.
   */
  public boolean isPaused() {
    return (!isGameOver()
        && attackerTimer != null && !attackerTimer.isCountingDown()
        && defenderTimer != null && !defenderTimer.isCountingDown());
  }

  /**
    Returns a copy of the LinkedList describing the recent attacker move
    history.
   */
  public LinkedList<int []> getAttMoves() {
    LinkedList<int []> ret = new LinkedList<int []>();
    for (int i = 0; i < attackerMoves.size(); ++i) {
      ret.add(attackerMoves.get(i).clone());
    }

    return ret;
  }

  /**
    Returns a copy of the LinkedList describing the recent defender move
    history.
   */
  public LinkedList<int []> getDefMoves() {
    LinkedList<int []> ret = new LinkedList<int []>();
    for (int i = 0; i < defenderMoves.size(); ++i) {
      ret.add(defenderMoves.get(i).clone());
    }

    return ret;
  }

  /**
    Returns a the LinkedList describing the attacker positions.
   */
  public LinkedList<int []> getAttackers() {
    LinkedList<int []> ret = new LinkedList<int []>();
    for (int i = 0; i < attackers.size(); ++i) {
      ret.add(attackers.get(i).clone());
    }
    return ret;
  }

  /**
    Returns a the LinkedList describing the defenders positions.
   */
  public LinkedList<int []> getDefenders() {
    LinkedList<int []> ret = new LinkedList<int []>();
    for (int i = 0; i < defenders.size(); ++i) {
      ret.add(defenders.get(i).clone());
    }
    return ret;
  }

  /**
    Returns a copy of the internal board.
   */
  public GridSquareState[][] getBoard() {
    GridSquareState[][] ret = new GridSquareState[11][11];
    for (int i = 0; i <= GRID_ROW_MAX; ++i) {
      for (int j = 0; j <= GRID_COL_MAX; ++j) {
        ret[i][j] = board[i][j];
      }
    }

    return ret;
  }

  /**
    Return the number of moves without capture.
   */
  public int getMovesWoCapture() {
    return movesWoCapture;
  }

  /**
    Retrieve the defensive board positions and their usage counts.
   */
  public HashMap<String, Integer> getDefensiveBoardPositions() {
    return defensive;
  }

  /**
    Return the king's row.
   */
  public int getKingRow() {
    return kingRow;
  }

  /**
    Get the king's column.
   */
  public int getKingCol() {
    return kingCol;
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
    // Set the turn.
    attackerTurn = turn;

    if (attackerTurn) {
      // If the defender's timer is running, stop it.
      if (defenderTimer != null && defenderTimer.isCountingDown()) {
        defenderTimer.stop(false);
      }

      // Start the attacker's timer.
      if (attackerTimer != null) {
        attackerTimer.start();
      }
    } else {
      // If the attacker's timer is running, stop it.
      if (attackerTimer != null && attackerTimer.isCountingDown()) {
        attackerTimer.stop(false);
      }

      // Start the defender's timer.
      if (defenderTimer != null) {
        defenderTimer.start();
      }
    }
  }

  /**
    Retrieve the timer for the attacker's side.
   */
  public BoardTimer getAttackerTimer() {
    return attackerTimer;
  }

  /**
    Retrieve the timer for the defender's side.
   */
  public BoardTimer getDefenderTimer() {
    return defenderTimer;
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
  public boolean select(int row, int col) throws GridOutOfBoundsException {
    switch (square(row, col)) {
      // The attacking side can only select attacker pieces.
      case ATTACKER:
        if (isAttackerTurn()) {
          selRow = row;
          selCol = col;
          return true;
        }
        return false;

      // The defending side can only select defender pieces and the king.
      case DEFENDER:
      case KING:
        if (!isAttackerTurn()) {
          selRow = row;
          selCol = col;
          return true;
        }

        return false;

      // For clarity sake, include the default no-op case.
      default:
        return false;
    }
  }

  /**
    Check if the specified square is along the edge. 
    Corners are excluded since this is a helper function for shieldwall capture only.

    @param row The row of the square.
    @param col The column of the square.

    @return Which edge: "Left", "Right", "Top", "Bottom" square is on
            OR "NE" for not edge. 
  */
  public String inEdgeLocation(int row, int col) {
    if (col == 0 && row != 0 && row != 10) {
      return "Left";
    } else if (col == 10 && row != 0 && row != 10) {
      return "Right";
    } else if (row == 0 && col != 0 && col != 10) {
      return "Top";
    } else if (row == 10 && col != 0 && col != 10) {
      return "Bottom";
    } else {
      return "NE";
    }
  }

  /**
    Check if the specified square is a corner.

    @param row The row of the square.
    @param col The column of the square.

    @return Whether or not the square is a corner.
    */
  public boolean inCornerLocation(int row, int col) {
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
  public boolean inSpecialLocation(int row, int col) {
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
  public boolean isPathClear(int row, int col) throws GridOutOfBoundsException {
    return BoardProcessor.isPathClear(this, row, col, selRow, selCol);
  }

  /**
    Check if the current opposing player loses on account of repeat moves.

    @return Whether or not the opponent loses on repeat moves.
    */
  private boolean tooManyRepeats() {
    // Check if the last six moves are back and fourth.
    ListIterator<int []> moves = !isAttackerTurn()
        ? attackerMoves.listIterator(0) : defenderMoves.listIterator(0);

    return BoardProcessor.tooManyRepeats(moves);
  }

  /**
    Return if too many moves have been made without capture.

    @return Whether the game is a draw.
    */
  public boolean isDraw() {
    return movesWoCapture >= maxMovesWoCapture;
  }

  /**
    Remove the piece that was selected from the list of pieces being tracked.
    */
  private void removeSelFromPieceList(
      LinkedList<int []> pieceList, int remRow, int remCol) {
    Iterator<int []> lit = pieceList.iterator();
    while (lit.hasNext()) {
      int [] pt = lit.next();
      if (pt[0] == remRow && pt[1] == remCol) {
        lit.remove();
        break;
      }
    }
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

    LinkedList<int []> pieceList = isAttackerTurn() ? attackers : defenders;
    removeSelFromPieceList(pieceList, selRow, selCol);
    pieceList.add(new int [] {row, col});

    // If piece is king and no conflict, update king location.
    if (square(row, col).isKing()) {
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
      // King was captured or timer ran out.
    } else if (square(row, col).isKing() && inCornerLocation(row, col)) {
      // If the king escaped we won.
      setGameOver(true);
    } else if (isAttackerTurn() && BoardProcessor.isSurrounded(this)) {
      // The defending side is surrounded by the attackers.
      setGameOver(true);
    } else if (!isAttackerTurn() && BoardProcessor.storeDefensiveBoard(this)) {
      // The defensive board position has been used three times without a capture.
      // The defending side loses.
      setAttackerTurn(true);
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

      // If there are no possible moves, end the game and opponent wins.
      if (!BoardProcessor.areMovesAvailable(this)) {
        setAttackerTurn(!isAttackerTurn());
        setGameOver(true);
      }

      // If an exit fort occurs, end the game and the defenders win.
      if (isExitFort()) {
        setAttackerTurn(false);
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

    if (!BoardProcessor.isValidMove(this, row, col, selRow, selCol)) {
      return false;
    }

    processMove(row, col);

    // Try to capture pieces. If nothing was captured, increment the counter
    // for moves without a capture.
    if (!capture(row, col)) {
      ++movesWoCapture;
    } else {
      movesWoCapture = 0;
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
  private boolean capture(int row, int column) throws GridOutOfBoundsException {
    boolean captured = false;

    // Try the basic captures.
    captured |= basicCapture(row, column, row - 2, column);
    captured |= basicCapture(row, column, row + 2, column);
    captured |= basicCapture(row, column, row, column - 2);
    captured |= basicCapture(row, column, row, column + 2);

    captured |= kingCapture();
    captured |= shieldWallCapture(row, column);

    return captured;
  }

  /**
    Check to see if the king was captured.

    @return Whether or not king was captured.
   */
  private boolean kingCapture() throws GridOutOfBoundsException {
    boolean captured = BoardProcessor.kingCapture(this);
    if (captured) {
      board[kingRow][kingCol] = GridSquareState.EMPTY;
      removeSelFromPieceList(defenders, kingRow, kingCol);
      kingRow = -1;
      kingCol = -1;
      setGameOver(true);
    }

    return captured;
  }

  /**
    Check to see if move caused a shieldwall capture.

    @param row The row of the move.
    @param column The column of the move.

    @return Whether or not a shieldwall capture occurred.
  */
  private boolean shieldWallCapture(int row, int column) {
    // Must be a move to an edge to be a shieldwall capture.
    String edge = inEdgeLocation(row, column);
    if (edge.equals("NE")) {
      return false;
    }

    boolean shieldWall = false;
    // Must be a flanking move that brackets the group of pieces at both ends.
    if (edge.equals("Left") || edge.equals("Right")) {
      // Check vertically for a shieldwall.
      shieldWall |= isShieldWall(edge, "Up", row, column);
      shieldWall |= isShieldWall(edge, "Down", row, column);
    } else {
      // Check horizontally for a shieldwall.
      shieldWall |= isShieldWall(edge, "Left", row, column);
      shieldWall |= isShieldWall(edge, "Right", row, column);
    }
    return shieldWall;
  }

  /**
    Check in specified direction if a shieldwall formed.

    @param direction The direction to look for a shieldwall in.
    @param row The row of the move.
    @param col The column of the move.

    @return Whether or not a shieldwall was formed.
  */
  private boolean isShieldWall(String edge, String direction, int row, int col) {
    // Last moved piece is attacker?
    boolean shieldAttack = safeSquare(row, col).isAttacking();

    GridSquareState curPiece;
    GridSquareState wallPiece;
    boolean shieldWall = false;
    int opponentCount = 0;

    int rowDelta = 0;
    int colDelta = 0;
    if (direction.equals("Up")) {
      rowDelta = -1;
    } else if (direction.equals("Down")) {
      rowDelta = 1;
    } else if (direction.equals("Left")) {
      colDelta = 1;
    } else {
      colDelta = -1;
    }

    int curRow = row + rowDelta;
    int curCol = col + colDelta;
    curPiece = safeSquare(curRow, curCol); 

    // Move one square at a time checking for shield wall.
    while (!curPiece.isEmpty()) {
      // Determine which side wall might be on.
      if (edge.equals("Left")) {
        wallPiece = safeSquare(curRow, col + 1);
      } else if (edge.equals("Right")) {
        wallPiece = safeSquare(curRow, col - 1);
      } else if (edge.equals("Top")) {
        wallPiece = safeSquare(row + 1, curCol);
      } else {
        wallPiece = safeSquare(row - 1, curCol);
      }
      
      boolean multType = shieldAttack
          ? curPiece.isDefending() : curPiece.isAttacking();
      boolean wallType = shieldAttack
          ? wallPiece.isAttacking() : wallPiece.isDefending();
      // Check for multiple attackers.
      if (multType) {
        // Check if defender wall continues.
        if (wallType) {
          opponentCount++;
        } else {
          // Break if shield wall not present.
          break;
        }
      } else if ((!multType && opponentCount >= 2)) {
        // Shield wall exists.
        shieldWall = true;
        break;
      }

      // Determine direction to traverse.
      curRow += rowDelta;
      curCol += colDelta;
      curPiece = safeSquare(curRow, curCol); 
    }

    // Check for corner shield wall capture
    shieldWall |= (inCornerLocation(curRow, curCol) && opponentCount >= 2);

    // Change captured pieces to empty.
    if (shieldWall) {
      curRow -= rowDelta;
      curCol -= colDelta;
      while (row != curRow || col != curCol) {
        if (!board[curRow][curCol].isKing()) {
          board[curRow][curCol] = GridSquareState.EMPTY;
          removeSelFromPieceList(defenders, curRow, curCol);
        }
        curRow -= rowDelta;
        curCol -= colDelta;
      }
    }
    return shieldWall;
  }  

  /**
    Check to see if exit fort occurred.

    @return Whether or not exit fort was formed.
  */
  public boolean isExitFort() {
    // Exit fort can only occur if king is on the edge of the board.
    return !inEdgeLocation(kingRow, kingCol).equals("NE")
        && checkKingMove() && BoardProcessor.isKingGuarded(this);
  }

  /**
    Check if king has room to move. 

    @return Whether or not king has room to move.
  */
  public boolean checkKingMove() {
    int [] above = new int[] {kingRow + 1, kingCol};
    int [] below = new int[] {kingRow - 1, kingCol};
    int [] left  = new int[] {kingRow, kingCol - 1};
    int [] right = new int[] {kingRow, kingCol + 1};

    // Check if king has room to move either left, right, up, or down.
    return BoardProcessor.inBounds(left) && safeSquare(left[0], left[1]).isEmpty()
        || BoardProcessor.inBounds(right) && safeSquare(right[0], right[1]).isEmpty()
        || BoardProcessor.inBounds(above) && safeSquare(above[0], above[1]).isEmpty()
        || BoardProcessor.inBounds(below) && safeSquare(below[0], below[1]).isEmpty();
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
      LinkedList<int []> pieceList = isAttackerTurn() ? defenders : attackers;
      removeSelFromPieceList(pieceList, rowC, colC);
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
  protected GridSquareState safeSquare(int row, int column) {
    GridSquareState state = GridSquareState.EMPTY;

    try {
      state = square(row, column);
    } catch (GridOutOfBoundsException exception) {
      // Quietly ignore. Treat as default (empty).
    }

    return state;
  }
}

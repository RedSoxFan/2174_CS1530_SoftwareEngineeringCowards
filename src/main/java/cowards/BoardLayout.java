package cowards;

/**
  Class encapsulating grid based attributes of a board.
*/
public class BoardLayout {
  public static enum GridSquareState {
    EMPTY, KING, DEFENDER, ATTACKER;

    /**
       Convenience method for checking whether or not the piece is on the
       attacking side.
     */
    public boolean isAttacking() {
      return this.equals(ATTACKER);
    }

    /**
       Convenience method for checking whether or not the piece is a defender.
     */
    public boolean isDefender() {
      return this.equals(DEFENDER);
    }

    /**
       Convenience method for checking whether or not the piece is a king.
     */
    public boolean isKing() {
      return this.equals(KING);
    }

    /**
       Convenience method for checking whether or not the piece is on the defending
       side. This includes both the defenders and the king.
     */
    public boolean isDefending() {
      return isDefender() || isKing();
    }

    /**
       Convenience method for checking whether or not the piece is empty.
     */
    public boolean isEmpty() {
      return this.equals(EMPTY);
    }
  }
  
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

  public static final int[][] CORNER_SQUARE_POSITIONS = new int[][]{
    {0, 0},
    {0, 10},
    {10, 0},
    {10, 10},
  };
}

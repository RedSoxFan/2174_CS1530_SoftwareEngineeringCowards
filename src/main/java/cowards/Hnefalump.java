package cowards;

/**
  Hnefatafl AI.
 */
public class Hnefalump {
  /**
    Uses a minimax tree to determine the next move. 

    Makes the interface to AI behavior neater.

    @param board The starting board to search for moves.
    @param depth The max depth of the tree to search.
   */
  public static int[] getNextMove(Board board, int depth) {
    return Minimax.minimaxDecision(new Node(board, null, 0), depth).getMove();
  }
}

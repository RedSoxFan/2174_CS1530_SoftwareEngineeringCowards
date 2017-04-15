package cowards;

import java.lang.Math;
import java.util.*;

/**
  AB Minimax tree supporting the Hnefalump module.

  This module contains all of the core AI functionality. The Hnefalump module
  is just a pretty interface for the rest of the program.

  Minimax AB trees minimize branches explored in a given set of possible moves
  by ignoring the actions that would yield worse results than what we have
  already seen. 

  Some terms:

  vee/v   - The node representing the best value (utility) for the current frame.
  utility - A double value representing what we think a resulting board
            configuration is worth.

            Note that the utility can be negative. Low values benefit
            attackers, and high values benefit the defenders. This makes it
            easier to act based on the scores.
 */
public class Minimax {

  /** Constructor. This is a static class. */
  private Minimax() {
  }

  /** Returns the node containing the correct starting vee value. */
  public static Node startV(boolean isMax) {
    return isMax ? Node.getNegInf() : Node.getInf();
  }

  /** Returns the euclidian distance of the king to the closest corner. */
  public static double kingBestCorner(Board board) {
    int kr = board.getKingRow();
    int kc = board.getKingCol();

    // figure out closest corner
    boolean north = kr <= 5;
    boolean west  = kc <= 5;
    double vdistance = Math.abs(kr - (north ? 0 : 10));
    double hdistance = Math.abs(kc - (west ? 0 : 10));
    return Math.sqrt(vdistance * vdistance + hdistance * hdistance);
  }

  /** Returns the score of the board. High numbers are good for defenders. */
  public static double utility(Board board) {
    // This is a very simple heuristic. A pro Hnefatafl player could improve it.
    if (board.isGameOver() && board.isAttackerTurn()) {
      // Obvious win for attackers should be prioritized.
      return -100;
    }
    if (board.isGameOver() && !board.isAttackerTurn()) {
      // Obvious win for defenders should be prioritized.
      return 100;
    }

    // Take the difference of the two sides and figure out the king's nearest
    // corner, and his minimum moves there. This heuristic portion is inspired
    // by the paper Evolving Players for an Ancient Game: Hnefatafl.
    return (board.getDefenders().size() - board.getAttackers().size())
        + (7.071 - kingBestCorner(board));
  }

  /** Adds move (rw, cl) to the list if valid. */
  public static void expandMove(
      Node parent, Board newBoard, LinkedList<Node> in, int [] pc, int rw, int cl)
      throws GridOutOfBoundsException {

    // Don't add any invalid moves.
    if (!newBoard.select(pc[0], pc[1]) || !newBoard.move(rw, cl)) {
      return;
    }

    // Add this possible move to the list of nodes.
    in.add(new Node(
        newBoard,
        parent.getMove() == null
            ? new int[] {rw, cl, pc[0], pc[1]} : parent.getMove(),
        utility(newBoard)
    ));
  }

  /** Permute all legal moves for the current side. */
  public static LinkedList<Node> expand(Node parent) {
    Board board = parent.getBoard();
    LinkedList<Node> ret = new LinkedList<Node>();
    LinkedList<int []> pieceList = board.isAttackerTurn()
        ? board.getAttackers() : board.getDefenders();

    // No possible moves.
    if (board.isGameOver()) {
      return ret;
    }

    try {
      // Go through all possible moves.
      // For every piece of our type:
      for (int[] p : pieceList) {
        // For every other square on the board.
        for (int i = 0; i < 11; ++i) {
          expandMove(parent, new Board(board, false), ret, p, i, p[1]);
          expandMove(parent, new Board(board, false), ret, p, p[0], i);
        }
      }
    } catch (GridOutOfBoundsException gx) {
      // This would be an obvious programmer error.
      System.err.println("AI ERROR: Tried to expand out of bounds.");
      return null;
    }

    return ret;
  }

  /** Prunes the children of the current frame. */
  private static void handlePrune(Frame cur) {
    if (cur.isMax) {
      if (cur.vee.utility() >= cur.beta.utility()) {
        // Prune the nodes below to save time.
        cur.children.clear();
      }
      if (cur.vee.utility() > cur.alpha.utility()) {
        // Update alpha.
        cur.alpha = cur.vee;
      }
    } else {
      if (cur.vee.utility() <= cur.alpha.utility()) {
        // Prune the nodes below to save time.
        cur.children.clear();
      }
      if (cur.vee.utility() < cur.beta.utility()) {
        // Update alpha.
        cur.beta = cur.vee;
      }
    }
  }

  /** Updates vee and the current node's utility if necessary. */
  private static void updateAbove(boolean isMax, Node node, Frame cur) {
    if ((isMax && node.utility() < cur.vee.utility())
        || (!isMax && node.utility() > cur.vee.utility())) {
      cur.vee = node;
      cur.node.setValue(node.utility());
    }
  }

  /** Non-recursively determines the best move using a Minimax algorithm. */
  public static Node minimaxDecision(Node root, int maxDepth) {
    // Stack init.
    Stack<Frame> stack = new Stack<Frame>();
    boolean isMax = !root.getBoard().isAttackerTurn();
    stack.push(new Frame(
        root, expand(root), isMax,
        Node.getNegInf(), Node.getInf(), startV(isMax)
    ));

    // Simulate recursion using a stack.
    while (!stack.empty()) {
      Frame cur = stack.peek();
      Node localVee = cur.vee;
      handlePrune(cur);

      // Stop if we hit max depth or lack children to evaluate.
      if (cur.children == null
          || (maxDepth != 0 && maxDepth < stack.size()) || cur.children.isEmpty()) {
        stack.pop();
        if (stack.empty()) {
          return localVee;
        }

        updateAbove(cur.isMax, cur.node, stack.peek());

        // Remove this node from the parent stack's list of kids to process.
        stack.peek().children.remove(cur.node);
        continue;
      }

      // Add the next child if possible.
      if (cur.children.size() > 0) {
        Node ch = cur.children.peek();
        stack.push(new Frame(
            ch, expand(ch), !cur.isMax, cur.alpha, cur.beta, startV(!cur.isMax)
        ));
      }
    }

    /* Shouldn't be possible to reach here. */
    return null;
  }
}

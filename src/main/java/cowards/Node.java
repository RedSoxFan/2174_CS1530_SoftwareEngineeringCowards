package cowards;

import java.util.*;

/**
  Node class that contains score data for the Minimax tree.

  A node is a move, the resulting board, and the associated score.
 */
public class Node {
  private int id;
  private double val;
  private boolean locked;

  // Move and board associated with this node, if any.
  private int[] move;
  private Board board;

  // These are used like singletons.
  private static Node inf;
  private static Node negInf;

  // Used to create unique IDs for each node.
  private static int nextId = 0;

  /** Constructor. */
  public Node(Board boardIn, int [] moveIn, double vl) {
    id = nextId++;
    val = vl;
    move = moveIn;
    board = boardIn;
  }

  /** Constructor. */
  private Node(int ident, double vl, boolean lock) {
    id = ident;
    val = vl;
    locked = lock;
  }

  /** Gives a node constructed to be the value infinity. */
  public static Node getInf() {
    if (inf == null) {
      inf = new Node(-1, Double.POSITIVE_INFINITY, true);
    }

    return inf;
  }

  /** Gives a node constructed to be the value negative infinity. */
  public static Node getNegInf() {
    if (inf == null) {
      negInf = new Node(-2, Double.NEGATIVE_INFINITY, true);
    }

    return negInf;
  }

  /** Returns the unique id of the node. */
  public int name() {
    return id;
  }

  /** Returns the value of the node. */
  public double utility() {
    return val;
  }

  /** Sets the value of the node. */
  public void setValue(double newVal) {
    val = newVal;
  }

  /** Gets the move that generated this node. */
  public int[] getMove() {
    if (locked) {
      return null;
    }

    return move;
  }

  /** Set the move for this node. */
  public void setMove(int[] moveIn) {
    if (locked) {
      return;
    }

    move = moveIn;
  }

  /** Returns the stored board. */
  public Board getBoard() {
    if (!locked) {
      return board;
    }

    return null;
  }
}

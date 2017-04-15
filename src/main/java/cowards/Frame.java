package cowards;

import java.util.*;

/** Struct class. */
public class Frame {
  Node node;
  LinkedList<Node> children;
  boolean isMax;
  Node alpha;
  Node beta;
  Node vee;
  
  /** Constructor. */
  public Frame(
      Node no, LinkedList<Node> kids,
      boolean max, Node al, Node be, Node veeIn) {
    node = no;
    children = kids;
    isMax = max;
    alpha = al;
    beta = be;
    vee = veeIn;
  }
}

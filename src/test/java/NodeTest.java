import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.Node;
import org.junit.Test;

/**
   Test methods associated with the node container class.
 */
public class NodeTest {
  /**
     Verify the infinity node is unique (singleton).
   */
  @Test
  public void uniqueInfTest() {
    Node inf = Node.getInf();
    Node inf2 = Node.getInf();
    assertEquals(inf, inf2);
  }

  /**
     Verify the negative infinity node is unique (singleton).
   */
  @Test
  public void uniqueNegInfTest() {
    Node ni = Node.getNegInf();
    Node ni2 = Node.getNegInf();
    assertEquals(ni, ni2);
  }

  /**
     Verify the name progression works for the node class.
   */
  @Test
  public void nameProgressionTest() {
    Node n1 = new Node(null, null, 0);
    Node n2 = new Node(null, null, 0);
    assertEquals(n1.name() + 1, n2.name());
  }

  /**
     Verify move retrieval works for the node class.
   */
  @Test
  public void moveFetchTest() {
    Node n1 = new Node(null, new int[] {0, 1}, 0);
    int[] ans = n1.getMove();
    assertEquals(0, ans[0]);
    assertEquals(1, ans[1]);
  }

  /**
     Verify move setting works for the node class.
   */
  @Test
  public void moveSetTest() {
    Node n1 = new Node(null, null, 0);
    n1.setMove(new int [] {0, 1});
    int[] ans = n1.getMove();
    assertEquals(0, ans[0]);
    assertEquals(1, ans[1]);
  }

  /**
     Verify value retrieval works for the node class.
   */
  @Test
  public void valFetchTest() {
    Node n1 = new Node(null, null, 5);
    assertEquals(5, n1.utility(), .001);
  }

  /**
     Verify value setting works for the node class.
   */
  @Test
  public void valSetTest() {
    Node n1 = new Node(null, null, 0);
    n1.setValue(5);
    assertEquals(5, n1.utility(), .001);
  }

  /**
     Verify board fetching works for the node class.
   */
  @Test
  public void boardFetchTest() {
    Board board = null;
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      fail();
    }

    Node n1 = new Node(board, null, 0);
    assertEquals(board, n1.getBoard());
  }
}

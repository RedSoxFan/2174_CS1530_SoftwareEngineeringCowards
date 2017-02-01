import static org.junit.Assert.*;

import cowards.Board;
import org.junit.Test;

public class BoardTest {
  @Test
  public void initPositionsTest() {
    Board board = new Board ();
    assertEquals(Board.GridSquareState.EMPTY, board.square (0, 0));
    assertEquals(Board.GridSquareState.KING, board.square (5, 5));
    assertEquals(Board.GridSquareState.DEFENDER, board.square (5, 1));
  }
}

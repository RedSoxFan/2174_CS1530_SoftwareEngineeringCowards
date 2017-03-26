import static org.junit.Assert.*;

import cowards.BadAsciiBoardFormatException;
import cowards.Board;
import cowards.BoardTimer;

import org.junit.Test;

public class TimerTest {
  /**
    Verify that the attacker's timer get initialized with the default settings.
   */
  @Test
  public void timerAttackerTest() {
    try {
      Board board = new Board();
      assertEquals(300, board.getAttackerTimer().getTimeRemaining());
      assertEquals(3, board.getAttackerTimer().getTimeAppended());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the defender's timer get initialized with the default settings.
   */
  @Test
  public void timerDefenderTest() {
    try {
      Board board = new Board();
      assertEquals(300, board.getDefenderTimer().getTimeRemaining());
      assertEquals(3, board.getDefenderTimer().getTimeAppended());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the game timer's can be paused.
   */
  @Test
  public void timerPauseTest() {
    try {
      Board board = new Board();
      assertFalse(board.isPaused());
      board.pauseTimers();
      assertTrue(board.isPaused());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the game timer's can be resumed.
   */
  @Test
  public void timerResumeTest() {
    try {
      Board board = new Board();
      board.pauseTimers();
      assertTrue(board.isPaused());
      board.resumeTimers();
      assertFalse(board.isPaused());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the timer's converting seconds to text correctly.
   */
  @Test
  public void timerTextualTest() {
    assertEquals("00:30", new BoardTimer(30, 3).getTimeRemainingAsText());
    assertEquals("01:00", new BoardTimer(60, 3).getTimeRemainingAsText());
    assertEquals("01:15", new BoardTimer(75, 3).getTimeRemainingAsText());
  }

  /**
    Verify the timer can be started.
   */
  @Test
  public void timerStartTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    assertFalse(timer.isCountingDown());
    timer.start();
    assertTrue(timer.isCountingDown());
  }

  /**
    Verify the timer can be stopped without skipping the append.
   */
  @Test
  public void timerStopWithoutSkipTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    timer.start();
    assertTrue(timer.isCountingDown());
    timer.stop(false);
    assertFalse(timer.isCountingDown());
  }

  /**
    Verify the timer can be stopped with skipping the append.
   */
  @Test
  public void timerStopWithSkipTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    timer.start();
    assertTrue(timer.isCountingDown());
    timer.stop(true);
    assertFalse(timer.isCountingDown());
  }

  /**
    Verify the if the timer is stopped without skipping the append, the
    seconds get appended.
   */
  @Test
  public void timerStopWithoutSkipAppendTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    timer.stop(false);
    assertEquals(303, timer.getTimeRemaining());
  }

  /**
    Verify the if the timer is stopped with skipping the append, the
    seconds do not get appended.
   */
  @Test
  public void timerStopWithSkipAppendTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    timer.stop(true);
    assertEquals(300, timer.getTimeRemaining());
  }

  /**
    Verify that the reconfigure works properly.
   */
  @Test
  public void timerReconfigureTest() {
    BoardTimer timer = new BoardTimer(300, 3);
    timer.start();
    timer.reconfigure(500, 7);
    assertEquals(500, timer.getTimeRemaining());
    assertEquals(7, timer.getTimeAppended());
    assertFalse(timer.isCountingDown());
  }

  /**
    Verify that the initial seconds for the timer can be set. This
    tests to make sure negatives are treated as the default.
   */
  @Test
  public void timerSetInitialNegativeTest() {
    try {
      Board.setTimerInitial(-1);
      Board board = new Board();
      assertEquals(300, board.getDefenderTimer().getTimeRemaining());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the initial seconds for the timer can be set. This
    tests to make sure zero is treated as the default.
   */
  @Test
  public void timerSetInitialZeroTest() {
    try {
      Board.setTimerInitial(0);
      Board board = new Board();
      assertEquals(300, board.getDefenderTimer().getTimeRemaining());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the initial seconds for the timer can be set. This
    tests to make sure positive values are respected.
   */
  @Test
  public void timerSetInitialPositiveTest() {
    try {
      Board.setTimerInitial(1);
      Board board = new Board();
      assertEquals(1, board.getDefenderTimer().getTimeRemaining());
      Board.setTimerInitial(-1);
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the appended seconds for the timer can be set. This
    tests to make sure negatives are treated as the default.
   */
  @Test
  public void timerSetAppendNegativeTest() {
    try {
      Board.setTimerAppend(-1);
      Board board = new Board();
      assertEquals(3, board.getDefenderTimer().getTimeAppended());
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the appended seconds for the timer can be set. This
    tests to make sure zero is respected.
   */
  @Test
  public void timerSetAppendZeroTest() {
    try {
      Board.setTimerAppend(0);
      Board board = new Board();
      assertEquals(0, board.getDefenderTimer().getTimeAppended());
      Board.setTimerAppend(-1);
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }

  /**
    Verify that the appended seconds for the timer can be set. This
    tests to make sure positive values are respected.
   */
  @Test
  public void timerSetAppendPositiveTest() {
    try {
      Board.setTimerAppend(1);
      Board board = new Board();
      assertEquals(1, board.getDefenderTimer().getTimeAppended());
      Board.setTimerAppend(-1);
    } catch (BadAsciiBoardFormatException ax) {
      fail();
    }
  }
}

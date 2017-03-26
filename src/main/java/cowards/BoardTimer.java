package cowards;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardTimer {
  /**
    The time remaining on the timer.
   */
  private AtomicInteger timeRemaining = new AtomicInteger(0);

  /**
    The amount of seconds to append at the end of a turn.
   */
  private int timeToAppend = 0;

  /**
    Whether or not to countdown the timer.
   */
  private AtomicBoolean countdown = new AtomicBoolean(false);

  /**
    The internal timer.
   */
  private java.util.Timer timer;

  /**
    A timer for one side in the game.

    @param initial The amount of seconds to initialize the timer to.
    @param append The amount of seconds to append at the end of a turn.
   */
  public BoardTimer(int initial, int append) {
    reconfigure(initial, append);

    // Create the actual timer that will perform the decrements.
    timer = new java.util.Timer();
    timer.scheduleAtFixedRate(new java.util.TimerTask() {
      public void run() {
        if (isCountingDown() && getTimeRemaining() > 0) {
          timeRemaining.decrementAndGet();
        }
      }
    }, 1000, 1000);
  }

  /**
    Retrieve the amount of time that is added to the timer after turns.
   */
  public int getTimeAppended() {
    return timeToAppend;
  }

  /**
    Retrieve the amount of time currently on the timer.
   */
  public int getTimeRemaining() {
    return timeRemaining.get();
  }

  /**
    Retrieve the amount of time remaining as text in a minutes and seconds
    format.
   */
  public String getTimeRemainingAsText() {
    // Retrieve seconds remaining on timer.
    int time = getTimeRemaining();

    // Convert to minutes and seconds.
    int minutes = time / 60;
    int seconds = time % 60;

    // Format as a string and return the result.
    return String.format("%02d:%02d", minutes, seconds);
  }

  /**
    Retrieves whether or not the timer is counting down.
   */
  public boolean isCountingDown() {
    return countdown.get();
  }

  /**
    Retrieve whether or not the timer is killed.
   */
  public boolean isKilled() {
    return timer == null;
  }

  /**
    Prevent all future use of this timer.
   */
  public void kill() {
    countdown.set(false);
    if (timer != null) {
      timer.cancel();
      timer = null;
    }
  }

  /**
    Reconfigure the current time remaining and the amount of time to add
    after each turn ends. This method stops the timer before reconfiguring.

    @param remaining The new amount of seconds remaining.
    @param append The new amount of seconds to append after turns.
   */
  public void reconfigure(int remaining, int append) {
    countdown.set(false);
    timeRemaining.set(remaining);
    timeToAppend = append;
  }

  /**
    Start counting down the timer.
   */
  public void start() {
    countdown.set(!isKilled());
  }

  /**
    Stop counting down the timer and append the configured amount of seconds.

    @param skipAppending Whether to skip the appending stage (for game over)
   */
  public int stop(boolean skipAppending) {
    countdown.set(false);
    
    if (skipAppending) {
      return getTimeRemaining();
    }

    return timeRemaining.addAndGet(timeToAppend);
  }
}

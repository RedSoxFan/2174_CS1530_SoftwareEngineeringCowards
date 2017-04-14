package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;
import javax.swing.filechooser.FileView;

public class HnefataflPanel extends JPanel {

  private static Board board;
  private Rectangle grid;
  private Rectangle newGame;
  private Rectangle saveGame;
  private Rectangle loadGame;
  private Rectangle exitGame;

  // Keeps the AI and player from stepping on one another's toes.
  private static Semaphore aiSem = new Semaphore(1);

  // For determining the game mode.
  private enum Mode { TWO_HUMAN, HUMAN_AI, AI_HUMAN }

  // The mode the user decided on.
  private Mode aiMode;

  /**
    Constructor.
  */
  public HnefataflPanel() {
    super();

    // Create initial game board.
    try {
      board = new Board();
    } catch (BadAsciiBoardFormatException bx) {
      JOptionPane.showMessageDialog(null, "Critical: Cannot load initial board.");
      System.exit(1);
    }
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent event) {
        mouseReleaseEvent(event);
      }
    });

    // Initialize timer to repaint.
    new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
      public void run() {
        repaint();
      }
    }, 50, 1000 / 30);

    String [] options = {"Human/Human", "Human/AI", "AI/Human"};

    Object selected = JOptionPane.showInputDialog(
        null, "Select game mode:", "Selection",
        JOptionPane.DEFAULT_OPTION, null, options, "Human/Human"
    );

    // Get the user's mode choice.
    if (selected == null || "Human/Human".equals(selected.toString())) {
      // Default
      aiMode = Mode.TWO_HUMAN;
    } else if ("Human/AI".equals(selected.toString())) {
      aiMode = Mode.HUMAN_AI;
    } else if ("AI/Human".equals(selected.toString())) {
      aiMode = Mode.AI_HUMAN;
    }

    // Only move first if the AI has the first move.
    if (aiMode == Mode.AI_HUMAN) {
      Thread thread = new Thread(HnefataflPanel::doAiMove);
      thread.start();
    }
  }

  /**
    Does an AI move. This method should only be launched as a separate thread.
    This method is not represented in the tests because in this format it is
    untestable.
   */
  public static void doAiMove() {
    try {
      // To keep the frames updating smoothly.
      if (!aiSem.tryAcquire()) {
        return;
      }

      // We can only afford to look ahead 2 moves due to the timers.
      // Note the thinking time will differ depending on CPU speed.
      int [] choice = Hnefalump.getNextMove(board, 2);
      if (choice == null) {
        aiSem.release();
        return;
      }
      board.select(choice[2], choice[3]);
      board.move(choice[0], choice[1]);
      aiSem.release();
    } catch (GridOutOfBoundsException gx) {
      JOptionPane.showMessageDialog(null, "Critical: AI out of bounds.");
    }
  }

  /**
    Handles click events

    @param event Mouse event object.
   */
  public void mouseReleaseEvent(MouseEvent event) {
    if (event.getButton() == 1) {
      if (grid != null && grid.contains(event.getPoint())) {
        int col = (event.getX() - grid.x) / (grid.width / 11);
        int row = (event.getY() - grid.y) / (grid.height / 11);

        // Freeze the board if the game is over.
        if (board.isGameOver()) {
          return;
        }

        try {
          aiSem.acquire();
          if (board.hasSelection()) {
            // Attempt to move. If it fails, try changing the selection.
            if (!board.move(row, col)) {
              board.select(row, col);
            } else if (aiMode != Mode.TWO_HUMAN) {
              // AI response if needed
              Thread thread = new Thread(HnefataflPanel::doAiMove);
              thread.start();
            }
          } else {
            board.select(row, col);
          }
          aiSem.release();
        } catch (GridOutOfBoundsException ex) {
          // Something went wrong with the geometry of the board painted.
          // Since this should not happen, just log a warning.
          System.out.println("WARNING: The board geometry is not synced");
        } catch (InterruptedException ix) {
          // Not much we can do here.
        }
      } else if (newGame != null && newGame.contains(event.getPoint())) {
        board.pauseTimers();
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to start new game?", 
            "New Game", JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
          try {
            board.setGameOver(true);
            board = new Board();

            // Make sure the AI goes first in new game situations when the mode is right.
            if (aiMode == Mode.AI_HUMAN) {
              Thread thread = new Thread(HnefataflPanel::doAiMove);
              thread.start();
            }
          } catch (BadAsciiBoardFormatException bx) {
            JOptionPane.showMessageDialog(null, "Critical: Cannot load initial board.");
            System.exit(1);
          }
        }
        board.resumeTimers();
      } else if (saveGame != null && saveGame.contains(event.getPoint())) {
        if (board.isGameOver()) {
          JOptionPane.showMessageDialog(null, "You cannot save a board in a game over state.");
        } else {
          board.pauseTimers();
          File directorylock = new File("saved_games");
          JFileChooser fc = new JFileChooser(directorylock);
          
          // Create the saved_games directory if it doesn't exist.
          if (!directorylock.exists()) {
            boolean success = directorylock.mkdir();
            if (!success) {
              return;
            }
          }
          fc.setFileView(new FileView() {
            public Boolean isTraversable(File fileName) {
              return directorylock.equals(fileName);
            }
          });
          
          String fileName = "";
          try {
            if (fc.showSaveDialog(HnefataflPanel.this) == JFileChooser.APPROVE_OPTION) {
              fileName = fc.getSelectedFile().getName();
            } else {
              board.resumeTimers();
              return;
            }
          } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(null, "You cannot move out of saved_games.");
            board.resumeTimers();
            return;
          }
          
          if (fileName != null) {
            if (BoardWriter.saveBoard(fileName, board)) {
              JOptionPane.showMessageDialog(null, "Successfully saved game file.");
            } else {
              JOptionPane.showMessageDialog(null, "Error saving game file.");
            }
          }
          board.resumeTimers();
        }
      } else if (loadGame != null && loadGame.contains(event.getPoint())) {
        board.pauseTimers();
        File directorylock = new File("saved_games");
        JFileChooser fc = new JFileChooser(directorylock);
          
        fc.setFileView(new FileView() {
          public Boolean isTraversable(File fileName) {
            return directorylock.equals(fileName);
          }
        });
          
        String fileName = "";
        try {
          if (fc.showOpenDialog(HnefataflPanel.this) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getName();
          } else {
            board.resumeTimers();
            return;
          }
        } catch (NullPointerException npe) {
          JOptionPane.showMessageDialog(null, "You cannot move out of saved_games.");
          board.resumeTimers();
          return;
        }
          
        if (fileName != null) {
          board.setGameOver(true);
          String[] splitFile = fileName.split("\\.");
          if (null != (board = BoardLoader.loadBoardFromSave(splitFile[0]))) {
            JOptionPane.showMessageDialog(null, "Successfully loaded game file.");
            repaint();
          } else {
            JOptionPane.showMessageDialog(null, "Error loading game file.");
          }
          board.setGameOver(false);
        }
        board.resumeTimers();
      } else if (exitGame != null && exitGame.contains(event.getPoint())) {
        board.pauseTimers();
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to exit the game?", 
            "Exit Game", JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
          System.exit(0);
        }
        board.resumeTimers();
      }
    }
  }

  /**
    Paints the main window.

    @param graph Graphics object used to draw the screen.
   */
  public void paintComponent(Graphics graph) {
    // Wipe the buffer with black.
    graph.setColor(Color.BLACK);
    graph.fillRect(0, 0, getWidth(), getHeight());

    // Set the minimum grid margins.
    int marginLeft = 10;
    int marginRight = 10;
    int marginTop = 100;
    int marginBottom = 100;

    // Get the maximum size of the grid.
    int maxWidth = getWidth() - marginLeft - marginRight;
    int maxHeight = getHeight() - marginTop - marginBottom;

    // Determine the bounds of the grid.
    int squares = BoardLayout.GRID_COL_MAX + 1;
    int gridSize = Math.min(maxWidth, maxHeight) / squares;
    int gridX = marginLeft + (maxWidth / 2 - (gridSize * squares) / 2);
    int gridY = marginTop + (maxHeight / 2 - (gridSize * squares) / 2);
    grid = new Rectangle(gridX, gridY, gridSize * squares, gridSize * squares);

    // Paint the board.
    paintBoard(graph, grid);

    // Paint the title.
    Rectangle title = new Rectangle(grid.x, 0, grid.width, grid.y);
    paintTitle(graph, title);
    
    // Determine the available height for the bottom sections.
    int bottom = (int) grid.getMaxY();
    int remaining = getHeight() - bottom;

    // Bottom -> Status.
    Rectangle status = new Rectangle(grid.x + grid.width / 4, bottom,
        grid.width / 2, remaining / 2);
    paintStatus(graph, status);

    // Bottom -> Timers.
    Rectangle attacker = new Rectangle(grid.x + grid.width / 16, bottom,
        grid.width / 8, remaining / 2);
    Rectangle defender = new Rectangle(grid.x + grid.width / 16 * 13, bottom,
        grid.width / 8, remaining / 2);
    paintTimers(graph, attacker, defender);

    // Bottom -> Buttons.
    int statusBot = (int) status.getMaxY();
    Rectangle buttons = new Rectangle(grid.x, statusBot, grid.width, remaining / 2);
    paintButtons(graph, buttons);
  }

  /**
    Paint the title centers in the bounds. The font size will be set to the
    maximum possible size that will fit in the bounds.
   
    @param graph The graphics handle.
    @param bounds The bounds to paint the title in.
   */
  private void paintTitle(Graphics graph, Rectangle bounds) {
    // Paint Title
    graph.setColor(Color.WHITE);
    drawText(graph, "Hnefatafl", bounds, true);
  }

  /**
    Paint the game board.
    
    @param graph The graphics handle.
    @param bounds The bounds to paint in.
   */
  private void paintBoard(Graphics graph, Rectangle bounds) {
    // Paint board background.
    graph.setColor(new Color(0, 196, 0));
    graph.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

    // Paint corners and throne.
    graph.setColor(new Color(0, 128, 0));
    for (int[] position : BoardLayout.SPECIAL_SQUARE_POSITIONS) {
      drawSquare(graph, bounds, position[0], position[1], 0, true);
    }


    // If a piece is selected (and the game is not paused), highlight it.
    if (!board.isPaused() && board.hasSelection()) {
      graph.setColor(Color.YELLOW);
      drawSquare(graph, bounds, board.getSelectedRow(), board.getSelectedColumn(), 0, true);
    }
 
    // Paint the game board.
    for (int r = 0; r <= BoardLayout.GRID_ROW_MAX; r++) {
      for (int c = 0; c <= BoardLayout.GRID_COL_MAX; c++) {
        // Draw grid borders.
        graph.setColor(new Color(0, 64, 0));
        drawSquare(graph, bounds, r, c, 0, false);

        // If the game is paused, just paint grid.
        if (board.isPaused()) {
          continue;
        }

        // Retrieve the state of the square. If it is empty, move on.
        BoardLayout.GridSquareState state = board.safeSquare(r, c);
        if (state.isEmpty()) {
          continue;
        }

        // Set the color of the piece.
        if (state.isAttacking()) {
          graph.setColor(Color.BLACK);
        } else if (state.isDefender()) {
          graph.setColor(Color.GRAY);
        } else if (state.isKing()) {
          graph.setColor(Color.WHITE);
        }

        // Draw the piece.
        drawSquare(graph, bounds, r, c, 5, true);
      }
    }
  }

  /**
    Paint the status. This is either whose turn it is, who won, or
    that there was a draw.
    
    @param graph The graphics handle.
    @param bounds The bounds to paint in.
   */
  private void paintStatus(Graphics graph, Rectangle bounds) {
    // Determine the status.
    String turn;
    if (board.isDraw()) {
      turn = "Draw";
    } else if (board.isPaused()) {
      turn = "Paused";
    } else {
      turn = board.isAttackerTurn() ? "Attacker's " : "Defender's ";
      turn += board.isGameOver() ? "Won" : "Turn";
    }
    
    // Draw the text.
    graph.setColor(Color.WHITE);
    drawText(graph, turn, bounds, true);
  }

  /**
    Paint the timers.

    @param graph The graphics handle.
    @param attacker The bounds of the timer for the attacker's side.
    @param defender The bounds of the timer for the defender's side.
   */
  private void paintTimers(Graphics graph, Rectangle attacker, Rectangle defender) {
    // Paint attacker's timer.
    String attackTime = board.getAttackerTimer().getTimeRemainingAsText();
    graph.setColor(board.isAttackerTurn() ? Color.WHITE : Color.GRAY);
    drawText(graph, attackTime, attacker, true);

    // Paint defender's timer.
    String defendTime = board.getDefenderTimer().getTimeRemainingAsText();
    graph.setColor(board.isAttackerTurn() ? Color.GRAY : Color.WHITE);
    drawText(graph, defendTime, defender, true);
  }

  /**
    Paint the buttons.
    
    @param graph The graphics handle.
    @param bounds The bounds to paint in.
   */
  private void paintButtons(Graphics graph, Rectangle bounds) {
    // Paint buttons.
    int buttonX = bounds.x;
    int buttonWidth = bounds.width / 4;
    setMaxFontSize(graph, "MMMMMMMMM", buttonWidth, bounds.height);

    // New Game.
    newGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "New Game", newGame, false);
    buttonX += buttonWidth;

    // Save.
    saveGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "Save Game", saveGame, false);
    buttonX += buttonWidth;
    
    // Load.
    loadGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "Load Game", loadGame, false);
    buttonX += buttonWidth;
 
    // Exit.
    exitGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "Exit Game", exitGame, false);
    buttonX += buttonWidth;
  }

  /**
    Paints a square on the grid. The margin attribute can be used to paint
    a piece.
    
    @param graph The graphics handle.
    @param bounds The bounds of the grid.
    @param row The row of the square to paint on.
    @param col The column of the square to paint on.
    @param margin The margin to use on all sides of the painting.
    @param fill Whether to fill or draw an outline.
   */
  private void drawSquare(Graphics graph, Rectangle bounds, int row, int col,
      int margin, boolean fill) {

    // Determine the size of a grid square.
    int squareSize = bounds.width / (BoardLayout.GRID_COL_MAX + 1);

    // Determine the bounds of the square to paint.
    int xloc = bounds.x + squareSize * col + margin;
    int yloc = bounds.y + squareSize * row + margin;
    int size = squareSize - margin - margin;

    // Paint.
    if (fill) {
      graph.fillRect(xloc, yloc, size, size);
    } else {
      graph.drawRect(xloc, yloc, size, size);
    }
  }

  /**
    Paints a string of text centered in an area.

    @param graph The graphics handle to use.
    @param text The text to paint.
    @param bounds The bounding box for the text.
    @param maxfont Whether or not to set the maximum font size possible.
   **/
  private void drawText(Graphics graph, String text, Rectangle bounds, boolean maxfont) {
    // If desired, set the maximum font size that fits the bounds.
    if (maxfont) {
      setMaxFontSize(graph, text, bounds.width, bounds.height);
    }

    // Get the size of the actual text.
    FontMetrics fm = graph.getFontMetrics();
    int width = fm.stringWidth(text);
    int height = fm.getHeight();

    // Get the top left x and bottom left y coordinates.
    int xloc = bounds.x + bounds.width / 2 - width / 2;
    int yloc = bounds.y + bounds.height / 2 - height / 2 + fm.getAscent();

    // Paint the text.
    graph.drawString(text, xloc, yloc);
  }

  /**
   Sets the max font size.

   @param graph Graphics object to paint on.
   @param text  Text for which to get the font size.
   @param maxw  Max width.
   @param maxh  Max height.
  */
  private void setMaxFontSize(Graphics graph, String text, int maxw, int maxh) {
    // Reset the font to be small.
    graph.setFont(graph.getFont().deriveFont(1f));

    // Increase the font size until it is too wide and/or too tall.
    FontMetrics fm = graph.getFontMetrics();
    while (fm.stringWidth(text) < maxw && fm.getHeight() < maxh) {
      graph.setFont(graph.getFont().deriveFont(graph.getFont().getSize() + 1f));
      fm = graph.getFontMetrics();
    }

    // Decrease the font size so that it fits inside the size requirements again.
    graph.setFont(graph.getFont().deriveFont(graph.getFont().getSize() - 1f));
  }
}

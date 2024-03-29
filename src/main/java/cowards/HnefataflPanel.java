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

  // The mode selector.
  private Menu menu;

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

    board.pauseTimers();
    showAiSelection();
  }

  /**
    Show the AI Selection menu.
   */
  private void showAiSelection() {
    board.pauseTimers();
    menu = new Menu("AI Mode");
    menu.addItem("Human/Human", () -> {
      aiMode = Mode.TWO_HUMAN;
      board.resumeTimers();
      return true;
    });
    menu.addItem("AI/Human", () -> {
      aiMode = Mode.AI_HUMAN;
      board.resumeTimers();
      Thread thread = new Thread(HnefataflPanel::doAiMove);
      thread.start();
      return true;
    });
    menu.addItem("Human/AI", () -> {
      aiMode = Mode.HUMAN_AI;
      board.resumeTimers();
      return true;
    });
  }

  /**
    Show a confirmation dialog menu.

    @param title The title text.
    @param yes The yes text option.
    @param no The no text option.
    @param yesCb The yes callback.
    @param noCb The no callback.
   */
  private void showConfirmDialog(String title, String yes, Callable<Boolean> yesCb,
      String no, Callable<Boolean> noCb) {
    board.pauseTimers();
    menu = new Menu(title);
    menu.addItem(yes, yesCb);
    menu.addItem(no, noCb);
  }

  /**
    Show a message dialog menu.

    @param msg The message text.
    @param pause Whether or not to pause the game.
   */
  private void showMessageDialog(String text, boolean pause) {
    if (pause) {
      board.pauseTimers();
    }
    menu = new Menu(text);
    menu.addItem("Ok", () -> {
      if (pause) {
        board.resumeTimers();
      }
      return true;
    });
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
      if (menu != null)  {
        if (menu.handleMouse(event.getX(), event.getY())) {
          menu = null;
        }
      } else if (grid != null && grid.contains(event.getPoint())) {
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
        if (!aiSem.tryAcquire()) {
          showMessageDialog("You cannot start a new game during the AI turn", false);
          return;
        }
        showConfirmDialog("Do you really want to start a new game?",
            "Yes", () -> {
              try {
                board.setGameOver(true);
                aiMode = Mode.TWO_HUMAN;
                board = new Board();
                showAiSelection();
              } catch (BadAsciiBoardFormatException bx) {
                JOptionPane.showMessageDialog(null, "Critical: Cannot load initial board.");
                System.exit(1);
              }
              aiSem.release();
              return false;
            },
            "No",  () -> {
              board.resumeTimers();
              aiSem.release();
              return true;
            }
        );
      } else if (saveGame != null && saveGame.contains(event.getPoint())) {
        if (board.isGameOver()) {
          showMessageDialog("You cannot save a game that is over", false);
        } else {
          // Don't allow a save if the AI is doing work, this could cause an invalid save state.
          // Note this allows a timer countdown for both AI and the player.
          if (!aiSem.tryAcquire()) {
            showMessageDialog("You cannot start a new game during the AI turn", false);
            return;
          }
          board.pauseTimers();
          File directorylock = new File("saved_games");
          JFileChooser fc = new JFileChooser(directorylock);
          
          // Create the saved_games directory if it doesn't exist.
          if (!directorylock.exists()) {
            boolean success = directorylock.mkdir();
            if (!success) {
              aiSem.release();
              return;
            }
          }
          fc.setFileView(new FileView() {
            public Boolean isTraversable(File fileName) {
              return directorylock.equals(fileName);
            }
          });
          
          // Prompt the user and check if they tried to save elsewhere.
          String fileName = "";
          try {
            if (fc.showSaveDialog(HnefataflPanel.this) == JFileChooser.APPROVE_OPTION) {
              fileName = fc.getSelectedFile().getName();
            } else {
              aiSem.release();
              board.resumeTimers();
              return;
            }
          } catch (NullPointerException npe) {
            showMessageDialog("You cannot move out of saved_games", true);
            aiSem.release();
            board.resumeTimers();
            return;
          }
          
          // Try to save.
          if (fileName != null) {
            if (BoardWriter.saveBoard(fileName, board)) {
              showMessageDialog("Successfully saved game file.", true);
            } else {
              showMessageDialog("Error saving game file", true);
            }
          }
          aiSem.release();
        }
      } else if (loadGame != null && loadGame.contains(event.getPoint())) {

        // Don't allow a load if the AI is doing work, this could cause an invalid save state.
        // Note this allows a timer countdown for both AI and the player.
        if (!aiSem.tryAcquire()) {
          showMessageDialog("You cannot start a new game during the AI turn", false);
          return;
        }

        board.pauseTimers();
        File directorylock = new File("saved_games");
        JFileChooser fc = new JFileChooser(directorylock);
          
        fc.setFileView(new FileView() {
          public Boolean isTraversable(File fileName) {
            return directorylock.equals(fileName);
          }
        });
          
        // Prompt the user and check if they moved outside of the save directory.
        String fileName = "";
        try {
          if (fc.showOpenDialog(HnefataflPanel.this) == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getSelectedFile().getName();
          } else {
            aiSem.release();
            board.resumeTimers();
            return;
          }
        } catch (NullPointerException npe) {
          showMessageDialog("You cannot move out of saved_games", true);
          aiSem.release();
          return;
        }
          
        // Try to load a game.
        try {
          if (fileName != null) {
            board.setGameOver(true);
            String[] splitFile = fileName.split("\\.");
            Board temp;
            if (null != (temp = BoardLoader.loadBoardFromSave(splitFile[0]))) {
              board = temp;
              showMessageDialog("Successfully loaded game file.", true);
              repaint();
            } else {
              showMessageDialog("Error loading game file.", true);
            }
          }
          board.setGameOver(false);
        } catch (BoardLoadException ex) {
          showMessageDialog("Game file is corrupted, cannot load.", true);
          aiSem.release();
          board.setGameOver(false);
          return;
        }
        aiSem.release();
      } else if (exitGame != null && exitGame.contains(event.getPoint())) {
        // If the AI is taking it's turn, don't pause the game for the exit
        // dialog.
        if (aiSem.tryAcquire()) {
          board.pauseTimers();
        }
        showConfirmDialog("Do you really want to exit the game?",
            "Yes", () -> {
              System.exit(0);
              return true;
            },
            "No", () -> {
              aiSem.release();
              board.resumeTimers();
              return true;
            }
        );
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

    // Paint menu.
    if (menu != null) {
      Dimension menuDim = menu.getSize(graph);
      Rectangle menuBounds = new Rectangle(
          grid.x + grid.width / 2 - (int) (menuDim.getWidth() / 2),
          grid.y + grid.height / 2 - (int) (menuDim.getHeight() / 2),
          (int) menuDim.getWidth(),
          (int) menuDim.getHeight());
      menu.paint(graph, menuBounds, getMousePosition());
    }
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

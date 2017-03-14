package cowards;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class HnefataflPanel extends JPanel {

  private Board board;
  private Rectangle grid;
  private Rectangle newGame;
  private Rectangle saveGame;
  private Rectangle loadGame;
  private Rectangle exitGame;

  /**
    Constructor.
  */
  public HnefataflPanel() {
    super();

    // Create initial game board.
    board = new Board();
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent event) {
        mouseReleaseEvent(event);
      }
    });

    // Initialize timer to repaint
    new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
      public void run() {
        repaint();
      }
    }, 50, 1000 / 30);
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
          if (board.hasSelection()) {
            // Attempt to move. If it fails, try changing the selection
            if (!board.move(row, col)) {
              board.select(row, col);
            }
          } else {
            board.select(row, col);
          }
        } catch (GridOutOfBoundsException ex) {
          // Something went wrong with the geometry of the board painted.
          // Since this should not happen, just log a warning
          System.out.println("WARNING: The board geometry is not synced");
        }
      } else if (newGame != null && newGame.contains(event.getPoint())) {
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to start new game?", 
            "New Game", JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
          board.reset();
        }
      } else if (saveGame != null && saveGame.contains(event.getPoint())) {
        if (board.isGameOver()) {
          JOptionPane.showMessageDialog(null, "You cannot save a board in a game over state.");
        } else {
          String fileName = JOptionPane.showInputDialog(null, 
              "Enter the name of your save game file");
          if (fileName.equals("")) {
            JOptionPane.showMessageDialog(null, "You cannot enter a blank file name.");
          } else if (fileName != null) {
            if (board.saveBoard(fileName)) {
              JOptionPane.showMessageDialog(null, "Successfully saved game file.");
            } else {
              JOptionPane.showMessageDialog(null, "Error saving game file.");
            }
          }
        }
      } else if (loadGame != null && loadGame.contains(event.getPoint())) {
        String fileName = JOptionPane.showInputDialog(null, 
            "Enter the name of the game you want to load");
        if (fileName.equals("")) {
          JOptionPane.showMessageDialog(null, "You cannot enter a blank file name.");
        } else if (fileName != null) {
          if (board.loadBoardFromSave(fileName)) {
            JOptionPane.showMessageDialog(null, "Successfully loaded game file.");
            repaint();
          } else {
            JOptionPane.showMessageDialog(null, "Error loading game file.");
          }
        }
      } else if (exitGame != null && exitGame.contains(event.getPoint())) {
        int selected = JOptionPane.showConfirmDialog(null, "Do you really want to exit the game?", 
            "Exit Game", JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
          System.exit(0);
        }
      }
    }
  }

  /**
    Paints the main window.

    @param graph Graphics object used to draw the screen.
   */
  public void paintComponent(Graphics graph) {
    // Wipe the buffer with black
    graph.setColor(Color.BLACK);
    graph.fillRect(0, 0, getWidth(), getHeight());

    // Set the minimum grid margins
    int marginLeft = 10;
    int marginRight = 10;
    int marginTop = 100;
    int marginBottom = 100;

    // Get the maximum size of the grid
    int maxWidth = getWidth() - marginLeft - marginRight;
    int maxHeight = getHeight() - marginTop - marginBottom;

    // Determine the bounds of the grid
    int squares = BoardLayout.GRID_COL_MAX + 1;
    int gridSize = Math.min(maxWidth, maxHeight) / squares;
    int gridX = marginLeft + (maxWidth / 2 - (gridSize * squares) / 2);
    int gridY = marginTop + (maxHeight / 2 - (gridSize * squares) / 2);
    grid = new Rectangle(gridX, gridY, gridSize * squares, gridSize * squares);

    // Paint the board
    paintBoard(graph, grid);

    // Paint the title
    Rectangle title = new Rectangle(grid.x, 0, grid.width, grid.y);
    paintTitle(graph, title);
    
    // Determine the available height for the bottom sections
    int bottom = (int) grid.getMaxY();
    int remaining = getHeight() - bottom;

    // Bottom -> Status
    Rectangle status = new Rectangle(grid.x, bottom, grid.width, remaining / 2);
    paintStatus(graph, status);

    // Bottom -> Buttons
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

    // If a piece is selected, highlight it.
    if (board.hasSelection()) {
      graph.setColor(Color.YELLOW);
      drawSquare(graph, bounds, board.getSelectedRow(), board.getSelectedColumn(), 0, true);
    }
 
    // Paint the game board.
    for (int r = 0; r <= BoardLayout.GRID_ROW_MAX; r++) {
      for (int c = 0; c <= BoardLayout.GRID_COL_MAX; c++) {
        // Draw grid borders.
        graph.setColor(new Color(0, 64, 0));
        drawSquare(graph, bounds, r, c, 0, false);

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
    // Determine the status
    String turn;
    if (board.isDraw()) {
      turn = "Draw";
    } else {
      turn = board.isAttackerTurn() ? "Attacker's " : "Defender's ";
      turn += board.isGameOver() ? "Won" : "Turn";
    }
    
    // Draw the text
    graph.setColor(Color.WHITE);
    drawText(graph, turn, bounds, true);
  }

  /**
    Paint the buttons.
    
    @param graph The graphics handle.
    @param bounds The bounds to paint in.
   */
  private void paintButtons(Graphics graph, Rectangle bounds) {
    // Paint buttons
    int buttonX = bounds.x;
    int buttonWidth = bounds.width / 4;
    setMaxFontSize(graph, "MMMMMMMMM", buttonWidth, bounds.height);

    // New Game
    newGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "New Game", newGame, false);
    buttonX += buttonWidth;

    // Save
    saveGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "Save Game", saveGame, false);
    buttonX += buttonWidth;
    
    // Load
    loadGame = new Rectangle(buttonX, bounds.y, buttonWidth, bounds.height);
    drawText(graph, "Load Game", loadGame, false);
    buttonX += buttonWidth;
 
    // Exit
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

    // Determine the size of a grid square
    int squareSize = bounds.width / (BoardLayout.GRID_COL_MAX + 1);

    // Determine the bounds of the square to paint
    int xloc = bounds.x + squareSize * col + margin;
    int yloc = bounds.y + squareSize * row + margin;
    int size = squareSize - margin - margin;

    // Paint
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
    // If desired, set the maximum font size that fits the bounds
    if (maxfont) {
      setMaxFontSize(graph, text, bounds.width, bounds.height);
    }

    // Get the size of the actual text.
    FontMetrics fm = graph.getFontMetrics();
    int width = fm.stringWidth(text);
    int height = fm.getHeight();

    // Get the top left x and bottom left y coordinates
    int xloc = bounds.x + bounds.width / 2 - width / 2;
    int yloc = bounds.y + bounds.height / 2 - height / 2 + fm.getAscent();

    // Paint the text
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

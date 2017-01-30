package hnefatafl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HnefataflPanel extends JPanel {

  public static enum GridSquareState {
    EMPTY, KING, DEFENDER, ATTACKER
  }

  public static final char[][] INITIAL_BOARD = new char[][]{
    {' ', ' ', ' ', 'A', 'A', 'A', 'A', 'A', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {'A', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ', 'A'},
      {'A', ' ', ' ', ' ', 'D', 'D', 'D', ' ', ' ', ' ', 'A'},
      {'A', 'A', ' ', 'D', 'D', 'K', 'D', 'D', ' ', 'A', 'A'},
      {'A', ' ', ' ', ' ', 'D', 'D', 'D', ' ', ' ', ' ', 'A'},
      {'A', ' ', ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ', 'A'},
      {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', ' ', ' ', 'A', ' ', ' ', ' ', ' ', ' '},
      {' ', ' ', ' ', 'A', 'A', 'A', 'A', 'A', ' ', ' ', ' '}
  };

  private GridSquareState[][] board = new GridSquareState[11][11];
  private boolean attacking = true;
  private boolean gameOver = false;
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
    addMouseListener(new MouseAdapter() {
        public void mouseReleased(MouseEvent event) {
        mouseReleaseEvent(event);
        }
    });

    reset();

    // Initialize timer to repaint
    new java.util.Timer().scheduleAtFixedRate(new java.util.TimerTask() {
        public void run() {
        repaint();
        }
        }, 50, 1000 / 30);
  }

  /**
    Reinitialize the board.
   */
  public void reset() {
    // Initialize the board
    for (int r = 0; r < 11; r++) {
      for (int c = 0; c < 11; c++) {
        char square = INITIAL_BOARD[r][c];
        if (square == 'A') {
          board[r][c] = GridSquareState.ATTACKER;
        } else if (square == 'D') {
          board[r][c] = GridSquareState.DEFENDER;
        } else if (square == 'K') {
          board[r][c] = GridSquareState.KING;
        } else {
          board[r][c] = GridSquareState.EMPTY;
        }
      }
    }
    attacking = true;
    gameOver = false;
  }

  /**
    Handles click events

    @param event Mouse event object.
   */
  public void mouseReleaseEvent(MouseEvent event) {
    if (event.getButton() == 1) {
      if (grid != null && grid.contains(event.getPoint())) {
        int xpos = (event.getX() - grid.x) / (grid.width / 11);
        int ypos = (event.getY() - grid.y) / (grid.height / 11);
        // TODO: Process event
      } else if (newGame != null && newGame.contains(event.getPoint())) {
        // TODO: Confirm and reset
        reset();
      } else if (saveGame != null && saveGame.contains(event.getPoint())) {
        // TODO: Show save dialog and save
      } else if (loadGame != null && loadGame.contains(event.getPoint())) {
        // TODO: Show load dialog and load
      } else if (exitGame != null && exitGame.contains(event.getPoint())) {
        // TODO: Confirm
        System.exit(0);
      }
    }
  }

  /**
    Paints the main window.

    @param graph Graphics object used to draw the screen.
   */
  public void paintComponent(Graphics graph) {
    graph.setColor(Color.BLACK);
    graph.fillRect(0, 0, getWidth(), getHeight());

    int gridS = Math.min((getWidth() - 20), (getHeight() - 200)) / 11;
    int gridX = getWidth() / 2 - (gridS * 11) / 2;
    int gridY = 115;
    grid = new Rectangle(gridX, gridY, gridS * 11, gridS * 11);

    // Paint Title
    String title = "Hnefatafl";
    graph.setColor(Color.WHITE);
    setMaxFontSize(graph, title, (gridS * 11) - 10, 100);
    int width = graph.getFontMetrics().stringWidth(title);
    graph.drawString(title, getWidth() / 2 - width / 2, 100);

    // Paint board background
    graph.setColor(new Color(0, 196, 0));
    graph.fillRect(gridX, gridY, gridS * 11, gridS * 11);

    // Paint corners and throne
    graph.setColor(new Color(0, 128, 0));
    graph.fillRect(gridX, gridY, gridS, gridS);
    graph.fillRect(gridX, gridY + gridS * 10, gridS, gridS);
    graph.fillRect(gridX + gridS * 5, gridY + gridS * 5, gridS, gridS);
    graph.fillRect(gridX + gridS * 10, gridY, gridS, gridS);
    graph.fillRect(gridX + gridS * 10, gridY + gridS * 10, gridS, gridS);

    int tempY = gridY;
    for (int r = 0; r < 11; r++) {
      int tempX = gridX;
      for (int c = 0; c < 11; c++) {
        // Draw Piece
        if (board[r][c].equals(GridSquareState.ATTACKER)) {
          graph.setColor(Color.BLACK);
          graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
        } else if (board[r][c].equals(GridSquareState.DEFENDER)) {
          graph.setColor(Color.GRAY);
          graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
        } else if (board[r][c].equals(GridSquareState.KING)) {
          graph.setColor(Color.WHITE);
          graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
        }

        // Draw Border
        graph.setColor(new Color(0, 64, 0));
        graph.drawRect(tempX, tempY, gridS, gridS);

        // Increment x
        tempX += gridS;
      }
      tempY += gridS;
    }

    // Paint whose turn it is (or who won)
    graph.setColor(Color.WHITE);
    String turn = attacking ? "Attacker's " : "Defender's ";
    turn += gameOver ? "Won" : "Turn";
    setMaxFontSize(graph, turn, (gridS * 11) - 10, 25);
    width = graph.getFontMetrics().stringWidth(turn);
    int height = graph.getFontMetrics().getHeight();
    graph.drawString(turn, getWidth() / 2 - width / 2, tempY + height);

    // Paint buttons
    setMaxFontSize(graph, "MMMMMMMMM", gridS * 11 / 5, 50);
    // New Game
    String newGameText = "New Game";
    width = graph.getFontMetrics().stringWidth(newGameText);
    height = graph.getFontMetrics().getHeight();
    newGame = new Rectangle(gridX + gridS * 11 / 5 - width / 2,
        getHeight() - 25 - height,
        width, height);
    graph.drawString(newGameText, newGame.x, newGame.y + height);

    // Save
    String saveText = "Save Game";
    width = graph.getFontMetrics().stringWidth(saveText);
    height = graph.getFontMetrics().getHeight();
    saveGame = new Rectangle(gridX + gridS * 22 / 5 - width / 2,
        getHeight() - 25 - height,
        width, height);
    graph.drawString(saveText, saveGame.x, saveGame.y + height);

    // Load
    String loadText = "Load Game";
    width = graph.getFontMetrics().stringWidth(loadText);
    height = graph.getFontMetrics().getHeight();
    loadGame = new Rectangle(gridX + gridS * 33 / 5 - width / 2,
        getHeight() - 25 - height,
        width, height);
    graph.drawString(loadText, loadGame.x, loadGame.y + height);

    // Exit
    String exitText = "Exit Game";
    width = graph.getFontMetrics().stringWidth(exitText);
    height = graph.getFontMetrics().getHeight();
    exitGame = new Rectangle(gridX + gridS * 44 / 5 - width / 2,
        getHeight() - 25 - height,
        width, height);
    graph.drawString(exitText, exitGame.x, exitGame.y + height);
  }

  private void setMaxFontSize(Graphics graph, String text, int maxw, int maxh) {
    graph.setFont(graph.getFont().deriveFont(1f));
    FontMetrics fm = graph.getFontMetrics();
    while (fm.stringWidth(text) < maxw && fm.getHeight() < maxh) {
      graph.setFont(graph.getFont().deriveFont(graph.getFont().getSize() + 1f));
      fm = graph.getFontMetrics();
    }
    graph.setFont(graph.getFont().deriveFont(graph.getFont().getSize() - 1f));
  }
}

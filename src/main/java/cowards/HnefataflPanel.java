package cowards;

import java.awt.*;
import java.awt.event.*;
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
        int xpos = (event.getX() - grid.x) / (grid.width / 11);
        int ypos = (event.getY() - grid.y) / (grid.height / 11);
        // TODO: Process event
      } else if (newGame != null && newGame.contains(event.getPoint())) {
        // TODO: Confirm and reset
        board.reset();
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
        try {
          if (board.square(r, c).equals(Board.GridSquareState.ATTACKER)) {
            graph.setColor(Color.BLACK);
            graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
          } else if (board.square(r, c).equals(Board.GridSquareState.DEFENDER)) {
            graph.setColor(Color.GRAY);
            graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
          } else if (board.square(r, c).equals(Board.GridSquareState.KING)) {
            graph.setColor(Color.WHITE);
            graph.fillRect(tempX + 5, tempY + 5, gridS - 10, gridS - 10);
          }
        } catch (GridOutOfBoundsException exception) {
          // TODO: Better error message.
          System.out.println(
              "Attempted to go outside of the grid. Please contact developers."
          );
          System.exit(1);
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
    String turn = board.isAttackerTurn() ? "Attacker's " : "Defender's ";
    turn += board.isGameOver() ? "Won" : "Turn";
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
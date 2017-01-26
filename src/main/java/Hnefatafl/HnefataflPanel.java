package Hnefatafl;

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
    private Rectangle grid, newGame, saveGame, loadGame, exitGame;

    public HnefataflPanel() {
        super();
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                mouseReleaseEvent(e);
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

    public void reset() {
        // Initialize the board
        for (int r=0; r < 11; r++) {
            for (int c=0; c < 11; c++) {
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

    public void mouseReleaseEvent(MouseEvent e) {
        if (e.getButton() == 1) {
            if (grid != null && grid.contains(e.getPoint())) {
                int c = (e.getX() - grid.x) / (grid.width / 11);
                int r = (e.getY() - grid.y) / (grid.height / 11);
                // TODO: Process event
            } else if (newGame != null && newGame.contains(e.getPoint())) {
                // TODO: Confirm and reset
                reset();
            } else if (saveGame != null && saveGame.contains(e.getPoint())) {
                // TODO: Show save dialog and save
            } else if (loadGame != null && loadGame.contains(e.getPoint())) {
                // TODO: Show load dialog and load
            } else if (exitGame != null && exitGame.contains(e.getPoint())) {
                // TODO: Confirm
                System.exit(0);
            }
        }
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        int gridS = Math.min((getWidth() - 20), (getHeight() - 200)) / 11;
        int gridX = getWidth() / 2 - (gridS * 11) / 2;
        int gridY = 115;
        grid = new Rectangle(gridX, gridY, gridS * 11, gridS * 11);

        // Paint Title
        String title = "Hnefatafl";
        g.setColor(Color.WHITE);
        setMaxFontSize(g, title, (gridS * 11) - 10, 100);
        int width = g.getFontMetrics().stringWidth(title);
        g.drawString(title, getWidth() / 2 - width / 2, 100);

        // Paint board background
        g.setColor(new Color(0, 196, 0));
        g.fillRect(gridX, gridY, gridS * 11, gridS * 11);

        // Paint corners and throne
        g.setColor(new Color(0, 128, 0));
        g.fillRect(gridX, gridY, gridS, gridS);
        g.fillRect(gridX, gridY + gridS * 10, gridS, gridS);
        g.fillRect(gridX + gridS * 5, gridY + gridS * 5, gridS, gridS);
        g.fillRect(gridX + gridS * 10, gridY, gridS, gridS);
        g.fillRect(gridX + gridS * 10, gridY + gridS * 10, gridS, gridS);

        int y = gridY;
        for (int r=0; r<11; r++) {
            int x = gridX;
            for (int c=0; c < 11; c++) {
                // Draw Piece
                if (board[r][c].equals(GridSquareState.ATTACKER)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x + 5, y + 5, gridS - 10, gridS - 10);
                } else if (board[r][c].equals(GridSquareState.DEFENDER)) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x + 5, y + 5, gridS - 10, gridS - 10);
                } else if (board[r][c].equals(GridSquareState.KING)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x + 5, y + 5, gridS - 10, gridS - 10);
                }

                // Draw Border
                g.setColor(new Color(0, 64, 0));
                g.drawRect(x, y, gridS, gridS);

                // Increment x
                x += gridS;
            }
            y += gridS;
        }

        // Paint whose turn it is (or who won)
        g.setColor(Color.WHITE);
        String turn = attacking ? "Attacker's " : "Defender's ";
        turn += gameOver ? "Won" : "Turn";
        setMaxFontSize(g, turn, (gridS * 11) - 10, 25);
        width = g.getFontMetrics().stringWidth(turn);
        int height = g.getFontMetrics().getHeight();
        g.drawString(turn, getWidth() / 2 - width / 2, y + height);

        // Paint buttons
        setMaxFontSize(g, "MMMMMMMMM", gridS * 11 / 5, 50);
        // New Game
        String newGameText = "New Game";
        width = g.getFontMetrics().stringWidth(newGameText);
        height = g.getFontMetrics().getHeight();
        newGame = new Rectangle(gridX + gridS * 11 / 5 - width / 2,
                getHeight() - 25 - height,
                width, height);
        g.drawString(newGameText, newGame.x, newGame.y + height);

        // Save
        String saveText = "Save Game";
        width = g.getFontMetrics().stringWidth(saveText);
        height = g.getFontMetrics().getHeight();
        saveGame = new Rectangle(gridX + gridS * 22 / 5 - width / 2,
                getHeight() - 25 - height,
                width, height);
        g.drawString(saveText, saveGame.x, saveGame.y + height);

        // Load
        String loadText = "Load Game";
        width = g.getFontMetrics().stringWidth(loadText);
        height = g.getFontMetrics().getHeight();
        loadGame = new Rectangle(gridX + gridS * 33 / 5 - width / 2,
                getHeight() - 25 - height,
                width, height);
        g.drawString(loadText, loadGame.x, loadGame.y + height);

        // Exit
        String exitText = "Exit Game";
        width = g.getFontMetrics().stringWidth(exitText);
        height = g.getFontMetrics().getHeight();
        exitGame = new Rectangle(gridX + gridS * 44 / 5 - width / 2,
                getHeight() - 25 - height,
                width, height);
        g.drawString(exitText, exitGame.x, exitGame.y + height);
    }

    private void setMaxFontSize(Graphics g, String text, int maxw, int maxh) {
        g.setFont(g.getFont().deriveFont(1f));
        FontMetrics fm = g.getFontMetrics();
        while (fm.stringWidth(text) < maxw && fm.getHeight() < maxh) {
            g.setFont(g.getFont().deriveFont(g.getFont().getSize() + 1f));
            fm = g.getFontMetrics();
        }
        g.setFont(g.getFont().deriveFont(g.getFont().getSize() - 1f));
    }
}

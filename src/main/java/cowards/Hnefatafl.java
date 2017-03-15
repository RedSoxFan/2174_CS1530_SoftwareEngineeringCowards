package cowards;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Hnefatafl extends JFrame {
  public static void main(String[] args) {
    new Hnefatafl();
  }

  private HnefataflPanel game;

  /**
    Constructor.
   */
  public Hnefatafl() {
    // Set window properties.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("Copenhagen Hnefatafl 11x11");

    // Create a panel.
    game = new HnefataflPanel();
    add(game);

    // Set size and display game.
    setMinimumSize(new Dimension(480, 640));
    setSize(new Dimension(570, 750));
    setVisible(true);
  }
}

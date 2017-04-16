package cowards;

import java.awt.*;
import java.util.*;
import java.util.concurrent.Callable;

/**
  Create a menu to display for the game.
 */
public class Menu {
  /**
    The menu items.
   */
  private ArrayList<String> items;

  /**
    The callbacks for the menu items.
   */
  private HashMap<String, Callable<Boolean>> listeners;

  /**
    The bounds of the items as they are painted.
   */
  private HashMap<String, Rectangle> buttons;

  /**
    The menu title.
   */
  private String title;

  /**
    The margins around the border.
   */
  private static final int BORDER = 30;

  /**
    The vertical separation of items.
   */
  private static final int SEPARATION = 20;

  /**
    Create a menu.

    @param title The menu title.
   */
  public Menu(String title) {
    this.title = title;
    items = new ArrayList<String>();
    listeners = new HashMap<String, Callable<Boolean>>();
    buttons = new HashMap<String, Rectangle>();
  }

  /**
    Add an item to the menu.

    @param text The text to display.
    @param listener The callback if the item get selected.
   */
  public void addItem(String text, Callable<Boolean> listener) {
    items.add(text);
    listeners.put(text, listener);
  }

  /**
    Handle mouse events.

    @param xloc The x coordinate.
    @param yloc The y coordinate.
    @return Whether or not an item was called.
   */
  public boolean handleMouse(int xloc, int yloc) {
    for (String item : items) {
      if (buttons.get(item) != null && buttons.get(item).contains(xloc, yloc)) {
        Callable<Boolean> cb = listeners.get(item);
        if (cb != null) {
          try {
            return cb.call();
          } catch (Exception ex) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
    Paint the menu. The font should be set before calling.
    
    @param gfx The graphics handle.
    @param bounds The bounds to use. See getSize() for dimensions.
    @param mouse The mouse location relative to the game panel.
   */
  public void paint(Graphics gfx, Rectangle bounds, Point mouse) {
    // Paint the background.
    gfx.setColor(new Color(0, 0, 0, 180));
    gfx.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    gfx.setColor(new Color(0, 0, 0, 212));
    gfx.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
    
    // Determine sizing.
    int yloc = bounds.y + BORDER;
    int height = bounds.height / items.size();
  
    // Font information.
    FontMetrics fm = gfx.getFontMetrics();
    int texth = fm.getHeight();

    // Paint the title.
    gfx.setColor(Color.WHITE);
    int textw = fm.stringWidth(title);
    gfx.drawString(title, bounds.x + bounds.width / 2 - textw / 2, yloc + texth);
    yloc += texth + SEPARATION;

    // Paint the items.
    for (String item : items) {
      textw = fm.stringWidth(item);
      Rectangle button = new Rectangle(bounds.x + bounds.width / 2 - textw / 2,
          yloc, textw, texth);
      buttons.put(item, button);

      if (mouse != null && button.contains(mouse.getX(), mouse.getY())) {
        gfx.setColor(Color.YELLOW);
      } else {
        gfx.setColor(Color.LIGHT_GRAY);
      }

      gfx.drawString(item, button.x, button.y + button.height);
      yloc += texth + SEPARATION;
    }
  }

  /**
    Determine the size of the menu. The font should be set before calling this.

    @param gfx The graphics handle.
    @return The dimensions for the menu.
   */
  public Dimension getSize(Graphics gfx) {
    FontMetrics metrics = gfx.getFontMetrics();

    int width = metrics.stringWidth(title);
    for (String item : items) {
      width = Math.max(width, metrics.stringWidth(item));
    }
    width += BORDER * 2;

    int height = (metrics.getHeight() + SEPARATION) * (items.size() + 1) + BORDER * 2;

    return new Dimension(width, height);
  }
}

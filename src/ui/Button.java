package ui;

import core.ImageStore;
import core.Tile;

import java.awt.*;

class Button<T> {
  public static final Color BUTTON_COLOR = new Color(100, 100, 100);
  public static final Color BUTTON_HIGHLIGHTED_COLOR = new Color(130, 130, 140);
  public static final Color BUTTON_DISABLED_COLOR = new Color(50, 50, 50);

  public static final int BUTTON_MARGIN = 5;
  public static final int BUTTON_SIZE = Tile.TILE_WIDTH + (BUTTON_MARGIN * 2);

  public T value;
  public int buttonIndex;
  public Rectangle rect;
  public String imageId;
  public boolean highlighted = false;
  public boolean enabled = true;

  public Button(int buttonIndex, T value, String imageId) {
    this.buttonIndex = buttonIndex;
    this.value = value;
    this.imageId = imageId;
  }

  public void setRect(Rectangle rect) {
    this.rect = rect;
  }

  public void draw(Graphics2D g) {

    if (enabled && highlighted) {
      g.setColor(BUTTON_HIGHLIGHTED_COLOR);
    } else if (enabled) {
      g.setColor(BUTTON_COLOR);
    } else {
      g.setColor(BUTTON_DISABLED_COLOR);
    }

    g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 6, 6);

    Image img = ImageStore.getInstance().getImage(imageId);
    g.drawImage(img, rect.x + BUTTON_MARGIN, rect.y + BUTTON_MARGIN, null);


    g.setColor(new Color(200, 200, 200));
    g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 6, 6);
  }
}

package core;

import java.awt.*;

@SuppressWarnings("serial")
public class Tile extends Rectangle {

  public static final int TILE_WIDTH = 48;
  public static final int TILE_HEIGHT = 48;

  public String imageId;

  public Tile() {
    super(0, 0, TILE_WIDTH, TILE_HEIGHT);
  }

  public Tile(int x, int y) {
    super(x, y, TILE_WIDTH, TILE_HEIGHT);
  }

  public Point getCentre() {
    return new Point((int) getCenterX(), (int) getCenterY());
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }
}

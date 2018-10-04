package core;

import java.awt.*;

@SuppressWarnings("serial") public class Tile extends Rectangle {

  public static final int TILE_WIDTH = 48;
  public static final int TILE_HEIGHT = 48;

  public String textureId;

  public Tile() {
    super(0, 0, TILE_WIDTH, TILE_HEIGHT);
  }

  public Tile(int x, int y) {
    super(x, y, TILE_WIDTH, TILE_HEIGHT);
  }

  public Point getCenter() {
    return new Point((int) getCenterX(), (int) getCenterY());
  }

  public void setTextureId(String id) {
    textureId = id;
  }
}

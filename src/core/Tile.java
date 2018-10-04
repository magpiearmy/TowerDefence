package core;

import java.awt.*;

@SuppressWarnings("serial") public class Tile extends Rectangle {

  public static final int WIDTH = 48;
  public static final int HEIGHT = 48;

  public String textureId;

  public Tile() {
    setBounds(0, 0, WIDTH, HEIGHT);
  }

  public Tile(int x, int y) {
    setBounds(x, y, WIDTH, HEIGHT);
  }

  public Point getCenter() {
    return new Point((int) getCenterX(), (int) getCenterY());
  }

  public void setTextureId(String id) {
    textureId = id;
  }

}

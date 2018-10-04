package core;

import java.awt.*;

public abstract class Entity implements GameEntity {

  protected Point position;
  protected int width;
  protected int height;

  protected Entity(Point startPos, int width, int height) {
    position = new Point(startPos);
    this.width = width;
    this.height = height;
  }

  protected Entity(Point startPos) {
    this(startPos, 0, 0);
  }

  protected void setSize(int w, int h) {
    width = w;
    height = h;
  }

  public Point getPosition() {
    return position;
  }

  public Rectangle getBoundingRect() {
    return new Rectangle(position.x - width/2, position.y - height/2, width, height);
  }
}

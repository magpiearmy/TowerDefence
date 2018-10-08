package core;

import java.awt.*;

public abstract class Entity implements GameEntity {

  protected Point position;
  protected int width;
  protected int height;

  protected Entity(Point origin, int width, int height) {
    position = new Point(origin.x - width / 2, origin.y - height / 2);
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

  public Point getCentre() {
    Point centre = new Point(position);
    centre.translate(width / 2, height / 2);
    return centre;
  }

  public Point getPosition() {
    return position;
  }

  public Rectangle getBoundingRect() {
    return new Rectangle(position.x, position.y, width, height);
  }
}

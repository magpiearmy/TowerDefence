package core;

import java.awt.*;

public class Circle {

  private static double pi = Math.PI;

  protected Point position;
  protected int radius;


  public Circle(Point position, int radius) {
    this.position = position;
    this.radius = radius;
  }

  public Circle(int x, int y, int radius) {
    this.position = new Point(x, y);
    this.radius = radius;
  }

  public int getX() {
    return position.x;
  }

  public int getBoundsX() {
    return position.x - radius;
  }

  public int getY() {
    return position.y;
  }

  public int getBoundsY() {
    return position.y - radius;
  }

  public int getRadius() {
    return radius;
  }

  public int getArea() {
    return (int) (pi * radius * radius);
  }

  public double getCircumference() {
    return (int) (pi * (radius * 2));
  }

  public boolean contains(Point p) {
    int dx = p.x - position.x;
    int dy = p.y - position.y;
    return (int) Math.sqrt(dx * dx + dy * dy) < radius;
  }
}

package core;

public class Vector2D {
  public int x, y;

  public Vector2D() {
    x = 0;
    y = 0;
  }

  public Vector2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean compareTo(Vector2D other) {
    return (this.x == other.x && this.y == other.y);
  }
}

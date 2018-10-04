package core;

@SuppressWarnings("serial") public class PathTile extends Tile {

  public interface Direction {
    int UP = 1;
    int RIGHT = 2;
    int DOWN = 3;
    int LEFT = 4;
  }


  private final int direction;

  public PathTile(int direction) {
    this.direction = direction;
  }

  public Vector2D getNextTileXY() {
    int xCoord = this.x / Tile.WIDTH;
    int yCoord = this.y / Tile.HEIGHT;

    switch (direction) {
      case Direction.UP:
        return new Vector2D(xCoord, yCoord - 1);
      case Direction.RIGHT:
        return new Vector2D(xCoord + 1, yCoord);
      case Direction.DOWN:
        return new Vector2D(xCoord, yCoord + 1);
      case Direction.LEFT:
        return new Vector2D(xCoord - 1, yCoord);
      default:
        return null;
    }
  }
}

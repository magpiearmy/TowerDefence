package core;

import java.awt.Point;

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

  public Point getNextTileXY() {
    int tileX = this.x / Tile.TILE_WIDTH;
    int tileY = this.y / Tile.TILE_HEIGHT;

    switch (direction) {
      case Direction.UP:
        return new Point(tileX, tileY - 1);
      case Direction.RIGHT:
        return new Point(tileX + 1, tileY);
      case Direction.DOWN:
        return new Point(tileX, tileY + 1);
      case Direction.LEFT:
        return new Point(tileX - 1, tileY);
      default:
        return null;
    }
  }
}

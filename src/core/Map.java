package core;

import java.awt.*;
import java.util.Vector;

public class Map {

  private int mapData[][];

  private Vector<Point> waypoints;

  private Point start;
  private Point end;
  private final int width;
  private final int height;

  public Map(int mapData[][], Point start, Point end) {
    this.mapData = mapData;
    width = this.mapData[0].length;
    height = this.mapData.length;

    this.start = start;
    this.end = end;
  }

  public int getTile(int x, int y) {
    return mapData[y][x];
  }

  public Point getStart() {
    return start;
  }

  public Point getEnd() {
    return end;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Vector<Point> getWaypoints() {
    return waypoints;
  }

  public void loadPath() {

    Vector<Point> pathTiles = getPathTiles();
    waypoints = new Vector<>();
    boolean pathComplete = false;
    boolean changedDirection = false;
    int dir = Direction.RIGHT;
    Point currentTile = start;

    while (!pathComplete) {

      Point nextTile = getNextTilePos(currentTile, dir);

      if (pathTiles.contains(nextTile)) {
        pathTiles.remove(nextTile);

        if (changedDirection) {
          addWaypoint(new Point(currentTile.x, currentTile.y));
          changedDirection = false;
        }

        if (nextTile.equals(end)) {
          addWaypoint(end);
          pathComplete = true;
        } else {
          currentTile = nextTile;
        }

      } else {
        dir = (dir % 4) + 1;
        changedDirection = true;
      }
    }
  }

  private Vector<Point> getPathTiles() {
    Vector<Point> path = new Vector<>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (getTile(x, y) == 1) {
          path.add(new Point(x, y));
        }
      }
    }
    return path;
  }

  private void addWaypoint(Point point) {
    point.x = (point.x * Tile.WIDTH) + (Tile.WIDTH / 2);
    point.y = (point.y * Tile.HEIGHT) + (Tile.HEIGHT / 2);
    waypoints.add(point);
  }

  private Point getNextTilePos(Point current, int dir) {
    switch (dir) {
      case Direction.UP:
        return new Point(current.x, current.y - 1);
      case Direction.RIGHT:
        return new Point(current.x + 1, current.y);
      case Direction.DOWN:
        return new Point(current.x, current.y + 1);
      case Direction.LEFT:
        return new Point(current.x - 1, current.y);
      default:
        throw new RuntimeException("Invalid direction value");
    }
  }
}

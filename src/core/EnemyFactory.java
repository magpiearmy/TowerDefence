package core;

import java.awt.*;
import java.util.Vector;

public class EnemyFactory {
  private Point startPos;
  private Vector<Point> waypoints;

  private String enemyImg;
  private String enemyDyingImg;

  public EnemyFactory(Point startPos, Vector<Point> waypoints, ImageStore imgStore) {
    this.startPos = startPos;
    this.waypoints = waypoints;

    enemyImg = imgStore.loadImage("enemy1.png");
    enemyDyingImg = imgStore.loadImage("enemy2.png");
  }

  public Enemy createEnemy(int enemyType) {
    return new Enemy(startPos, waypoints, enemyImg, enemyDyingImg, 1);
  }
}

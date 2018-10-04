package towers;

import core.Enemy;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial") public class BlastTower extends Tower {

  private int blastDamage;
  private int reloadTime;
  private int timeSinceFired;
  private int drawTime;

  public BlastTower(int x, int y, int range) {
    super(x, y, range);
  }

  public void setBlastDamage(int blastDamage) {
    this.blastDamage = blastDamage;
  }

  public void setReloadTime(int reloadTime) {
    this.reloadTime = reloadTime;
    timeSinceFired = reloadTime;
  }

  public void update(long elapsed) {
    timeSinceFired += elapsed;
    if (drawTime > 0)
      drawTime -= elapsed;
    if (drawTime < 0)
      drawTime = 0;
  }

  public boolean fire(Vector<Enemy> enemies) {
    if (timeSinceFired < reloadTime)
      return false;
    for (int i = 0; i < enemies.size(); i++) {
      Enemy thisEnemy = enemies.elementAt(i);
      if (thisEnemy.isAlive()) {
        Point enemyCenter = new Point((int) thisEnemy.getCenterX(), (int) thisEnemy.getCenterY());
        if (fireRadius.contains(enemyCenter)) {
          drawTime = 80;
          timeSinceFired = 0;
          thisEnemy.hit(blastDamage);
        }
      }
    }
    return true;
  }

  public void draw(Graphics2D gfx) {
    super.draw(gfx);
    if (drawTime > 0) {
      gfx.setColor(new Color(200, 200, 200, 120));
      gfx.fillOval(fireRadius.getBoundsX(), fireRadius.getBoundsY(), fireRadius.getRadius() * 2,
        fireRadius.getRadius() * 2);
    }
  }
}

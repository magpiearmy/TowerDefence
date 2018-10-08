package towers;

import core.Enemy;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class BlastTower extends Tower {

  private int blastDamage;
  private int reloadTime;
  private int timeSinceFired;
  private int drawTime;

  public BlastTower(Point pos, int range) {
    super(pos, range);
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

    for (Enemy enemy : enemies) {
      if (enemy.isAlive()) {
        Point enemyCentre = new Point(enemy.getCentre());
        if (fireRadius.contains(enemyCentre)) {
          drawTime = 80;
          timeSinceFired = 0;
          enemy.hit(blastDamage);
        }
      }
    }

    return true;
  }

  public void draw(Graphics2D g) {
    super.draw(g);

    if (drawTime > 0) {
      g.setColor(new Color(200, 200, 200, 120));
      g.fillOval(fireRadius.getBoundsX(), fireRadius.getBoundsY(), fireRadius.getRadius() * 2,
        fireRadius.getRadius() * 2);
    }
  }
}

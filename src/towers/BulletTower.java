package towers;

import core.BulletManager;
import core.Enemy;
import core.Bullet;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial") public class BulletTower extends Tower {

  public static final int MAX_DAMAGE = 30;

  private BulletManager bulletManager;
  private int bulletSpeed = 0; // Pixels per second
  private int hitDamage = 0;
  private int reloadTime; // Reload time (in milliseconds)
  private int timeSinceFired;

  public BulletTower(Point pos, int range, BulletManager bulletManager) {
    super(pos, range);
    this.bulletManager = bulletManager;
  }

  public void setDamage(int hitDamage) {
    this.hitDamage = hitDamage;
  }

  public void setReloadTime(int reloadTime) {
    this.reloadTime = reloadTime;
    timeSinceFired = reloadTime;
  }

  public void setBulletSpeed(int bulletSpeed) {
    this.bulletSpeed = bulletSpeed;
  }

  public boolean isReloaded() {
    return (timeSinceFired >= reloadTime);
  }

  public boolean fire(Vector<Enemy> enemies) {
    if (timeSinceFired >= reloadTime) {
      for (int i = 0; i < enemies.size(); i++) {
        Enemy enemy = enemies.elementAt(i);

        if (enemy.isAlive() && fireRadius.contains(enemy.getPosition())) {
          timeSinceFired = 0;

          bulletManager.addBullet(createBullet(enemy));

          return true;
        }
      }
    }

    return false;
  }

  private Bullet createBullet(Enemy enemy) {
    int bulletX = getCentre().x - Bullet.RADIUS / 2;
    int bulletY = getCentre().y - Bullet.RADIUS / 2;
    return new Bullet(new Point(bulletX, bulletY), enemy, hitDamage, bulletSpeed);
  }

  public void update(long elapsed) {
    super.update(elapsed);
    timeSinceFired += elapsed;
  }

  public void draw(Graphics2D gfx) {
    super.draw(gfx);
  }

}

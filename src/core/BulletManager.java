package core;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

public class BulletManager {

  private Vector<Bullet> bullets = new Vector<>();
  private Rectangle screenBounds;

  public BulletManager(int boundsWidth, int boundsHeight) {
    screenBounds = new Rectangle(boundsWidth, boundsHeight);
  }

  public void init() {
  }

  public void addBullet(Bullet newBullet) {
    bullets.add(newBullet);
  }

  public void onTargetKilled(Enemy target) {
    bullets.stream()
      .filter(bullet -> bullet.getTarget() == target)
      .forEach(Bullet::onTargetIsDead);
  }

  public void updateBullets(long elapsed) {
    bullets.forEach(bullet -> bullet.update(elapsed));
    bullets.removeIf(bullet -> bullet.isDead() || isBulletOffScreen(bullet));
  }

  public void drawBullets(Graphics2D g) {
    bullets.forEach(bullet -> bullet.draw(g));
  }

  private boolean isBulletOffScreen(Bullet bullet) {
    return !screenBounds.intersects(bullet.getBoundingRect());
  }
}

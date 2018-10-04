package core;

import java.awt.*;
import java.util.Vector;

public class BulletManager {

  private Vector<Projectile> bullets;
  private Rectangle screenBounds;

  public BulletManager(int boundsWidth, int boundsHeight) {
    screenBounds = new Rectangle(boundsWidth, boundsHeight);
    bullets = new Vector<>();
  }

  public void init() {
  }

  public void addBullet(Projectile newBullet) {
    bullets.add(newBullet);
  }

  public void onTargetKilled(Enemy target) {
    for (int i = 0; i < bullets.size(); i++) {
      Projectile thisBullet = bullets.elementAt(i);
      if (thisBullet.getTarget() == target) {
        thisBullet.setTargetDead();
      }
    }
  }

  public void updateBullets(long elapsed) {
    bullets.forEach(bullet -> bullet.update(elapsed));
    bullets.removeIf(bullet -> bullet.isDead() || isBulletOffScreen(bullet));
  }

  public void drawBullets(Graphics2D gfx) {
    bullets.forEach(bullet -> bullet.draw(gfx));
  }

  private boolean isBulletOffScreen(Projectile bullet) {
    return !screenBounds.intersects(bullet.getBoundingRect());
  }
}

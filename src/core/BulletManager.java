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
    for (int i = 0; i < bullets.size(); i++) {
      Projectile thisBullet = bullets.elementAt(i);

      thisBullet.update(elapsed);

      if (thisBullet.isDead() || isBulletOffScreen(thisBullet)) {
        bullets.remove(thisBullet);
        i--;
      }
    }
  }

  public void drawBullets(Graphics2D g) {
    for (int i = 0; i < bullets.size(); i++) {
      Projectile thisBullet = bullets.elementAt(i);
      if (thisBullet.getChargePct() < 80) {
        g.setColor(new Color(155, 155, 155 + thisBullet.getChargePct()));
        g.fillOval(thisBullet.getBoundsX(), thisBullet.getBoundsY(), Projectile.SIZE - 2,
          Projectile.SIZE - 2);
      } else {
        g.setColor(new Color(100, 201, 255));
        g.fillOval(thisBullet.getBoundsX(), thisBullet.getBoundsY(), Projectile.SIZE,
          Projectile.SIZE);
      }
    }
  }

  private boolean isBulletOffScreen(Projectile thisBullet) {
    return !screenBounds.contains(new Point(thisBullet.getX(), thisBullet.getY()));
  }

}

package towers;

import java.awt.*;
import java.util.Vector;

import core.BulletManager;
import core.Enemy;
import core.Projectile;

@SuppressWarnings("serial")
public class BulletTower extends Tower {

	public static final int MAX_DAMAGE = 30;

	private BulletManager bulletManager;
	private int bulletSpeed = 0; // Pixels per second
	private int hitDamage = 0;
	private int reloadTime; // Reload time (in milliseconds)
	private int timeSinceFired;

	public BulletTower(int x, int y, int range, BulletManager manager) {
		super(x, y, range);
		bulletManager = manager;
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
				Point enemyCenter = new Point((int) enemy.getCenterX(), (int) enemy.getCenterY());

				if (enemy.isAlive() && fireRadius.contains(enemyCenter)) {
					timeSinceFired = 0;

					// Create a bullet
					int bulletX = (int) (getCenterX() + Projectile.SIZE / 2);
					int bulletY = (int) (getCenterY() + Projectile.SIZE / 2);
					Projectile newBullet = new Projectile(new Point(bulletX, bulletY), enemy, hitDamage,
						bulletSpeed);
					bulletManager.addBullet(newBullet);
					return true;
				}
			}
		}

		return false;
	}

	public void update(long elapsed) {
		super.update(elapsed);
		timeSinceFired += elapsed;
	}

	public void draw(Graphics2D gfx) {
		super.draw(gfx);
	}

}

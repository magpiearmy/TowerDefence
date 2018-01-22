package towers;

import java.awt.*;
import java.util.Vector;

import core.BulletManager;
import core.Enemy;
import core.Projectile;

@SuppressWarnings("serial")
public class BulletTower extends Tower {

	public static final int MAX_DAMAGE = 30;

	private BulletManager _bulletManager;
	private int _bulletSpeed = 0; // Pixels per second
	private int _hitDamage = 0;
	private int _reloadTime; // Reload time (in milliseconds)
	private int _timeSinceFired;

	public BulletTower(int x, int y, int range, BulletManager manager) {
		super(x, y, range);
		_bulletManager = manager;
	}

	public void setDamage(int hitDamage) {
		_hitDamage = hitDamage;
	}

	public void setReloadTime(int reloadTime) {
		_reloadTime = reloadTime;
		_timeSinceFired = reloadTime;
	}

	public void setBulletSpeed(int bulletSpeed) {
		_bulletSpeed = bulletSpeed;
	}

	public boolean isReloaded() {
		return (_timeSinceFired >= _reloadTime);
	}

	public boolean fire(Vector<Enemy> enemies) {
		if (_timeSinceFired >= _reloadTime) {
			for (int i = 0; i < enemies.size(); i++) {
				Enemy enemy = enemies.elementAt(i);
				Point enemyCenter = new Point((int) enemy.getCenterX(), (int) enemy.getCenterY());

				if (enemy.isAlive() && fireRadius.contains(enemyCenter)) {
					_timeSinceFired = 0;

					// Create a bullet
					int bulletX = (int) (getCenterX() + Projectile.SIZE / 2);
					int bulletY = (int) (getCenterY() + Projectile.SIZE / 2);
					Projectile newBullet = new Projectile(new Point(bulletX, bulletY), enemy, _hitDamage, _bulletSpeed);
					_bulletManager.addBullet(newBullet);
					return true;
				}
			}
		}

		return false;
	}

	public void update(long elapsed) {
		super.update(elapsed);
		_timeSinceFired += elapsed;
	}

	public void draw(Graphics2D gfx) {
		super.draw(gfx);
	}

}

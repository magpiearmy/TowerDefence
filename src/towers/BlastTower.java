package towers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import core.Enemy;

@SuppressWarnings("serial")
public class BlastTower extends Tower {
	
	private int _blastDamage;
	private int _reloadTime;
	private int _timeSinceFired;
	private int _drawTime;

	public BlastTower(int x, int y, int range) {
		super(x, y, range);
	}

	public void setBlastDamage(int blastDamage) {
		_blastDamage = blastDamage;
	}

	public void setReloadTime(int reloadTime) {
		_reloadTime = reloadTime;
		_timeSinceFired = reloadTime;
	}

	public void update(long elapsed) {
		_timeSinceFired += elapsed;
		if (_drawTime > 0)
			_drawTime -= elapsed;
		if (_drawTime < 0)
			_drawTime = 0;
	}

	public boolean fire(Vector<Enemy> enemies) {
		// We can't fire until the reload time has elapsed
		if (_timeSinceFired < _reloadTime)
			return false;

		// We can fire now so damage all enemies within range
		for (int i = 0; i < enemies.size(); i++) {
			Enemy thisEnemy = enemies.elementAt(i);
			if (thisEnemy.isAlive()) {
				Point enemyCenter = new Point((int) thisEnemy.getCenterX(), (int) thisEnemy.getCenterY());
				if (fireRadius.contains(enemyCenter)) {
					_drawTime = 80;
					_timeSinceFired = 0;
					thisEnemy.hit(_blastDamage);
				}
			}
		}
		return true;
	}

	public void draw(Graphics2D gfx) {
		super.draw(gfx);
		if (_drawTime > 0) {
			gfx.setColor(new Color(200, 200, 200, 120));
			gfx.fillOval(fireRadius.getBoundsX(), fireRadius.getBoundsY(), fireRadius.getRadius() * 2,
					fireRadius.getRadius() * 2);
		}
	}
}

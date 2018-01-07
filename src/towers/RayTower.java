package towers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Vector;

import core.AnimatedSprite;
import core.Enemy;
import core.ParticleEmitter;

@SuppressWarnings("serial")
public class RayTower extends Tower {
	private Enemy	_target			= null;
	private int		_damagePerSec;
	private float	_damageLeftover	= 0;
	private ParticleEmitter _emitter;
	
	private double	_lastRotation	= 0;
	
	AnimatedSprite 	_sprite;

	public RayTower(int x, int y, int range) {
		super(x, y, range);
		_sprite = new AnimatedSprite("flame_spritesheet.png", 160, 160);
		_elements.addElement(BasicElementType.FIRE);
		_emitter = new ParticleEmitter(new Point(getCenter().x, getCenter().y), this, _elements);
	}
	
	public void setDamagePerSec(int damagePerSec) {
		_damagePerSec = damagePerSec;
	}

	public Enemy getEnemyTarget() { 
		return _target;
	}

	public void update(long elapsed) {
		super.update(elapsed);

//		if (_target != null && !_target.isAlive()) {
//			_target = null;
//			_sprite.deactivate();
//		}
//
//		if (_sprite.isActive())
//			_sprite.update(elapsed);

		if (_target != null && _target.isAlive())
		{
			// Check if current target is still in range
			Point enemyCenter = new Point((int) _target.getCenterX(), (int) _target.getCenterY());
			if (_fireRadius.contains(enemyCenter)) {
				float damageThisFrame = ((float) _damagePerSec / 1000f) * elapsed + _damageLeftover;
				int roundedDamage = (int) Math.floor(damageThisFrame);
				_damageLeftover = damageThisFrame - (float) roundedDamage;
				_target.hit(roundedDamage);
			}
			else {
				_target = null;
				_sprite.deactivate();
			}
		}
		
		_emitter.update(elapsed);
	}

	public boolean fire(Vector<Enemy> enemies) {
		if (_target != null && _target.isAlive())
			return false;
		/*
		 * Point enemyCenter = new Point( (int)enemy.getCenterX(),
		 * (int)enemy.getCenterY()); if (_fireRadius.contains(enemyCenter)) {
		 * _target = enemy; return true; } else return false; // Enemy out of
		 * range
		 */

		for (int i = 0; i < enemies.size(); i++) {
			Enemy thisEnemy = enemies.elementAt(i);
			Point enemyCenter = new Point((int) thisEnemy.getCenterX(), (int) thisEnemy.getCenterY());
			if (thisEnemy.isAlive() && _fireRadius.contains(enemyCenter)) {
				_target = thisEnemy;
				_sprite.activate();
				return true;
			}
		}
		return false;
	}

	public void draw(Graphics2D gfx) {
		
		// Draw the tower texture
		super.draw(gfx);
		_emitter.drawParticles(gfx);

//		if (_target != null) {
//			Stroke prevStroke = gfx.getStroke();
//			Stroke currStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//			gfx.setStroke(currStroke);
//			gfx.setColor(new Color(250, 80, 25));
//			gfx.drawLine((int) getCenterX(), (int) getCenterY(), (int) _target.getCenterX(), (int) _target.getCenterY());
//			gfx.setStroke(prevStroke);
//		}
		
		// Draw the sprite with the correct orientation
//		if (_sprite.isActive()) {
//			double rotation;
//			if (_target != null && _target.isAlive()) {
//				double dx = _target.getCenterX() - getCenterX();
//				double dy = _target.getCenterY() - getCenterY();
//				rotation = Math.atan2(dy, dx) + Math.PI*0.5;
//			} else {
//				rotation = _lastRotation;
//			}
//			_sprite.draw(gfx, (int)getCenterX(), (int)getCenterY(), rotation);
//			_lastRotation = rotation;
//		}
		
	}
}

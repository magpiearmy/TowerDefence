package core;

import java.awt.Point;
import java.util.Random;

public class Projectile extends Circle {

	public static final int SIZE = 7;
	private static final int MAX_CHARGE_DISTANCE = Tile.HEIGHT/2 + 8; //8px outside the tower bounds
	private static final double MAX_CHARGE_TIME = 275; //ms
	
	private enum State { CHARGING, FIRING };
	private State _state = State.CHARGING;
	private Point _chargeEndPos;
	private Point _chargeStartPos;
	private double _chargeTime = MAX_CHARGE_TIME;

	private double _dirX = 0;
	private double _dirY = 0;
	private double _xOverflow = 0;
	private double _yOverflow = 0;

	private int _hitDamage;
	private double _speed;
	private Enemy _target;

	boolean _isDead = false;

	public Projectile(Point startCentre, Enemy target, int hitDamage, int speed) {
		super(startCentre, SIZE); // Init Circle 

		this._target = target;
		this._speed = speed;
		this._hitDamage = hitDamage;
		this._chargeStartPos = new Point(position.x, position.y);

		// Generate a random angle in radians
		Random r = new Random();
		int radians = r.nextInt() % (int)(2*Math.PI);
		
		// Set the end position of the charge sequence based on the random angle
		int chargeX = (int) (getX() + MAX_CHARGE_DISTANCE * Math.cos(radians));
		int chargeY = (int) (getY() + MAX_CHARGE_DISTANCE * Math.sin(radians));
		_chargeEndPos = new Point(chargeX, chargeY);
	}

	public boolean isDead() {
		return _isDead;
	}

	public Enemy getTarget() {
		return _target;
	}

	public void setTargetDead() {
		_target = null;
	}
	
	public int getChargePct() {
		return (int)((1.0f - (_chargeTime / MAX_CHARGE_TIME))*100);
	}
	
	public void update(long elapsed) {

		if (_isDead) return;

		// The distance to move along both axis
		int moveX, moveY;
		
		switch(_state) {
		case CHARGING: {
			
			_chargeTime -= elapsed;

			
			if (_chargeTime <= 0) {
				_chargeTime = 0;
				position = _chargeEndPos;
			} else {
				
				// Get current charge as a % of total charge time
				double chargePct = 1.0f - (_chargeTime / MAX_CHARGE_TIME);
				Point deltaPos = new Point((int)(((_chargeEndPos.x - _chargeStartPos.x) * chargePct)),
										   (int)(((_chargeEndPos.y - _chargeStartPos.y) * chargePct)));
				
				position.x = _chargeStartPos.x + deltaPos.x;
				position.y = _chargeStartPos.y + deltaPos.y;
			}
			
			// Move to next state when fully charged
			if (position.equals(_chargeEndPos)) {
				_xOverflow = 0;
				_yOverflow = 0;
				_dirX = 0;
				_dirY = 0;
				_state = State.FIRING;
			}
			break;
		}
		case FIRING: {
	
			// If the target has been killed, just continue in the same direction
			if (_target == null || !_target.isAlive())
			{
	
				// If we never had chance to move before our target died just kill the bullet
				if (_dirX == 0.0f && _dirY == 0.0f)
				{
					_isDead = true;
					return;
				}
				
				double distThisFrame = ((double)elapsed / 1000d) * _speed;
				double actualMoveX = _dirX * distThisFrame;
				double actualMoveY = _dirY * distThisFrame;
				moveX = (int)Math.floor(actualMoveX + _xOverflow);
				moveY = (int)Math.floor(actualMoveY + _yOverflow);
				_xOverflow += actualMoveX - (double)moveX;
				_yOverflow += actualMoveY - (double)moveY;
				move(moveX, moveY);
			}
			else // Otherwise, head for the target
			{
				// Get latest position of target
				Point targetPos = new Point((int) _target.getCenterX(),
						(int) _target.getCenterY());
				
				double distanceToTarget = position.distance(targetPos);
				double dx = targetPos.x - position.x;
				double dy = targetPos.y - position.y;
	
				double distanceThisFrame = ((double) elapsed / 1000f) * _speed;
				
				if (distanceThisFrame > distanceToTarget)
					distanceThisFrame = distanceToTarget;
				
				// Update the direction
				_dirX = dx / distanceToTarget;
				_dirY = dy / distanceToTarget;
	
				// Calculate the distance to move
				double actualMoveX = _dirX * distanceThisFrame;
				double actualMoveY = _dirY * distanceThisFrame;
	
				moveX = (int)Math.floor(actualMoveX + _xOverflow);
				moveY = (int)Math.floor(actualMoveY + _yOverflow);
	
				// When the distance to move this frame includes a fraction of one
				// pixel, we can't represent that on screen so we accrue the
				// move distance 'overflow' and move a full pixel when we've
				// accrued enough.
				_xOverflow += actualMoveX - (double)moveX;
				_yOverflow += actualMoveY - (double)moveY;
	
				// Move!
				move(moveX, moveY);
	
				// Check if we've hit it this update
				boolean hitTarget = contains(new Point((int) _target.getBounds()
						.getCenterX(), (int) _target.getBounds().getCenterY()));
				if (hitTarget) {
					_target.hit(_hitDamage);
					_isDead = true;
				}
			}
			break;
		}
		}
	}

	private void move(int dx, int dy) {
		position.x += dx;
		position.y += dy;
	}

}

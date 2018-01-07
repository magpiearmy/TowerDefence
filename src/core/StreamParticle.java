package core;

import java.awt.Graphics2D;
import java.awt.Point;

public class StreamParticle {

	private ParticleEmitter _emitter;
	
	private Enemy 		_target;
	private String		_imgId;
	private boolean 	_isDead = false;

	//TODO: Refactor to common class shared by this and Projectile
	private Point _position;
	private Point _lastTargetPos = null;
	private Vector2D 	_size;
	private double _speed;
	private double _dirX = 0;
	private double _dirY = 0;
	private double _xOverflow = 0;
	private double _yOverflow = 0;
	
	public StreamParticle(ParticleEmitter emitter, Enemy enemyTarget) {
		_emitter = emitter;
		_position = new Point(_emitter.getPos());
		_target = enemyTarget;
		_speed = 220;
	}
	
	public void init(String imgId) {
		_imgId = imgId;
		_size = new Vector2D(
				ImageStore.get().getImage(_imgId).getWidth(null),
				ImageStore.get().getImage(_imgId).getHeight(null));
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public void update(long elapsed) {
		
		if (_isDead) return;
		
		// Calculate the straight-line distance we can move this frame (distance = speed * time)
		double distanceToMove = _speed * ((double) elapsed / 1000f);
		
		// Check if the target is already dead
		if (_target == null || !_target.isAlive())
		{
			// If we never had chance to move before our target died just kill the bullet
			if ((_dirX == 0.0f && _dirY == 0.0f) || _lastTargetPos == null)
			{
				_isDead = true;
				return;
			}

			moveTowardTarget(_lastTargetPos, distanceToMove);
			if (_lastTargetPos.equals(_position)) {
				_isDead = true;
			}
		}
		else // Otherwise, head for the target
		{
			// Get latest _position of target
			Point targetPos = new Point((int) _target.getCenterX(),
					(int) _target.getCenterY());
			
			_lastTargetPos = targetPos;

			moveTowardTarget(targetPos, distanceToMove);
			
			// Check if we've hit it this update
			if (_target.getHitBox().contains(_position)) {
				_isDead = true;
			}
		}
	}
	
	private void moveTowardTarget(Point targetPos, double distanceToMove) {

		// The distance to move along both axis
		int moveX, moveY;
		
		double distanceToTarget = _position.distance(targetPos);
		double dx = targetPos.x - _position.x;
		double dy = targetPos.y - _position.y;
		
		if (distanceToMove > distanceToTarget)
			distanceToMove = distanceToTarget;
		
		// Update the direction
		_dirX = dx / distanceToTarget;
		_dirY = dy / distanceToTarget;

		// Calculate the distance to move
		double actualMoveX = _dirX * distanceToMove;
		double actualMoveY = _dirY * distanceToMove;

		moveX = (int)Math.floor(actualMoveX + _xOverflow);
		moveY = (int)Math.floor(actualMoveY + _yOverflow);

		// When the distance to move this frame includes a fraction of one
		// pixel, we can't represent that on screen so we accrue the
		// move distance 'overflow' and move a full pixel when we've
		// accrued enough.
		_xOverflow += actualMoveX - (double)moveX;
		_yOverflow += actualMoveY - (double)moveY;

		// Move!
		_position.translate(moveX, moveY);
	}
	
	public void draw(Graphics2D gfx) {
		if (_isDead) return;
		int posX = _position.x - _size.x/2;
		int posY = _position.y - _size.y/2;
		gfx.drawImage(ImageStore.get().getImage(_imgId), posX, posY, null);
	}
}

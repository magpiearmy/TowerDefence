package core;

import java.awt.Graphics2D;
import java.awt.Point;

public class StreamParticle {

	private ParticleEmitter emitter;
	
	private Enemy target;
	private String imgId;
	private boolean isDead = false;

	//TODO: Refactor to common class shared by this and Projectile
	private Point position;
	private Point lastTargetPos = null;
	private Vector2D size;
	private double speed;
	private double dirX = 0;
	private double dirY = 0;
	private double xOverflow = 0;
	private double yOverflow = 0;
	
	public StreamParticle(ParticleEmitter emitter, Enemy target) {
		this.emitter = emitter;
		this.target = target;
		position = new Point(this.emitter.getPos());
		speed = 220;
	}
	
	public void init(String imgId) {
		this.imgId = imgId;
		size = new Vector2D(
				ImageStore.get().getImage(this.imgId).getWidth(null),
				ImageStore.get().getImage(this.imgId).getHeight(null));
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void update(long elapsed) {
		
		if (isDead) return;
		
		// Calculate the straight-line distance we can move this frame (distance = speed * time)
		double distanceToMove = speed * ((double) elapsed / 1000f);
		
		// Check if the target is already dead
		if (target == null || !target.isAlive())
		{
			// If we never had chance to move before our target died just kill the bullet
			if ((dirX == 0.0f && dirY == 0.0f) || lastTargetPos == null)
			{
				isDead = true;
				return;
			}

			moveTowardTarget(lastTargetPos, distanceToMove);
			if (lastTargetPos.equals(position)) {
				isDead = true;
			}
		}
		else // Otherwise, head for the target
		{
			// Get latest position of target
			Point targetPos = new Point((int) target.getCenterX(),
					(int) target.getCenterY());
			
			lastTargetPos = targetPos;

			moveTowardTarget(targetPos, distanceToMove);
			
			// Check if we've hit it this update
			if (target.getHitBox().contains(position)) {
				isDead = true;
			}
		}
	}
	
	private void moveTowardTarget(Point targetPos, double distanceToMove) {

		// The distance to move along both axes
		int moveX, moveY;
		
		double distanceToTarget = position.distance(targetPos);
		double dx = targetPos.x - position.x;
		double dy = targetPos.y - position.y;
		
		if (distanceToMove > distanceToTarget)
			distanceToMove = distanceToTarget;
		
		// Update the direction
		dirX = dx / distanceToTarget;
		dirY = dy / distanceToTarget;

		// Calculate the distance to move
		double actualMoveX = dirX * distanceToMove;
		double actualMoveY = dirY * distanceToMove;

		moveX = (int)Math.floor(actualMoveX + xOverflow);
		moveY = (int)Math.floor(actualMoveY + yOverflow);

		// When the distance to move this frame includes a fraction of one
		// pixel, we can't represent that on screen so we accrue the
		// move distance 'overflow' and move a full pixel when we've
		// accrued enough.
		xOverflow += actualMoveX - (double)moveX;
		yOverflow += actualMoveY - (double)moveY;

		// Move!
		position.translate(moveX, moveY);
	}
	
	public void draw(Graphics2D gfx) {
		if (isDead) return;
		int posX = position.x - size.x/2;
		int posY = position.y - size.y/2;
		gfx.drawImage(ImageStore.get().getImage(imgId), posX, posY, null);
	}
}

package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

/**
 * Manages, updates and renders all bullets on the screen.
 * @author Adam
 */
public class BulletManager {

	private Vector<Projectile> bullets;
	
	private Rectangle bounds;
	
	public BulletManager(int boundsWidth, int boundsHeight)
	{
		bounds = new Rectangle(boundsWidth, boundsHeight);
		bullets = new Vector<Projectile>();
	}
	
	public void init()
	{
	}
	
	public void addBullet(Projectile newBullet)
	{
		bullets.add(newBullet);
	}
	
	public void onTargetKilled(Enemy target)
	{
		for(int i = 0; i < bullets.size(); i++)
		{
			Projectile thisBullet = bullets.elementAt(i);
			if(thisBullet.getTarget() == target)
			{
				thisBullet.setTargetDead();
			}
		}
	}
	
	public void updateBullets(long elapsed)
	{
		for(int i = 0; i < bullets.size(); i++)
		{
			Projectile thisBullet = bullets.elementAt(i);
			
			thisBullet.update(elapsed); // Update
			
			// Check if it's hit it's target or it's gone off-screen
			boolean isOffScreen = !bounds.contains( new Point(thisBullet.getX(), thisBullet.getY()));
			if (thisBullet.isDead() || isOffScreen)
			{
				bullets.remove(thisBullet);
				i--;
			}
		}
	}
	
	public void drawBullets(Graphics2D g)
	{
		for(int i = 0; i < bullets.size(); i++)
		{
			Projectile thisBullet = bullets.elementAt(i);
			if (thisBullet.getChargePct() < 80) {
				g.setColor(new Color(155, 155, 155 + thisBullet.getChargePct()));
				g.fillOval(thisBullet.getBoundsX(), thisBullet.getBoundsY(), Projectile.SIZE-2, Projectile.SIZE-2);
			} else {
				g.setColor(new Color(100, 201, 255));
				g.fillOval(thisBullet.getBoundsX(), thisBullet.getBoundsY(), Projectile.SIZE, Projectile.SIZE);
			}
		}
	}
	
}

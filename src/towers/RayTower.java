package towers;

import core.AnimatedSprite;
import core.Enemy;
import core.ParticleEmitter;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial") public class RayTower extends Tower {
  private Enemy target = null;
  private int damagePerSec;
  private float damageLeftover = 0;
  private ParticleEmitter emitter;

  private double lastRotation = 0;

  private AnimatedSprite sprite;

  public RayTower(int x, int y, int range) {
    super(x, y, range);
    sprite = new AnimatedSprite("flame_spritesheet.png", 160, 160);
    elements.addElement(BasicElementType.FIRE);
    emitter = new ParticleEmitter(new Point(getCenter().x, getCenter().y), this, elements);
  }

  public void setDamagePerSec(int damagePerSec) {
    this.damagePerSec = damagePerSec;
  }

  public Enemy getEnemyTarget() {
    return target;
  }

  public void update(long elapsed) {
    super.update(elapsed);

    if (isCurrentTargetAlive()) {
      if (fireRadius.contains(target.getCentre())) {
        float damageThisFrame = ((float) damagePerSec / 1000f) * elapsed + damageLeftover;
        int roundedDamage = (int) Math.floor(damageThisFrame);
        damageLeftover = damageThisFrame - (float) roundedDamage;
        target.hit(roundedDamage);
      } else {
        target = null;
        sprite.stopAnimating();
      }
    }

    emitter.update(elapsed);
  }

  private boolean isCurrentTargetAlive() {
    return target != null && target.isAlive();
  }

  public boolean fire(Vector<Enemy> enemies) {
    if (isCurrentTargetAlive())
      return false;

    for (int i = 0; i < enemies.size(); i++) {
      Enemy thisEnemy = enemies.elementAt(i);
      if (thisEnemy.isAlive() && fireRadius.contains(thisEnemy.getCentre())) {
        target = thisEnemy;
        sprite.startAnimating();
        return true;
      }
    }
    return false;
  }

  public void draw(Graphics2D gfx) {

    // Draw the tower texture
    super.draw(gfx);
    emitter.drawParticles(gfx);

    //		if (target != null) {
    //			Stroke prevStroke = gfx.getStroke();
    //			Stroke currStroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    //			gfx.setStroke(currStroke);
    //			gfx.setColor(new Color(250, 80, 25));
    //			gfx.drawLine((int) getCenterX(), (int) getCenterY(), (int) target.getCenterX(), (int) target.getCenterY());
    //			gfx.setStroke(prevStroke);
    //		}

    // Draw the sprite with the correct orientation
    //		if (_sprite.isActive()) {
    //			double rotation;
    //			if (target != null && target.isAlive()) {
    //				double dx = target.getCenterX() - getCenterX();
    //				double dy = target.getCenterY() - getCenterY();
    //				rotation = Math.atan2(dy, dx) + Math.PI*0.5;
    //			} else {
    //				rotation = _lastRotation;
    //			}
    //			_sprite.draw(gfx, (int)getCenterX(), (int)getCenterY(), rotation);
    //			_lastRotation = rotation;
    //		}

  }
}

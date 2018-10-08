package towers;

import core.AnimatedSprite;
import core.Enemy;
import core.ParticleEmitter;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class RayTower extends Tower {

  private Enemy target = null;
  private int damagePerSec;
  private float damageLeftover = 0;
  private ParticleEmitter emitter;

  private AnimatedSprite sprite;

  public RayTower(Point pos, int range) {
    super(pos, range);

    sprite = new AnimatedSprite("flame_spritesheet.png", 160, 160);
    elements.addElement(BasicElementType.FIRE);
    emitter = new ParticleEmitter(this, elements);
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
      if (fireRadius.contains(target.getPosition())) {
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
      if (thisEnemy.isAlive() && fireRadius.contains(thisEnemy.getPosition())) {
        target = thisEnemy;
        sprite.startAnimating();
        return true;
      }
    }

    return false;
  }

  public void draw(Graphics2D gfx) {
    super.draw(gfx);

    emitter.draw(gfx);
  }
}

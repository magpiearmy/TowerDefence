package core;

import java.awt.*;
import java.util.Random;

public class Projectile extends /*Circle*/ Entity {

  public static final int RADIUS = 3;
  public static final int DIAMETER = RADIUS * 2;
  private static final int MAX_CHARGE_DISTANCE = Tile.TILE_HEIGHT / 2 + 5;
  private static final double MAX_CHARGE_TIME = 275; //ms

  private enum State {CHARGING, FIRING}


  private State state = State.CHARGING;
  private Point chargeEndPos;
  private Point chargeStartPos;
  private double chargeTime = MAX_CHARGE_TIME;
  private Enemy target;
  private int hitDamage;

  boolean isDead = false;

  private MovementComponent movementComponent;

  public Projectile(Point startPos, Enemy target, int hitDamage, int speed) {
    super(startPos, RADIUS * 2, RADIUS * 2);

    this.target = target;
    this.hitDamage = hitDamage;
    this.chargeStartPos = new Point(position);

    movementComponent = new MovementComponent(speed);
    chargeEndPos = calculateChargeEndPos();
  }

  private Point calculateChargeEndPos() {

    Random r = new Random();
    int radians = r.nextInt() % (int) (2 * Math.PI);

    // Set the end position of the charge sequence based on the random angle
    int chargeX = (int) (position.x + MAX_CHARGE_DISTANCE * Math.cos(radians));
    int chargeY = (int) (position.y + MAX_CHARGE_DISTANCE * Math.sin(radians));

    return new Point(chargeX, chargeY);
  }

  public boolean isDead() {
    return isDead;
  }

  public Enemy getTarget() {
    return target;
  }

  public void setTargetDead() {
    target = null;
  }

  public int getChargePct() {
    return (int) ((1.0f - (chargeTime / MAX_CHARGE_TIME)) * 100);
  }

  @Override public void update(long elapsed) {

    if (isDead)
      return;

    switch (state) {
      case CHARGING: {
        updateChargingPhase(elapsed);
        break;
      }
      case FIRING: {
        updateFiringPhase(elapsed);
        break;
      }
    }
  }

  @Override public void draw(Graphics2D gfx) {
    if (getChargePct() < 80) {
      gfx.setColor(new Color(155, 155, 155 + getChargePct()));
      gfx.fillOval(position.x, position.y, DIAMETER - 2, DIAMETER - 2);
    } else {
      gfx.setColor(new Color(100, 201, 255));
      gfx.fillOval(position.x, position.y, DIAMETER, DIAMETER);
    }
  }

  private void updateFiringPhase(long elapsed) {

    if (isTargetDead()) {

      if (movementComponent.isStationary()) {
        isDead = true;
      } else {
        movementComponent.moveInCurrentDirection(position, elapsed);
      }

    } else {

      movementComponent.moveTowardTarget(position, getTargetPos(), elapsed);

      if (hasHitTarget()) {
        target.hit(hitDamage);
        isDead = true;
      }

    }
  }

  private void updateChargingPhase(long elapsed) {
    chargeTime -= elapsed;

    if (chargeTime <= 0) {
      chargeTime = 0;
      position = chargeEndPos;
    } else {

      // Get current charge as a % of total charge time
      double chargePct = 1.0f - (chargeTime / MAX_CHARGE_TIME);
      Point deltaPos = new Point((int) (((chargeEndPos.x - chargeStartPos.x) * chargePct)),
        (int) (((chargeEndPos.y - chargeStartPos.y) * chargePct)));

      position.x = chargeStartPos.x + deltaPos.x;
      position.y = chargeStartPos.y + deltaPos.y;
    }

    // Move to next state when fully charged
    if (position.equals(chargeEndPos)) {
      state = State.FIRING;
    }
  }

  private boolean hasHitTarget() {
    return getBoundingRect().intersects(target.getBoundingRect());
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  private Point getTargetPos() {
    return new Point(target.position);
  }
}

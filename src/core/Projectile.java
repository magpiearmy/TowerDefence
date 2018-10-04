package core;

import java.awt.*;
import java.util.Random;

public class Projectile extends Circle {

  public static final int SIZE = 7;
  private static final int MAX_CHARGE_DISTANCE = Tile.HEIGHT / 2 + 8; //8px outside the tower bounds
  private static final double MAX_CHARGE_TIME = 275; //ms


  private enum State {CHARGING, FIRING}


  private State state = State.CHARGING;
  private Point chargeEndPos;
  private Point chargeStartPos;
  private double chargeTime = MAX_CHARGE_TIME;
  private Enemy target;
  private int hitDamage;

  boolean isDead = false;

  private PositionComponent positionComponent;

  public Projectile(Point startPos, Enemy target, int hitDamage, int speed) {
    super(startPos, SIZE);

    this.target = target;
    this.hitDamage = hitDamage;
    this.chargeStartPos = new Point(position.x, position.y);

    positionComponent = new PositionComponent(startPos, speed);
    chargeEndPos = calculateChargeEndPos();
  }

  private Point calculateChargeEndPos() {

    Random r = new Random();
    int radians = r.nextInt() % (int) (2 * Math.PI);

    // Set the end position of the charge sequence based on the random angle
    int chargeX = (int) (getX() + MAX_CHARGE_DISTANCE * Math.cos(radians));
    int chargeY = (int) (getY() + MAX_CHARGE_DISTANCE * Math.sin(radians));

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

  public void update(long elapsed) {

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

  private void updateFiringPhase(long elapsed) {

    if (isTargetDead()) {

      if (positionComponent.isStationary()) {
        isDead = true;
      } else {
        positionComponent.moveInCurrentDirection(elapsed);
      }

    } else {

      positionComponent.moveTowardTarget(getTargetPos(), elapsed);
      position = positionComponent.getPosition();

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
    return super.contains(getBoundsOfTarget());
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  private Point getBoundsOfTarget() {
    return new Point((int) target.getBounds().getCenterX(), (int) target.getBounds().getCenterY());
  }

  private Point getTargetPos() {
    return new Point((int) target.getCenterX(), (int) target.getCenterY());
  }
}

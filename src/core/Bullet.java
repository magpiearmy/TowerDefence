package core;

import java.awt.*;
import java.util.Random;

public class Bullet extends Entity {

  private enum State {CHARGING, FIRING}


  public static final int RADIUS = 3;
  public static final int DIAMETER = RADIUS * 2;
  public static final int DIAMETER_WHEN_CHARGING = DIAMETER - 2;
  private static final int MAX_CHARGE_DISTANCE = Tile.TILE_HEIGHT / 2 + 5;
  private static final double MAX_CHARGE_TIME = 300; //ms

  private final double chargeSpeed;
  private final double firingSpeed;

  private State state;
  private Point chargeEndPos;
  private Enemy target;
  private int hitDamage;

  boolean isDead = false;

  private MovementComponent movementComponent;

  public Bullet(Point startPos, Enemy target, int hitDamage, double speed) {
    super(startPos, RADIUS * 2, RADIUS * 2);

    this.target = target;
    this.hitDamage = hitDamage;
    this.chargeSpeed = 100;
    this.firingSpeed = speed;
    this.state = State.CHARGING;
    movementComponent = new MovementComponent(this, chargeSpeed);
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

  public void onTargetIsDead() {
    target = null;
  }

  public int getChargePct() {
    double distanceToEndOfCharge = position.distance(chargeEndPos);
    return (int) (100 - ((distanceToEndOfCharge * 100) / MAX_CHARGE_DISTANCE));
  }

  @Override
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

      if (movementComponent.isStationary()) {
        isDead = true;
      } else {
        movementComponent.moveInCurrentDirection(elapsed);
      }

    } else {

      movementComponent.moveTowardTarget(getTargetPos(), elapsed);

      if (hasHitTarget()) {
        target.hit(hitDamage);
        isDead = true;
      }

    }
  }

  private void updateChargingPhase(long elapsed) {

    if (isTargetDead()) {
      isDead = true;
      return;
    }

    movementComponent.moveTowardTarget(chargeEndPos, elapsed);

    if (getCentre().equals(chargeEndPos)) {
      enterFiringState();
    }
  }

  private void enterFiringState() {
    state = State.FIRING;
    movementComponent.setSpeed(firingSpeed);
  }

  @Override
  public void draw(Graphics2D gfx) {
    if (state == State.CHARGING && getChargePct() < 80) {
      gfx.setColor(new Color(155, 155, 155 + getChargePct()));
      gfx.fillOval(position.x, position.y, DIAMETER_WHEN_CHARGING, DIAMETER_WHEN_CHARGING);
    } else {
      gfx.setColor(new Color(100, 201, 255));
      gfx.fillOval(position.x, position.y, DIAMETER, DIAMETER);
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

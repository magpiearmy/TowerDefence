package core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class StreamParticle extends Entity {

  private static final int WIDTH = 20;
  private static final int HEIGHT = 20;
  private static final int SPEED = 120;

  private Enemy target;
  private String imageId;
  private boolean isDead = false;

  private MovementComponent movementComponent;

  public StreamParticle(Point startPos, Enemy target, String imageId) {
    super(startPos, WIDTH, HEIGHT);

    this.target = target;
    this.imageId = imageId;

    movementComponent = new MovementComponent(this, SPEED);
  }

  public boolean isDead() {
    return isDead;
  }

  public void update(long elapsed) {

    if (isDead)
      return;

    if (isTargetDead()) {
      isDead = true;
      return;
    }

    movementComponent.moveTowardTarget(getPositionOfTarget(), elapsed);

    if (hasHitTarget()) {
      isDead = true;
    }
  }

  private Point getPositionOfTarget() {
    return new Point(target.getCentre());
  }

  private boolean hasHitTarget() {
    return target.getHitBox().contains(getCentre());
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  public void draw(Graphics2D gfx) {
    if (isDead)
      return;

    Image image = ImageStore.getInstance().getImage(imageId);
    gfx.drawImage(image, position.x, position.y, null);
  }
}

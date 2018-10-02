package core;

import java.awt.Graphics2D;
import java.awt.Point;

public class StreamParticle {

  private Enemy target;
  private String imgId;
  private boolean isDead = false;
  private Vector2D size;

  private MovementComponent movementComponent;

  public StreamParticle(Point startPos, Enemy target) {
    this.target = target;

    movementComponent = new MovementComponent(startPos, 200);
  }

  public void init(String imgId) {
    this.imgId = imgId;
    size = new Vector2D(ImageStore.getInstance().getImage(this.imgId).getWidth(null),
      ImageStore.getInstance().getImage(this.imgId).getHeight(null));
  }

  public boolean isDead() {
    return isDead;
  }

  public void update(long elapsed) {

    if (isDead) {
      return;
    } else if (isTargetDead()) {
      isDead = true;
      return;
    }

    movementComponent.moveTowardTarget(getPositionOfTarget(), elapsed);

    if (hasHitTarget()) {
      isDead = true;
    }
  }

  private Point getPositionOfTarget() {
    return new Point((int) target.getCenterX(), (int) target.getCenterY());
  }

  private boolean hasHitTarget() {
    return target.getHitBox().contains(movementComponent.getPosition());
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  public void draw(Graphics2D gfx) {
    if (isDead) return;

    int posX = movementComponent.getPosition().x - size.x / 2;
    int posY = movementComponent.getPosition().y - size.y / 2;
    gfx.drawImage(ImageStore.getInstance().getImage(imgId), posX, posY, null);
  }
}

package core;

import java.awt.*;

public class StreamParticle extends Entity {

  private Enemy target;
  private String imgId;
  private boolean isDead = false;

  private MovementComponent movementComponent;

  public StreamParticle(Point startPos, Enemy target) {
    super(startPos, 0, 0);
    this.target = target;

    movementComponent = new MovementComponent(200);
  }

  public void init(String imgId) {
    this.imgId = imgId;

    Image image = ImageStore.getInstance().getImage(imgId);
    setSize(image.getWidth(null), image.getHeight(null));
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

    movementComponent.moveTowardTarget(position, getPositionOfTarget(), elapsed);

    if (hasHitTarget()) {
      isDead = true;
    }
  }

  private Point getPositionOfTarget() {
    return new Point(target.position);
  }

  private boolean hasHitTarget() {
    return target.getHitBox().contains(position);
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  public void draw(Graphics2D gfx) {
    if (isDead)
      return;

    int posX = position.x - width / 2;
    int posY = position.y - height / 2;
    gfx.drawImage(ImageStore.getInstance().getImage(imgId), posX, posY, null);
  }
}

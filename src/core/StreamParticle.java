package core;

import java.awt.*;

public class StreamParticle {

  private Enemy target;
  private String imgId;
  private boolean isDead = false;
  private Vector2D size;

  private PositionComponent positionComponent;

  public StreamParticle(Point startPos, Enemy target) {
    this.target = target;

    positionComponent = new PositionComponent(startPos, 200);
  }

  public void init(String imgId) {
    this.imgId = imgId;

    Image image = ImageStore.getInstance().getImage(imgId);
    size = new Vector2D(image.getWidth(null), image.getHeight(null));
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

    positionComponent.moveTowardTarget(getPositionOfTarget(), elapsed);

    if (hasHitTarget()) {
      isDead = true;
    }
  }

  private Point getPositionOfTarget() {
    return new Point((int) target.getCenterX(), (int) target.getCenterY());
  }

  private boolean hasHitTarget() {
    return target.getHitBox().contains(positionComponent.getPosition());
  }

  private boolean isTargetDead() {
    return target == null || !target.isAlive();
  }

  public void draw(Graphics2D gfx) {
    if (isDead)
      return;

    int posX = positionComponent.getPosition().x - size.x / 2;
    int posY = positionComponent.getPosition().y - size.y / 2;
    gfx.drawImage(ImageStore.getInstance().getImage(imgId), posX, posY, null);
  }
}

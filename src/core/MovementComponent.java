package core;

import java.awt.*;

public class MovementComponent {

  private Point position;
  private double speed;
  private double dirX = 0;
  private double dirY = 0;
  private double xOverflow = 0;
  private double yOverflow = 0;

  public MovementComponent(Point position, double speed) {
    this.position = position;
    this.speed = speed;
  }

  public Point getPosition() {
    return position;
  }

  public void moveTowardTarget(Point targetPos, long elapsed) {

    double distanceToMove = speed * ((double) elapsed / 1000f);

    // The distance to move along both axes
    int moveX, moveY;

    double distanceToTarget = position.distance(targetPos);
    double dx = targetPos.x - position.x;
    double dy = targetPos.y - position.y;

    if (distanceToMove > distanceToTarget)
      distanceToMove = distanceToTarget;

    // Update the direction
    dirX = dx / distanceToTarget;
    dirY = dy / distanceToTarget;

    // Calculate the true distance to move
    double exactMoveX = dirX * distanceToMove;
    double exactMoveY = dirY * distanceToMove;

    moveX = (int)Math.floor(exactMoveX + xOverflow);
    moveY = (int)Math.floor(exactMoveY + yOverflow);

    // When the distance to move this frame includes a fraction of one
    // pixel, we can't represent that on screen so we accrue the
    // move distance 'overflow' and move a full pixel when we've
    // accrued enough.
    xOverflow += exactMoveX - (double)moveX;
    yOverflow += exactMoveY - (double)moveY;

    // Move!
    position.translate(moveX, moveY);
  }

  public boolean isStationary() {
    return dirX == 0.0f && dirY == 0.0f;
  }
}

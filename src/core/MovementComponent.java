package core;

import java.awt.*;

public class MovementComponent {

  private double speed;
  private double dirX = 0;
  private double dirY = 0;
  private double xOverflow = 0;
  private double yOverflow = 0;

  public MovementComponent(double speed) {
    this.speed = speed;
  }

  public void moveTowardTarget(Point currentPos, Point targetPos, long elapsed) {

    double distanceToMove = speed * ((double) elapsed / 1000f);
    double distanceToTarget = currentPos.distance(targetPos);
    double dx = targetPos.x - currentPos.x;
    double dy = targetPos.y - currentPos.y;

    if (distanceToMove > distanceToTarget)
      distanceToMove = distanceToTarget;

    dirX = dx / distanceToTarget;
    dirY = dy / distanceToTarget;

    // When the distance to move this frame includes a fraction of one
    // pixel, we can't represent that on screen so we accrue the
    // 'overflow' and move a full pixel when we've accrued enough.
    double exactMoveX = dirX * distanceToMove;
    double exactMoveY = dirY * distanceToMove;
    int moveX = (int) Math.floor(exactMoveX + xOverflow);
    int moveY = (int) Math.floor(exactMoveY + yOverflow);
    xOverflow += exactMoveX - (double) moveX;
    yOverflow += exactMoveY - (double) moveY;

    currentPos.translate(moveX, moveY);
  }

  public void moveInCurrentDirection(Point currentPos, long elapsed) {
    double distThisFrame = ((double) elapsed / 1000d) * speed;
    double actualMoveX = dirX * distThisFrame;
    double actualMoveY = dirY * distThisFrame;
    int moveX = (int) Math.floor(actualMoveX + xOverflow);
    int moveY = (int) Math.floor(actualMoveY + yOverflow);
    xOverflow += actualMoveX - (double) moveX;
    yOverflow += actualMoveY - (double) moveY;

    currentPos.translate(moveX, moveY);
  }

  public boolean isStationary() {
    return dirX == 0.0f && dirY == 0.0f;
  }
}

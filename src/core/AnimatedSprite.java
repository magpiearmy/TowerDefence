package core;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class AnimatedSprite {
  private enum AnimationState {
    INACTIVE, WARM_UP, ANIMATING, COOL_DOWN
  }


  private AnimationState animationState;

  // Image data
  private BufferedImage sheet;
  private int frameWidth = 0;
  private int frameHeight = 0;
  private int rowCount = 0;
  private int framesPerRow = 0;

  // Animation data
  private final int warmUpTime = 120;
  private final int coolDownTime = 300;
  private int currentPhaseElapsedTime = 0;
  private int frameNum = 0;

  public AnimatedSprite(String fileName, int frameWidth, int frameHeight) {
    //		try {
    //			sheet = ImageIO.read(new File("/resources/"+fileName));
    //		}
    //		catch (IOException e) {
    //		    e.printStackTrace();
    //		    throw new RuntimeException("Failed to load animated sprite sheet file: " + fileName);
    //		}

    this.frameWidth = frameWidth;
    this.frameHeight = frameHeight;
    //        framesPerRow = sheet.getWidth() / frameWidth;
    //        rowCount = sheet.getHeight() / frameHeight;
    animationState = AnimationState.INACTIVE;
  }

  public void update(long elapsed) {

    switch (animationState) {
      case WARM_UP:
        updateWarmUpPhase(elapsed);
        break;
      case ANIMATING:
        updateAnimatingPhase(elapsed);
        break;
      case COOL_DOWN:
        updateCoolDownPhase(elapsed);
        break;
      default:
        break;
    }
  }

  private void updateCoolDownPhase(long elapsed) {
    final int maxFrames = 7;
    final int timePerFrame = coolDownTime / maxFrames;

    currentPhaseElapsedTime += elapsed;

    if (currentPhaseElapsedTime >= coolDownTime) {
      animationState = AnimationState.INACTIVE;
    } else {
      frameNum = 18 + ((currentPhaseElapsedTime / timePerFrame) % maxFrames);
    }
    return;
  }

  private void updateAnimatingPhase(long elapsed) {
    final int maxFrames = 11;
    final int timePerFrame = 20;

    currentPhaseElapsedTime += elapsed;
    frameNum = 7 + ((currentPhaseElapsedTime / timePerFrame) % maxFrames);

    return;
  }

  private void updateWarmUpPhase(long elapsed) {
    final int maxFrames = 7;
    final int timePerFrame = warmUpTime / maxFrames;

    currentPhaseElapsedTime += elapsed;

    if (currentPhaseElapsedTime >= warmUpTime) {
      animationState = AnimationState.ANIMATING;
      currentPhaseElapsedTime = 0;
    } else {
      frameNum = (currentPhaseElapsedTime / timePerFrame) % maxFrames;
    }
    return;
  }

  public boolean isActive() {
    return animationState != AnimationState.INACTIVE;
  }

  public void startAnimating() {
    animationState = AnimationState.WARM_UP;
    currentPhaseElapsedTime = 0;
  }

  public void stopAnimating() {
    animationState = AnimationState.COOL_DOWN;
    currentPhaseElapsedTime = 0;
  }

  public void draw(Graphics2D gfx, int startX, int startY, double rotation) {
    final int x = (frameNum % framesPerRow) * frameWidth;
    final int y = (int) (Math.floor(frameNum / framesPerRow)) * frameHeight;

    BufferedImage img = sheet.getSubimage(x, y, frameWidth, frameHeight);
    AffineTransform xform = new AffineTransform();
    xform.rotate(rotation, frameWidth / 2, frameHeight / 2);
    AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
    img = op.filter(img, null);

    int radius = frameWidth / 2;
    int baseX = (int) (startX + radius * Math.cos(rotation + Math.PI / 2));
    int baseY = (int) (startY + radius * Math.sin(rotation + Math.PI / 2));

    int drawX = (startX - frameWidth / 2) + (startX - baseX);
    int drawY = (startY - frameHeight / 2) + (startY - baseY);
    gfx.drawImage(img, drawX, drawY, null);
  }
}

package core;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnimatedSprite {
    private enum State {
        INACTIVE, WARM_UP, ANIMATING, COOL_DOWN
    }

    private State state;

    // Image data
    private BufferedImage sheet;
    private int frameWidth = 0;
    private int frameHeight = 0;
    private int rowCount = 0;
    private int framesPerRow = 0;

    // Animation data (all times in milliseconds)
    private final int warmUpTime = 120;
    private final int coolDownTime = 300;
    private int phaseTime = 0;
    private int frame = 0;

	public AnimatedSprite(String fileName, int frameWidth, int frameHeight) {
//		try {
//			sheet = ImageIO.read(new File("/Res/"+fileName));
//		}
//		catch (IOException e) {
//		    e.printStackTrace();
//		    throw new RuntimeException("Failed to load animated sprite sheet file: " + fileName);
//		}

        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
//        framesPerRow = sheet.getWidth() / frameWidth;
//        rowCount = sheet.getHeight() / frameHeight;
        state = State.INACTIVE;
    }

    public void update(long elapsed) {

        switch (state) {
            case WARM_UP: {
                final int maxFrames = 7;
                final int timePerFrame = warmUpTime / maxFrames;

                phaseTime += elapsed;

                if (phaseTime >= warmUpTime) {
                    state = State.ANIMATING;
                    phaseTime = 0;
                } else {
                    frame = (phaseTime / timePerFrame) % maxFrames;
                }
                break;
            }
            case ANIMATING: {
                final int maxFrames = 11;
                final int timePerFrame = 20;

                phaseTime += elapsed;
                frame = 7 + ((phaseTime / timePerFrame) % maxFrames);

                break;
            }
            case COOL_DOWN:
                final int maxFrames = 7;
                final int timePerFrame = coolDownTime / maxFrames;

                phaseTime += elapsed;

                if (phaseTime >= coolDownTime) {
                    state = State.INACTIVE;
                } else {
                    frame = 18 + ((phaseTime / timePerFrame) % maxFrames);
                }
                break;
            default:
                break;
        }
    }

    public boolean isActive() {
        return state != State.INACTIVE;
    }

    /**
     * Activation starts the "warm up" animation sequence
     */
    public void activate() {
        state = State.WARM_UP;
        phaseTime = 0;
    }

    /**
     * Deactivation starts the "cool down" animation sequence
     */
    public void deactivate() {
        state = State.COOL_DOWN;
        phaseTime = 0;
    }

    public void draw(Graphics2D gfx, int startX, int startY, double rotation) {

        final int x = (frame % framesPerRow) * frameWidth;
        final int y = (int) (Math.floor(frame / framesPerRow)) * frameHeight;

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

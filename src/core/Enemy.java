package core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;

@SuppressWarnings("serial")
public class Enemy extends Rectangle implements ISelectable {
    private static final int MAX_HEALTH = 800;
    public static final int SIZE = 48;

    public enum EnemyState {
        STATE_ALIVE, STATE_DYING, STATE_DEAD, STATE_ESCAPED
    }

    // These position variables are used internally to track exact
    // position. The rendering position is defined by the rectangle
    // bounds which are set each frame from these two floats.
    private float posX;
    private float posY;

    private double dirX = 0;
    private double dirY = 0;
    private double xOverflow = 0;
    private double yOverflow = 0;

    // Texture data
    private String textureAlive;
    private String textureDying;

    private int health = MAX_HEALTH;
    private EnemyState state;
    private int escapeCost;
    private int yield;
    private boolean isHit = false;
    private boolean isSelected = false;

    private final long deathDuration = 175; // milliseconds
    private long dieStartTime = 0;
    private float moveSpeed = 0; // pixels per second
    private HealthBar bar;

    private Vector<Point> waypoints;
    private int nextWaypoint;

    public Enemy(Point center, Vector<Point> waypoints, String textureAlive, String textureDying, int escapeCost) {
        posX = center.x - SIZE / 2;
        posY = center.y - SIZE / 2;
        setBounds(x, y, SIZE, SIZE);

        state = EnemyState.STATE_ALIVE;
        moveSpeed = 30;

        this.waypoints = waypoints;
        nextWaypoint = 0;

        this.textureAlive = textureAlive;
        this.textureDying = textureDying;
        this.escapeCost = escapeCost;
        yield = 30;

        bar = new HealthBar((int) posX, (int) posY - 4, width);
        bar.setMaxHP(MAX_HEALTH);
    }

    public Enemy(Enemy other) {
        escapeCost = other.escapeCost;
    }

    public void init() {
    }

    public boolean isAlive() {
        return (state == EnemyState.STATE_ALIVE);
    }

    public boolean isDying() {
        return (state == EnemyState.STATE_DYING);
    }

    public boolean isDead() {
        return (state == EnemyState.STATE_DEAD);
    }

    public boolean hasEscaped() {
        return (state == EnemyState.STATE_ESCAPED);
    }

    public boolean isHit() {
        return isHit;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getEscapeCost() {
        return escapeCost;
    }

    public int getYield() {
        return yield;
    }

    public int getHealthPercent() {
        return (int) ((float) health * 100f / (float) MAX_HEALTH);
    }

    public HealthBar getHealthBar() {
        bar.setCurrentHP(health);
        bar.setPos(x, y);
        return bar;
    }

    public Rectangle getHitBox() {
        final int boxSize = 20;
        return new Rectangle(x + boxSize / 2, y + boxSize / 2, boxSize, boxSize);
    }

    public void hit(int damage) {
        if (state != EnemyState.STATE_ALIVE) return;

        health -= damage;
        isHit = true;

        if (health <= 0) {
            state = EnemyState.STATE_DYING;
        }
    }

    public void update(long elapsed) {
        switch (state) {
            case STATE_ESCAPED:
            case STATE_DEAD:
                return;
            case STATE_DYING:
                // TODO Add some kind of death sequence
                dieStartTime += elapsed;
                if (dieStartTime >= deathDuration) {
                    state = EnemyState.STATE_DEAD;
                }
                break;

            default:
                isHit = false;
                int moveX = 0;
                int moveY = 0;

                Point targetPos = new Point(waypoints.elementAt(nextWaypoint).x,
                        waypoints.elementAt(nextWaypoint).y);
                Point thisPos = new Point((int) this.getCenterX(),
                        (int) this.getCenterY());

                double distanceToTarget = (double) thisPos.distance(targetPos);
                double distanceThisFrame = ((double) elapsed / 1000f) * moveSpeed;
                if (distanceThisFrame > distanceToTarget)
                    distanceThisFrame = distanceToTarget;

                if (distanceThisFrame == 0) {
                    posX = targetPos.x;
                    posY = targetPos.y;
                } else {
                    double dx = targetPos.x - thisPos.x;
                    double dy = targetPos.y - thisPos.y;

                    // Update the direction
                    dirX = dx / distanceToTarget;
                    dirY = dy / distanceToTarget;

                    // Calculate the distance to move
                    double actualMoveX = dirX * distanceThisFrame;
                    double actualMoveY = dirY * distanceThisFrame;

                    moveX = (int) Math.floor(actualMoveX + xOverflow);
                    moveY = (int) Math.floor(actualMoveY + yOverflow);

                    // When the distance to move this frame includes a fraction of
                    // one
                    // pixel, we can't represent that on screen so we accrue the
                    // move distance 'overflow' and move a full pixel when we've
                    // accrued enough.
                    xOverflow += actualMoveX - (double) moveX;
                    yOverflow += actualMoveY - (double) moveY;

                    posX += actualMoveX;
                    posY += actualMoveY;
                }

                // Set the new rectangle position
                this.x = (int) posX;
                this.y = (int) posY;

                final Point currentPos = new Point((int)getCenterX(), (int)getCenterY());
                if (waypoints.elementAt(nextWaypoint).equals(currentPos)) {
                    if (nextWaypoint == waypoints.size() - 1) {
                        state = EnemyState.STATE_ESCAPED;
                    } else {
                        nextWaypoint++;
                        nextWaypoint %= waypoints.size();
                    }
                }
        }
    }

    @Override
    public void select() {
        isSelected = true;
    }

    @Override
    public void deselect() {
        isSelected = false;
    }

    public boolean wasClicked(int clickX, int clickY) {
        return ((clickX >= x) && (clickX <= x + width) && (clickY >= y) && (clickY <= y
                + height));
    }

    public void draw(Graphics2D g, ImageStore imgs) {
        BufferedImage img;
        if (state == EnemyState.STATE_DYING) {
            img = (BufferedImage) imgs.getImage(textureDying);
        } else if (state == EnemyState.STATE_ALIVE) {
            img = (BufferedImage) imgs.getImage(textureAlive);
        } else
            return;

        g.drawImage(img, (int) posX, (int) posY, null);
    }

}

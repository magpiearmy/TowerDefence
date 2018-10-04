package core;

import org.w3c.dom.css.Rect;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial") public class Enemy extends Entity implements ISelectable {
  public static final int WIDTH = 48;
  public static final int HEIGHT = 48;
  private static final int MAX_HEALTH = 800;
  private static final int DEATH_DURATION = 250; //millis


  public enum EnemyState {
    STATE_ALIVE, STATE_DYING, STATE_DEAD, STATE_ESCAPED
  }


  private EnemyState state;

  private MovementComponent movement;

  private String textureAlive;
  private String textureDying;
  private boolean isSelected = false;

  private HealthBar healthBar;
  private int health = MAX_HEALTH;
  private int escapeCost;
  private int yield;
  private long deathSequenceTime = 0;

  private Vector<Point> waypoints;
  private int nextWaypoint;

  public Enemy(Point startPos, Vector<Point> waypoints, String textureAlive, String textureDying,
    int escapeCost) {
    super(startPos, WIDTH, HEIGHT);

    movement = new MovementComponent(20);

    state = EnemyState.STATE_ALIVE;

    this.waypoints = waypoints;
    nextWaypoint = 0;

    this.textureAlive = textureAlive;
    this.textureDying = textureDying;
    this.escapeCost = escapeCost;
    yield = 30;

    healthBar = new HealthBar(startPos.x, startPos.y - 4, width, MAX_HEALTH);
  }

  public boolean isAlive() {
    return state == EnemyState.STATE_ALIVE;
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

  public boolean isSelected() {
    return isSelected;
  }

  public int getEscapeCost() {
    return escapeCost;
  }

  public int getYield() {
    return yield;
  }

  public HealthBar getHealthBar() {
    healthBar.setCurrentHP(health);
    healthBar.setPosition(getBoundingRect().getLocation());
    return healthBar;
  }

  public Rectangle getHitBox() {
    final int boxSize = 12;
    return new Rectangle(position.x - boxSize / 2, position.y - boxSize / 2, boxSize, boxSize);
  }

  public void hit(int damage) {
    if (state != EnemyState.STATE_ALIVE)
      return;

    health = Math.max(0, health - damage);

    if (health == 0) {
      state = EnemyState.STATE_DYING;
    }
  }

  public void update(long elapsed) {
    switch (state) {
      case STATE_DYING: {
        // TODO Add some kind of death sequence
        deathSequenceTime += elapsed;
        if (deathSequenceTime >= DEATH_DURATION) {
          state = EnemyState.STATE_DEAD;
        }
        break;
      }
      case STATE_ALIVE: {
        movement.moveTowardTarget(position, getTargetPos(), elapsed);

        if (hasReachedNextWaypoint()) {
          if (isNextWaypointTheLast()) {
            state = EnemyState.STATE_ESCAPED;
          } else {
            nextWaypoint++;
          }
        }

        break;
      }
    }
  }

  private boolean isNextWaypointTheLast() {
    return nextWaypoint == waypoints.size() - 1;
  }

  private boolean hasReachedNextWaypoint() {
    return waypoints.elementAt(nextWaypoint).equals(position);
  }

  private Point getTargetPos() {
    return new Point(waypoints.elementAt(nextWaypoint));
  }

  @Override public boolean wasClicked(int clickX, int clickY) {
    return getBoundingRect().contains(clickX, clickY);
  }

  public void draw(Graphics2D g) {
    if (state == EnemyState.STATE_ESCAPED || state == EnemyState.STATE_DEAD)
      return;

    Image image = getImageForCurrentState();

    Rectangle bounds = getBoundingRect();
    g.drawImage(image, bounds.x, bounds.y, null);

    Rectangle hitBox = getHitBox();
    g.setColor(Color.red);
    g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
  }

  private Image getImageForCurrentState() {
    ImageStore images = ImageStore.getInstance();
    switch (state) {
      case STATE_DYING:
        return images.getImage(textureDying);
      case STATE_ALIVE:
        return images.getImage(textureAlive);
      default:
        return null;
    }
  }

  @Override public void select() {
    isSelected = true;
  }

  @Override public void deselect() {
    isSelected = false;
  }
}

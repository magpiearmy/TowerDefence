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
    private float _posX;
    private float _posY;

    private double _dirX = 0;
    private double _dirY = 0;
    private double _xOverflow = 0;
    private double _yOverflow = 0;

    // Texture data
    private String _textureAlive;
    private String _textureDying;

    private int _health = MAX_HEALTH;
    private EnemyState _state;
    private int _escapeCost;
    private int _yield;
    private boolean _isHit = false;
    private boolean _isSelected = false;

    private final long _deathDuration = 175;            // milliseconds
    private long _dieStartTime = 0;
    private float _moveSpeed = 0;            // pixels per
    // second
    private HealthBar _bar;

    private Vector<Point> _waypoints;
    private int _nextWaypoint;

    public Enemy(Point center, Vector<Point> waypoints, String textureAlive, String textureDying, int escapeCost) {
        _posX = center.x - SIZE / 2;
        _posY = center.y - SIZE / 2;
        setBounds(x, y, SIZE, SIZE);

        _state = EnemyState.STATE_ALIVE;
        _moveSpeed = 30;

        _waypoints = waypoints;
        _nextWaypoint = 0;

        _textureAlive = textureAlive;
        _textureDying = textureDying;
        _escapeCost = escapeCost;
        _yield = 30;

        _bar = new HealthBar((int) _posX, (int) _posY - 4, width);
        _bar.setMaxHP(MAX_HEALTH);
    }

    public Enemy(Enemy other) {
        _escapeCost = other._escapeCost;
    }

    public void init() {
    }

    public boolean isAlive() {
        return (_state == EnemyState.STATE_ALIVE);
    }

    public boolean isDying() {
        return (_state == EnemyState.STATE_DYING);
    }

    public boolean isDead() {
        return (_state == EnemyState.STATE_DEAD);
    }

    public boolean hasEscaped() {
        return (_state == EnemyState.STATE_ESCAPED);
    }

    public boolean isHit() {
        return _isHit;
    }

    public boolean isSelected() {
        return _isSelected;
    }

    public int getEscapeCost() {
        return _escapeCost;
    }

    public int getYield() {
        return _yield;
    }

    public int getHealthPercent() {
        return (int) ((float) _health * 100f / (float) MAX_HEALTH);
    }

    public HealthBar getHealthBar() {
        _bar.setCurrentHP(_health);
        _bar.setPos(x, y);
        return _bar;
    }

    public Rectangle getHitBox() {
        final int boxSize = 20;
        return new Rectangle(x + boxSize / 2, y + boxSize / 2, boxSize, boxSize);
    }

    public void hit(int damage) {
        if (_state != EnemyState.STATE_ALIVE) return;

        _health -= damage;
        _isHit = true;

        if (_health <= 0) {
            _state = EnemyState.STATE_DYING;
        }
    }

    public void update(long elapsed) {
        switch (_state) {
            case STATE_ESCAPED:
            case STATE_DEAD:
                return; // Just return if this enemy is dead or has escaped the map
            case STATE_DYING:
                // TODO Add some kind of death sequence
                _dieStartTime += elapsed;
                if (_dieStartTime >= _deathDuration) {
                    _state = EnemyState.STATE_DEAD;
                }
                break;

            default:

                _isHit = false; // Reset 'hit' flag
                int moveX = 0;
                int moveY = 0;

                Point targetPos = new Point(_waypoints.elementAt(_nextWaypoint).x,
                        _waypoints.elementAt(_nextWaypoint).y);
                Point thisPos = new Point((int) this.getCenterX(),
                        (int) this.getCenterY());

                double distanceToTarget = (double) thisPos.distance(targetPos);
                double distanceThisFrame = ((double) elapsed / 1000f) * _moveSpeed;
                if (distanceThisFrame > distanceToTarget)
                    distanceThisFrame = distanceToTarget;

                if (distanceThisFrame == 0) {
                    _posX = targetPos.x;
                    _posY = targetPos.y;
                } else {
                    double dx = targetPos.x - thisPos.x;
                    double dy = targetPos.y - thisPos.y;

                    // Update the direction
                    _dirX = dx / distanceToTarget;
                    _dirY = dy / distanceToTarget;

                    // Calculate the distance to move
                    double actualMoveX = _dirX * distanceThisFrame;
                    double actualMoveY = _dirY * distanceThisFrame;

                    moveX = (int) Math.floor(actualMoveX + _xOverflow);
                    moveY = (int) Math.floor(actualMoveY + _yOverflow);

                    // When the distance to move this frame includes a fraction of
                    // one
                    // pixel, we can't represent that on screen so we accrue the
                    // move distance 'overflow' and move a full pixel when we've
                    // accrued enough.
                    _xOverflow += actualMoveX - (double) moveX;
                    _yOverflow += actualMoveY - (double) moveY;

                    _posX += actualMoveX;
                    _posY += actualMoveY;
                }

                // Set the new rectangle position
                this.x = (int) _posX;
                this.y = (int) _posY;

                if (_waypoints.elementAt(_nextWaypoint).equals(
                        new Vector2D((int) this.getCenterX(), (int) this
                                .getCenterY()))) {
                    if (_nextWaypoint == _waypoints.size() - 1) {
                        _state = EnemyState.STATE_ESCAPED;
                    } else {
                        _nextWaypoint++;
                        _nextWaypoint %= _waypoints.size();
                    }
                }
        }
    }

    @Override
    public void select() {
        _isSelected = true;
    }

    @Override
    public void deselect() {
        _isSelected = false;
    }

    public boolean wasClicked(int clickX, int clickY) {
        return ((clickX >= x) && (clickX <= x + width) && (clickY >= y) && (clickY <= y
                + height));
    }

    public void draw(Graphics2D g, ImageStore imgs) {
        BufferedImage img;
        if (_state == EnemyState.STATE_DYING) {
            img = (BufferedImage) imgs.getImage(_textureDying);
        } else if (_state == EnemyState.STATE_ALIVE) {
            img = (BufferedImage) imgs.getImage(_textureAlive);
        } else
            return;

        g.drawImage(img, (int) _posX, (int) _posY, null);
    }

}

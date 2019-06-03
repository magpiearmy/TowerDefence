package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class HealthBar extends Rectangle {

  private static final int HEIGHT = 3;
  private int maxHP;
  private int currentHP;

  public HealthBar(int x, int y, int width, int maxHP) {
    super(x, y, width, HEIGHT);
    this.maxHP = maxHP;
  }

  public void setCurrentHP(int currentHP) {
    this.currentHP = currentHP;
  }

  public void setPosition(Point pos) {
    setBounds(pos.x, pos.y, width, height);
  }

  public int getHealthPercent() {
    return (int) ((float) currentHP * 100f / (float) maxHP);
  }

  public void draw(Graphics2D g) {

    float healthCoefficient = (float) currentHP / (float) maxHP;
    int healthBarSize = (int) (this.width * healthCoefficient);

    if (getHealthPercent() < 30)
      g.setColor(new Color(210, 30, 30));
    else
      g.setColor(new Color(20, 255, 20));

    g.fillRect(x, y, healthBarSize, height);
    g.setColor(new Color(100, 100, 100));
    g.drawRect(x, y, width, height);
  }
}

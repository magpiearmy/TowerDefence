package core;

import java.awt.*;

@SuppressWarnings("serial") public class HealthBar extends Rectangle {

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

  public void draw(Graphics2D gfx) {

    float healthCoefficient = (float) currentHP / (float) maxHP;
    int healthBarSize = (int) (this.width * healthCoefficient);

    if (getHealthPercent() < 30)
      gfx.setColor(new Color(210, 30, 30));
    else
      gfx.setColor(new Color(20, 255, 20));

    gfx.fillRect(this.x, this.y, healthBarSize, this.height);
    gfx.setColor(new Color(100, 100, 100));
    gfx.drawRect(this.x, this.y, this.width, this.height);
  }
}

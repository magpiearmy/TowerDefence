package core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class HealthBar extends Rectangle {
	
	private static final int HEIGHT = 3;
	private int _maxHP;
	private int _currentHP;
	
	public HealthBar(int x, int y, int width) {
		super(x, y, width, HEIGHT);
	}
	
	public void setMaxHP(int maxHP) {
		_maxHP = maxHP;
	}

	public void setCurrentHP(int currentHP) {
		_currentHP = currentHP;
	}
	
	public void setPos(int x, int y) {
		setBounds(x, y, width, height);
	}

	public int getHealthPercent()
	{
		return (int) ((float) _currentHP * 100f / (float) _maxHP);
	}
	
	public void draw(Graphics2D gfx) {

		float healthCoefficient = (float) _currentHP / (float) _maxHP;
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

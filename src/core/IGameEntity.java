package core;

import java.awt.Graphics2D;

public interface IGameEntity {

	public void init();
	public void update(long elapsed);
	public void draw(Graphics2D gfx);
}

package core;

import java.awt.Graphics2D;

public interface GameEntity {

  void update(long elapsed);

  void draw(Graphics2D gfx);

}

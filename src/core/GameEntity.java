package core;

import java.awt.*;

public interface GameEntity {

  void update(long elapsed);

  void draw(Graphics2D gfx);

}

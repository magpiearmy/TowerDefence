package core;

import java.awt.*;

public interface GameEntity {

  public void update(long elapsed);

  public void draw(Graphics2D gfx);

}

package towers;

import core.*;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial") public class Tower extends Tile implements ISelectable {

  protected ElementProperties elements = new ElementProperties();
  protected Circle fireRadius;
  protected int cost = 0;

  protected boolean isSelected = false;

  public Tower(int x, int y, int range) {
    this.setBounds(x, y, Tile.WIDTH, Tile.HEIGHT);
    fireRadius = new Circle(x + Tile.WIDTH / 2, y + Tile.HEIGHT / 2, range);
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public int getPosX() {
    return x;
  }

  public int getPosY() {
    return y;
  }

  public Circle getFireRadius() {
    return fireRadius;
  }

  public int getCost() {
    return cost;
  }

  public void update(long elapsed) {
  }

  public void addElement(BasicElementType type) {
    elements.addElement(type);
  }

  public void draw(Graphics2D gfx) {
    ImageStore images = ImageStore.getInstance();
    gfx.drawImage(images.getImage(textureId), x, y, null);
  }

  public boolean fire(Vector<Enemy> enemies) {
    return false;
  }

  @Override public void select() {
    isSelected = true;
  }

  @Override public void deselect() {
    isSelected = false;
  }

  @Override public boolean wasClicked(int clickX, int clickY) {
    return this.contains(clickX, clickY);
  }
}
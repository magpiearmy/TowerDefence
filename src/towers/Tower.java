package towers;

import core.*;

import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class Tower extends Entity /*Tile*/ implements ISelectable {

  protected ElementProperties elements = new ElementProperties();
  protected Circle fireRadius;
  protected int cost = 0;
  protected String imageId;

  protected boolean isSelected = false;

  public Tower(Point pos, int range) {
    super(pos, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

    fireRadius = new Circle(getCentre(), range);
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public void setCost(int cost) {
    this.cost = cost;
  }

  public boolean isSelected() {
    return isSelected;
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
    Image image = ImageStore.getInstance().getImage(imageId);
    gfx.drawImage(image, position.x, position.y, null);
  }

  public boolean fire(Vector<Enemy> enemies) {
    return false;
  }

  @Override
  public void select() {
    isSelected = true;
  }

  @Override
  public void deselect() {
    isSelected = false;
  }

  @Override
  public boolean wasClicked(int clickX, int clickY) {
    return getBoundingRect().contains(clickX, clickY);
  }
}
package towers;

import java.awt.Graphics2D;
import java.util.Vector;

import core.Circle;
import core.Enemy;
import core.ISelectable;
import core.Tile;

@SuppressWarnings("serial")
public class Tower extends Tile implements ISelectable {

	protected ElementProperties _elements = new ElementProperties();
	protected Circle		_fireRadius;
	protected int			_cost		= 0;

	protected boolean		_isSelected	= false;

	public Tower(int x, int y, int range) {
		this.setBounds(x, y, Tile.WIDTH, Tile.HEIGHT);
		_fireRadius = new Circle(x + Tile.WIDTH / 2, y + Tile.HEIGHT / 2, range);
	}
	
	public void setCost(int cost) {
		_cost = cost;
	}
	
	public boolean isSelected() {
		return _isSelected;
	}

	public int getPosX() {
		return x;
	}

	public int getPosY() {
		return y;
	}

	public Circle getFireRadius() {
		return _fireRadius;
	}

	public int getCost() {
		return _cost;
	}

	public void update(long elapsed) {
	}
	
	public void addElement(BasicElementType type) {
		_elements.addElement(type);
	}
	
	public void draw(Graphics2D gfx) {
		gfx.drawImage(_texture, x, y, null);
	}

	public boolean fire(Vector<Enemy> enemies) {
		return false;
	}

	@Override
	public void select() {
		_isSelected = true;
	}

	@Override
	public void deselect() {
		_isSelected = false;
	}

	@Override
	public boolean wasClicked(int clickX, int clickY) {
		return this.contains(clickX, clickY);
	}
}
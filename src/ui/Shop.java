package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import core.ImageStore;
import core.Level;
import core.Tile;

public class Shop {

	public static final int BUTTON_MARGIN = 5;
	public static final int BUTTON_SIZE = Tile.WIDTH + (BUTTON_MARGIN * 2);
	
	private int _screenWidth;
	private Vector<Rectangle> _buttons;		// The bounds of all buttons
	private int _highlightedButton = -1;	// Array index of highlighted button
	private int _heldItemIndex = -1;		// Array index of the currently held item
	private Rectangle _startButton;
	private Level _level;

	private ImageStore 		_imgs;
	private Vector<Integer> _items;		// Shop objects
	private Vector<String> 	_images;	// Shop object images
	
	enum ShopState { idle, holdingItem, placedItem }
	private ShopState state = ShopState.idle;
	
	
	public Shop(int screenWidth, Level level)
	{
		_screenWidth = screenWidth;
		_buttons = new Vector<Rectangle>();
		_items = new Vector<Integer>();
		_images = new Vector<String>();		
		_startButton = new Rectangle(24, 510, BUTTON_SIZE, BUTTON_SIZE);
		_level = level;
	}
	
	public void addItem(String imgId)
	{
		_items.add(_items.size() + 1);
		_images.add(imgId);
	}
	
	public void setImageStore(ImageStore imgs)
	{
		_imgs = imgs;
	}
	
	public void construct()
	{
		final int buttonSpacing = 10;
		final int sizePlusSpacing = BUTTON_SIZE + buttonSpacing;
		int xOffset = ( _screenWidth - (_items.size() * sizePlusSpacing) ) / 2;
		
		for( int i = 0; i < _items.size(); i++)
		{
			_buttons.add(new Rectangle(i*sizePlusSpacing+xOffset, 510, BUTTON_SIZE, BUTTON_SIZE));
		}
		
		String startId = _imgs.loadImage("start.png");
		_images.add(startId);
		_buttons.add(_startButton);
	}
	
	public Image getImageOfHeldItem()
	{
		if (_heldItemIndex == -1) return null;
		else
		{
			return _imgs.getImage(_images.elementAt(_heldItemIndex));
		}
	}
	
	public int getHeldItem()
	{
		if (state == ShopState.holdingItem)
		{
			return _items.elementAt(_heldItemIndex);
		}
		else
			return -1;
	}
	
	public void clearHeldItem()
	{
		state = ShopState.idle;
		_heldItemIndex = -1;
	}
	
	public void handleMouseClick(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		Point clickPt = new Point(mx, my);
		
		switch(state)
		{
		case holdingItem:
			
			boolean clearItem = true;
			
			// Check if the user clicked a shop item
			for (int i = 0; i < _buttons.size(); i++)
			{
				if (_buttons.elementAt(i).contains(clickPt))
				{
					_heldItemIndex = i;
					clearItem = false;
					break;
				}
			}
			
			if (clearItem) {
				clearHeldItem();
			}
			break;
			
			
		case idle:
			
			for( int i = 0; i < _buttons.size(); i++)
			{
				Rectangle thisButton = _buttons.elementAt(i);
				if( thisButton.contains(clickPt) )
				{
					if (thisButton == _startButton)
					{
						if (!_level.isStarted())
							_level.start();
						return;
					}
					else
					{
						state = ShopState.holdingItem;
						_heldItemIndex = i;
						return;
					}
				}
			}
			break;
		
		default:
			break;
		}
	}
	
	public void handleMouseMove(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		for( int i = 0; i < _buttons.size(); i++)
		{
			if( _buttons.elementAt(i).contains( new Point(mx, my) ) )
			{
				_highlightedButton = i;
				return;
			}
		}
		_highlightedButton = -1;
	}

	public void selectItem(int idx) {
		state = ShopState.holdingItem;
		_heldItemIndex = idx;
	}
	
	public void draw(Graphics2D g)
	{		
		for( int i = 0; i < _buttons.size(); i++)
		{
			Rectangle thisButton = _buttons.elementAt(i);
			Image img = _imgs.getImage(_images.elementAt(i));
			
			if (thisButton == _startButton && _level.isStarted())
				g.setColor( new Color(50, 50, 50) );
			else if (i == _highlightedButton)
				g.setColor( new Color(130, 130, 140) );
			else
				g.setColor( new Color(100, 100, 100) );

			g.fillRoundRect(thisButton.x,
						   thisButton.y,
						   thisButton.width,
						   thisButton.height,
						   6,
						   6);
			g.drawImage(img,
						thisButton.x + BUTTON_MARGIN,
						thisButton.y + BUTTON_MARGIN,
						null);

			g.setColor( new Color(200, 200, 200) );
			
			g.drawRoundRect(thisButton.x,
						   thisButton.y,
						   thisButton.width,
						   thisButton.height,
						   6,
						   6);
		}
	}
	
}

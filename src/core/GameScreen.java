package core;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ui.Shop;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Runnable {

	Thread _thread = new Thread(this);

	private Level 	_level;
	private int 	_levelDrawOffsetX = 0;
	private int 	_levelDrawOffsetY = 0;
	private Shop shop;
	private int		_mouseX;
	private int 	_mouseY;
	
	public GameScreen()
	{
		_level = new Level(this);
		shop = new Shop(900, _level);
		setBackground( new Color(20,60,90) );
		setFocusable(true);
	}
	
	public void init()
	{
		_level.init(shop);
		
		addMouseListener( new MouseListener() {
			public void mousePressed(MouseEvent e)
			{
				// Check if the point lies within the main level window
				if (getLevelBounds().contains(e.getPoint())) {
					
					// Convert the mouse event into level coordinates
					e.translatePoint(-_levelDrawOffsetX, -_levelDrawOffsetY);
					
					// Notify the level of the click
					_level.handleMouseEvent(e);
					e.consume();
				
				} else {
					// Click was outside the level so let the shop handle it
					shop.handleMouseClick(e);
				}
			}
			public void mouseClicked(MouseEvent e) {}			
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		addMouseMotionListener( new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {}
			public void mouseMoved(MouseEvent e)
			{
				_mouseX = e.getX();
				_mouseY = e.getY();
				shop.handleMouseMove(e);
			}			
		});
		
		getInputMap().put(KeyStroke.getKeyStroke("1"), "pressedNumber");
		getInputMap().put(KeyStroke.getKeyStroke('2'), "pressedNumber");
		getInputMap().put(KeyStroke.getKeyStroke('3'), "pressedNumber");
		getActionMap().put("pressedNumber", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				shop.selectItemByIndex(Integer.parseInt(arg0.getActionCommand()) - 1);
			};
		
		});
		
		_thread.start();
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	public Rectangle getLevelBounds() {
		return new Rectangle(_levelDrawOffsetX, _levelDrawOffsetY,
				_level.getWidthInPixels(), _level.getHeightInPixels());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int levelWidth = _level.getWidthInPixels();
		int levelHeight = _level.getHeightInPixels();
		_levelDrawOffsetX = (getWidth() - levelWidth)/2;	// x-Offset to centre the level in the panel
		_levelDrawOffsetY = 5;								// y-Offset
		
		// This buffered image is what the level will draw onto
		BufferedImage mapCanvas = new BufferedImage(levelWidth, levelHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D mapGfx = (Graphics2D)mapCanvas.getGraphics();
		
		BufferedImage infoboxCanvas = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
		Graphics2D infoboxGfx = (Graphics2D)infoboxCanvas.getGraphics();
		
		// Let level to the image canvas
		_level.draw(mapGfx, infoboxGfx);
		
		 // Add a small border
		mapGfx.setColor(Color.white);
		mapGfx.drawRect(0, 0, levelWidth-1, levelHeight-1);
		
		// Draw the map and infobox
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(mapCanvas, null, _levelDrawOffsetX, _levelDrawOffsetY);
		g2d.drawImage(infoboxCanvas, null, _levelDrawOffsetX + levelWidth - infoboxCanvas.getWidth(), 520);
		
		// Draw the shop
		shop.draw(g2d);
		shop.getHeldItem().ifPresent(towerType -> {
			Image img = shop.getImageOfHeldItem();
			int drawX = _mouseX - img.getWidth(null) / 2;
			int drawY = _mouseY - img.getHeight(null) / 2;
			g2d.drawImage(img, drawX, drawY, null);
		});
	}

	@Override
	public void run() {
		
		boolean isRunning = true;
		long currentTime;
		long previousTime = System.nanoTime();
		while(isRunning)
		{
			currentTime = System.nanoTime();								// nano-precision
			long elapsed = (currentTime - previousTime) / (long)1000000;	// milli-precision
			
			if (elapsed - 10 > 0) // Update every 10ms
			{
				_level.update(elapsed);
				repaint();
				previousTime = System.nanoTime();
			}
			
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}

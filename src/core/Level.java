package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import profiles.ProfileParser;
import towers.Tower;
import towers.TowerFactory;
import ui.Shop;

public class Level
{
	// Level data
	private Map					_map;
	private int					_towerMap[][];
	private Tile				_tiles[][];
	private Tile				_startTile;
	private Vector<Vector2D>	_waypoints;
	private MapLoader			_mapLoader;
	private Shop				_shop;
	private final int			_tileSize;
	private int					_widthInTiles;
	private int					_heightInTiles;

	private boolean				_started		= false;

	// Resource data
	private String				_grassImg;
	private String				_pathImg;
	private String				_tower1Img;
	private String				_tower2Img;
	private String				_tower3Img;
	private ImageStore			_imgs = ImageStore.get();

	private BulletManager		_bulletManager;
	private TowerFactory		_towerFactory;
	private EnemyFactory		_enemyFactory;
	private Vector<Tower>		_towers			= new Vector<Tower>();
	private Vector<Enemy>		_enemies		= new Vector<Enemy>();
	private Vector<ISelectable>	_clickables		= new Vector<ISelectable>();

	private Spawner				_spawner;

	// Level-specific player data
	private int					_livesRemaining;
	private int					_money;

	public Level(JPanel screen)
	{
		_tileSize = Tile.WIDTH;
		_mapLoader = new MapLoader("Level_0.txt");
	}

	// Init
	public void init(Shop shop)
	{
		_shop = shop;

		// Build the Map from a map file
		loadMap();

		// Now we can allocate and load the tile array
		_tiles = new Tile[_widthInTiles][_heightInTiles];
		TileFactory tileFactory = new TileFactory(_imgs);
		for (int y = 0; y < _heightInTiles; y++)
		{
			for (int x = 0; x < _widthInTiles; x++)
			{
				_tiles[x][y] = tileFactory.createTile(x, y, _map.getTile(x ,y));
			}
		}
		
		// Default the tower map to zeros
		_towerMap = new int[_widthInTiles][_heightInTiles];
		for (int y = 0; y < _heightInTiles; y++)
		{
			for (int x = 0; x < _widthInTiles; x++)
			{
				_towerMap[x][y] = 0;
			}
		}

		// TODO This code should be part of a separate resource manager
		// Load the necessary textures into memory
		_grassImg = _imgs.loadImage("grass.png");
		_pathImg = _imgs.loadImage("path.png");
		_tower1Img = _imgs.loadImage("tower1.png");
		_tower2Img = _imgs.loadImage("tower2.png");
		_tower3Img = _imgs.loadImage("tower3.png");

		// Set up the shop images
		shop.setImageStore(_imgs);
		shop.addItem(_tower1Img);
		shop.addItem(_tower2Img);
		shop.addItem(_tower3Img);
		shop.construct();

		// Create the bullet manager and tower factory
		_bulletManager = new BulletManager(getWidthInPixels(), getHeightInPixels());
		_bulletManager.init();
		_towerFactory = new TowerFactory(_bulletManager);

		// Get the start positon from the map and get the corresponding tile
		Vector2D startPos = _map.getStart();
		_startTile = _tiles[startPos.x][startPos.y];

		// Create enemy factory
		EnemyFactory enemyFactory = new EnemyFactory(_startTile.getCenter(), _map.getWaypoints(), _imgs);

		ProfileParser profParser = new ProfileParser(enemyFactory);
		_spawner = new Spawner(profParser.parse("profile1.xml"));

		_livesRemaining = 5;
		_money = 300;
	}

	private void loadMap()
	{
		if (_mapLoader != null)
		{
			try {
				_map = _mapLoader.loadMap();
			}
			catch (Exception e) { //TODO: MapLoadException
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		_widthInTiles = _map.getWidth();
		_heightInTiles = _map.getHeight();
	}

	public int getWidthInPixels()
	{
		return _widthInTiles * _tileSize;
	}

	public int getHeightInPixels()
	{
		return _heightInTiles * _tileSize;
	}

	public void start()
	{
		_started = true;
	}

	public boolean isStarted()
	{
		return _started;
	}

	public void notifyMouse(MouseEvent e)
	{
		final int mx = e.getX();
		final int my = e.getY();
		
		int placedItem = _shop.getHeldItem();
		if (placedItem != -1)
		{
			placeTower(mx, my, placedItem);
		}
		else
		{
			for (ISelectable clickable : _clickables)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					if (clickable.wasClicked(mx, my))
					{
						clickable.select();
					}
					else
						clickable.deselect();
				}
			}
		}
	}

	public void placeTower(int clickX, int clickY, int type)
	{
		int towerX = (clickX - clickX % Tile.WIDTH);
		int towerY = (clickY - clickY % Tile.HEIGHT);
		int mapX = towerX / Tile.WIDTH;
		int mapY = towerY / Tile.HEIGHT;

		// Make sure there is no tower on this tile already
		if (_towerMap[mapX][mapY] == 0 && _map.getTile(mapX, mapY) != 1)
		{
			// Stop showing the item being carried
			_shop.clearHeldItem();

			Tower newTower = _towerFactory.createTower(towerX, towerY, type);
			int cost = newTower.getCost(); // TODO store cost with a tower type instead of tower instance
			if (_money >= cost)
			{
				_towerMap[mapX][mapY] = 1; // Mark this tower in the map
				_towers.add(newTower);
				_clickables.add(newTower);
				_money -= cost;
			}
			else
			{
				// TODO Notify that user cannot afford tower
				System.out.println(new StringBuilder("Cannot afford that tower! Cost [" + cost
						+ "] Funds [" + _money + "]").toString());
			}
		}
	}

	/**
	 * Update
	 * 
	 * @param elapsed
	 */
	public void update(long elapsed)
	{
		if (!_started)
			return;

		/* First, do some end-game checks */
		if (_livesRemaining <= 0)
		{
			// TODO Player lose
		}
		else if (_enemies.size() == 0)
		{
			// TODO Player win
		}

		_spawner.update(elapsed);

		if (_spawner.hasSpawned())
		{
			_enemies.addAll(_spawner.getSpawnedEnemies());
		}

		for (int i = 0; i < _enemies.size(); i++)
		{
			_enemies.elementAt(i).update(elapsed);
		}

		// For each tower, check whether an enemy is in range
		// and tell it to fire if necessary.
		for (int i = 0; i < _towers.size(); i++)
		{
			Tower thisTower = _towers.elementAt(i);

			thisTower.update(elapsed); // Update the tower
			thisTower.fire(_enemies); // Allow towers to fire at a target
		}
		_bulletManager.updateBullets(elapsed);

		// Clean up any dead enemies
		for (int i = 0; i < _enemies.size(); i++)
		{
			Enemy thisEnemy = _enemies.elementAt(i);
			if (thisEnemy.isDead())
			{
				_enemies.remove(thisEnemy);
				_bulletManager.onTargetKilled(thisEnemy);
				_money += thisEnemy.getYield();
			}
			else if (thisEnemy.hasEscaped())
			{
				_enemies.remove(thisEnemy);
				_livesRemaining -= thisEnemy.getEscapeCost();
			}
		}
	}

	/**
	 * Draw
	 * 
	 * @param mapGfx
	 * @param infoboxGfx
	 */
	public void draw(Graphics2D mapGfx, Graphics2D infoboxGfx)
	{
		mapGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		infoboxGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		infoboxGfx.setFont(new Font("Consolas", Font.PLAIN, 12));
		infoboxGfx.drawString(new StringBuilder("Money: " + _money).toString(), 5, 15);
		infoboxGfx.drawString(new StringBuilder("Lives: " + _livesRemaining).toString(), 5, 32);

		// Tiles
		for (int y = 0; y < _heightInTiles; y++)
		{
			for (int x = 0; x < _widthInTiles; x++)
			{
				Tile thisTile = _tiles[x][y];

				mapGfx.drawImage(_imgs.getImage(thisTile._textureId), thisTile.x, thisTile.y, null);

				mapGfx.setColor(new Color(80, 120, 80));
				mapGfx.drawRect(thisTile.x, thisTile.y, thisTile.width, thisTile.height);

				// TODO: thisTile.draw();
			}
		}
		

		// Enemies
		List<HealthBar> healthBars = new ArrayList<HealthBar>();
		final int enemyCount = _enemies.size();
		for (int i = 0; i < enemyCount; i++)
		{
			Enemy thisEnemy = _enemies.elementAt(i);

			if (thisEnemy.isAlive())
			{
				mapGfx.setColor(new Color(69, 67, 120));
			}
			else if (thisEnemy.isDying())
			{
				mapGfx.setColor(new Color(80, 30, 30, 120));
			}
			thisEnemy.draw(mapGfx, _imgs);

			// Draw health bar
			if (thisEnemy.isAlive())
			{
				healthBars.add( thisEnemy.getHealthBar() );
			}
		}


		// Towers
		final int towerCount = _towers.size();
		for (int i = 0; i < towerCount; i++)
		{
			Tower thisTower = _towers.elementAt(i);

			thisTower.draw(mapGfx);

			if (thisTower.isSelected())
			{
				Circle range = thisTower.getFireRadius();
				mapGfx.setColor(new Color(200, 200, 200));
				mapGfx.drawOval(range.getX() - range.getRadius(), range.getY() - range.getRadius(),
						range.getRadius() * 2, range.getRadius() * 2);
			}
		}
		
		// Draw health bars
		for (HealthBar bar : healthBars) {
			bar.draw(mapGfx);
		}

		// Finally, draw the bullets!
		_bulletManager.drawBullets(mapGfx);
	}

}

package core;

import java.util.Vector;

public class Map {

	// Raw map data
	private int			_data[][];

	// Path and waypoints
	private Vector<Vector2D>	_path;
	private Vector<Vector2D>	_waypoints;
	
	private Vector2D	_start;
	private Vector2D	_end;
	private final int	_width;
	private final int	_height;

	public Map(int data[][], Vector2D start, Vector2D end) {
		_data = data;
		_width = _data[0].length;
		_height = _data.length;
		
		_start = start;
		_end = end;
	}

	public int getTile(int x, int y) {
		return _data[y][x];
	}

	public Vector2D getStart() {
		return _start;
	}

	public Vector2D getEnd() {
		return _end;
	}

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}
	
	public Vector<Vector2D> getWaypoints() {
		return _waypoints;
	}
	
	public void loadPath() {
		_path = new Vector<Vector2D>();
		_waypoints = new Vector<Vector2D>();

		// First, find the coords of all path tiles
		for (int y = 0; y < _height; y++)
		{
			for (int x = 0; x < _width; x++)
			{
				if (getTile(x, y) == 1)
					_path.add(new Vector2D(x, y));
			}
		}

		// Now set the waypoints which is any point
		// at which an enemy will change direction
		boolean pathComplete = false;
		boolean changedDirection = false;
		int dir = Direction.RIGHT;
		Vector2D currentTile = _start;
		while (!pathComplete)
		{
			// Get the next tile in the current direction
			Vector2D nextTile = getNextTilePos(currentTile, dir);
			
			boolean foundNext = false;
			int tilesWalked = 0;
			
			// Check if this next tile is part of the path
			while (!foundNext && tilesWalked < _path.size())
			{
				for (int i = 0; i < _path.size(); i++)
				{
					if (_path.elementAt(i).compareTo(nextTile))
					{
						foundNext = true;
						_path.remove(i);
						break;
					}
				}
				tilesWalked++;
			}
			
			if (!foundNext) // We didn't find a path tile in the current direction
			{
				// Check if we are already at the end
				if (currentTile.compareTo(_end))
				{
					addWaypoint(getNextTilePos(currentTile, dir));
					pathComplete = true;
				}
				else
				{
					// Change direction!
					dir = (dir % 4) + 1;
					changedDirection = true;
				}
			}
			else // Found the next path tile!
			{
				// If we had to change direction to find the next path tile, set a waypoint.
				if (changedDirection)
				{
					addWaypoint(new Vector2D(currentTile.x, currentTile.y));
					changedDirection = false;
				}
				
				
				// Move to next tile
				currentTile = nextTile;
			}
		}
	}

	private void addWaypoint(Vector2D point)
	{
		point.x = (point.x * Tile.WIDTH) + (Tile.WIDTH / 2);
		point.y = (point.y * Tile.HEIGHT) + (Tile.HEIGHT / 2);
		_waypoints.add(point);
	}
	
	/**
	 * Takes a position vector and a direction and returns the next position
	 * vector in that direction.
	 * 
	 * @param current
	 * @param dir
	 * @return
	 */
	private Vector2D getNextTilePos(Vector2D current, int dir)
	{
		switch (dir)
		{
		case Direction.UP:
			return new Vector2D(current.x, current.y - 1);
		case Direction.RIGHT:
			return new Vector2D(current.x + 1, current.y);
		case Direction.DOWN:
			return new Vector2D(current.x, current.y + 1);
		case Direction.LEFT:
			return new Vector2D(current.x - 1, current.y);
		default:
			return null; // shouldn't get here
		}
	}
}

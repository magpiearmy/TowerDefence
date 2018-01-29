package core;

import java.awt.*;
import java.util.Vector;

public class Map {

	// Raw map data
	private int data[][];

	private Vector<Point> path;
	private Vector<Point> waypoints;
	
	private Point start;
	private Point end;
	private final int width;
	private final int height;

	public 	Map(int data[][], Point start, Point end) {
		this.data = data;
		width = this.data[0].length;
		height = this.data.length;
		
		this.start = start;
		this.end = end;
	}

	public int getTile(int x, int y) {
		return data[y][x];
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Vector<Point> getWaypoints() {
		return waypoints;
	}
	
	public void loadPath() {
		path = new Vector<>();
		waypoints = new Vector<>();

		// First, find the coords of all path tiles
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (getTile(x, y) == 1)
					path.add(new Point(x, y));
			}
		}

		// Now set the waypoints which is any point
		// at which an enemy will change direction
		boolean pathComplete = false;
		boolean changedDirection = false;
		int dir = Direction.RIGHT;
		Point currentTile = start;
		while (!pathComplete)
		{
			// Get the next tile in the current direction
			Point nextTile = getNextTilePos(currentTile, dir);
			
			boolean foundNext = false;
			int tilesWalked = 0;
			
			// Check if this next tile is part of the path
			while (!foundNext && tilesWalked < path.size())
			{
				for (int i = 0; i < path.size(); i++)
				{
					if (path.elementAt(i).equals(nextTile))
					{
						foundNext = true;
						path.remove(i);
						break;
					}
				}
				tilesWalked++;
			}
			
			if (!foundNext) // We didn't find a path tile in the current direction
			{
				// Check if we are already at the end
				if (currentTile.equals(end))
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
					addWaypoint(new Point(currentTile.x, currentTile.y));
					changedDirection = false;
				}
				
				
				// Move to next tile
				currentTile = nextTile;
			}
		}
	}

	private void addWaypoint(Point point)
	{
		point.x = (point.x * Tile.WIDTH) + (Tile.WIDTH / 2);
		point.y = (point.y * Tile.HEIGHT) + (Tile.HEIGHT / 2);
		waypoints.add(point);
	}
	
	/**
	 * Takes a position vector and a direction and returns the next position
	 * vector in that direction.
	 * 
	 * @param current
	 * @param dir
	 * @return
	 */
	private Point getNextTilePos(Point current, int dir)
	{
		switch (dir)
		{
		case Direction.UP:
			return new Point(current.x, current.y - 1);
		case Direction.RIGHT:
			return new Point(current.x + 1, current.y);
		case Direction.DOWN:
			return new Point(current.x, current.y + 1);
		case Direction.LEFT:
			return new Point(current.x - 1, current.y);
		default:
			return null; // shouldn't get here
		}
	}
}

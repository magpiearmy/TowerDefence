package core;

import java.util.Vector;

public class EnemyFactory
{
	private Vector2D _startPos;
	private Vector<Vector2D> _waypoints;

	private String _enemyImg;
	private String _enemyDyingImg;
	
	public EnemyFactory(Vector2D startPos, Vector<Vector2D> waypoints, ImageStore imgStore) {
		_startPos = startPos;
		_waypoints = waypoints;
		_enemyImg = imgStore.loadImage("enemy1.png");
		_enemyDyingImg = imgStore.loadImage("enemy2.png");
	}
	
	public Enemy createEnemy(int enemyType) {
		
		Enemy enemy = new Enemy(_startPos, _waypoints, _enemyImg, _enemyDyingImg, 1);
		return enemy;
	}
}

package profiles;

import java.util.ArrayList;
import java.util.List;

import core.Enemy;
import core.EnemyFactory;

public class WaveCommand extends Command
{
	private enum State {
		DELAYING,
		SPAWNING
	};
	
	private State _state;
	private int _enemyCount;
	private int _interval;
	private int _delayTotal;
	
	private int _delayTime;
	private int _timeSinceSpawn;
	private int _spawnCount;
	private EnemyFactory _factory;
	
	public WaveCommand(int enemyCount, int delay, int interval, EnemyFactory factory)
	{
		type = CommandType.COMMAND_WAVE;
		_enemyCount = enemyCount;
		_delayTotal = delay;
		_interval = interval;
		_timeSinceSpawn = 0;
		_delayTime = 0;
		_spawnCount = 0;
		_state = State.DELAYING;
		_factory = factory;
	}
	
	public List<Enemy> update(long elapsed) {

		List<Enemy> spawnedEnemies = null;
		
		switch (_state) {
		case DELAYING:
			_delayTime += elapsed;
			if (_delayTime >= _delayTotal) {
				_state = State.SPAWNING;
				_timeSinceSpawn = _interval + (_delayTime - _delayTotal);
			}
			break;
		case SPAWNING:
			_timeSinceSpawn += elapsed;
			while (_timeSinceSpawn >= _interval) {
				if (spawnedEnemies == null)
					spawnedEnemies = new ArrayList<Enemy>();
				
				_timeSinceSpawn -= _interval;
				spawnedEnemies.add( spawn() );
			}
			
			if (_spawnCount == _enemyCount) {
				finish();
			}
			break;
		}
		
		return spawnedEnemies;
	}
	
	private Enemy spawn() {
		_spawnCount++;
		return _factory.createEnemy(0);
	}
	
	public int getEnemyCount() { return _enemyCount; }
	
	public int getDelay() { return _delayTotal; }
}

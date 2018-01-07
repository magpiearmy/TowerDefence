package core;

import java.util.ArrayList;
import java.util.List;

import profiles.CommandBase;
import profiles.SpawnProfile;

public class Spawner {
	
	private SpawnProfile _profile;
	private CommandBase _cmd;
	//private EnemyFactory _enemyFac;
	private List<Enemy> _spawnedEnemies = new ArrayList<Enemy>();
	
	public Spawner(SpawnProfile profile)
	{
		_profile = profile;
		_cmd = profile.getNextCommand();
		if (_cmd != null) _cmd.start();
	}
	
	public void update(long elapsed)
	{
		if (_cmd == null) return;
		
		// Let the command update and collect any newly spawned enemies
		List<Enemy> enemies = _cmd.update(elapsed);
		if (enemies != null)
			_spawnedEnemies.addAll(enemies);
		
		// The current command has finished, get the next one
		if (_cmd.isFinished()) {
			_cmd = _profile.getNextCommand();
		}
	}
	
	public boolean hasSpawned() {
		return !_spawnedEnemies.isEmpty();
	}
	
	public List<Enemy> getSpawnedEnemies() {
		List<Enemy> spawnedEnemiesTemp = new ArrayList<Enemy>(_spawnedEnemies);
		_spawnedEnemies.clear();
		return spawnedEnemiesTemp;
	}
}

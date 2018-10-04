package core;

import profiles.Command;
import profiles.SpawnProfile;

import java.util.ArrayList;
import java.util.List;

public class Spawner {

  private SpawnProfile profile;
  private Command cmd;
  //private EnemyFactory _enemyFac;
  private List<Enemy> spawnedEnemies = new ArrayList<Enemy>();

  public Spawner(SpawnProfile profile) {
    this.profile = profile;

    cmd = profile.getNextCommand();
    if (cmd != null) {
      cmd.start();
    }
  }

  public void update(long elapsed) {
    if (cmd == null)
      return;

    // Let the command update and collect any newly spawned enemies
    List<Enemy> enemies = cmd.update(elapsed);
    if (enemies != null)
      spawnedEnemies.addAll(enemies);

    if (cmd.isFinished()) {
      cmd = profile.getNextCommand();
    }
  }

  public boolean hasSpawned() {
    return !spawnedEnemies.isEmpty();
  }

  public List<Enemy> getSpawnedEnemies() {
    List<Enemy> spawnedEnemiesTemp = new ArrayList<Enemy>(spawnedEnemies);
    spawnedEnemies.clear();
    return spawnedEnemiesTemp;
  }
}

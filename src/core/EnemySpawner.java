package core;

import levelprofiles.LevelProfile;
import levelprofiles.ProfileStep;

import java.util.ArrayList;
import java.util.List;

public class EnemySpawner {

  private LevelProfile profile;
  private ProfileStep cmd;
  private List<Enemy> spawnedEnemies = new ArrayList<>();

  public EnemySpawner(LevelProfile profile) {
    this.profile = profile;

    cmd = profile.getNextStep();
    if (cmd != null) {
      cmd.start();
    }
  }

  public void update(long elapsed) {
    if (cmd == null)
      return;

    List<Enemy> newEnemies = cmd.update(elapsed);

    if (newEnemies != null)
      spawnedEnemies.addAll(newEnemies);

    if (cmd.isFinished()) {
      cmd = profile.getNextStep();
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

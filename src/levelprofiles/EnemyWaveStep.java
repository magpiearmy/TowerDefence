package levelprofiles;

import core.Enemy;
import core.EnemyFactory;

import java.util.ArrayList;
import java.util.List;

public class EnemyWaveStep extends ProfileStep {
  private enum State {
    DELAYING, SPAWNING
  }


  private State waveState;
  private int enemyCount;
  private int interval;
  private int delay;

  private int delayTime;
  private int timeSinceSpawn;
  private int spawnCount;
  private EnemyFactory factory;

  public EnemyWaveStep(int enemyCount, int delay, int interval, EnemyFactory factory) {
    this.enemyCount = enemyCount;
    this.delay = delay;
    this.interval = interval;
    this.factory = factory;

    type = StepType.WAVE;
    waveState = State.DELAYING;
    timeSinceSpawn = 0;
    delayTime = 0;
    spawnCount = 0;
  }

  @Override
  public List<Enemy> update(long elapsed) {

    List<Enemy> spawnedEnemies = null;

    switch (waveState) {
      case DELAYING: {
        updateDelayingState(elapsed);
        break;
      }
      case SPAWNING: {
        spawnedEnemies = updateSpawningState(elapsed, spawnedEnemies);
        break;
      }
    }

    return spawnedEnemies;
  }

  private List<Enemy> updateSpawningState(long elapsed, List<Enemy> spawnedEnemies) {
    timeSinceSpawn += elapsed;
    while (timeSinceSpawn >= interval) {
      if (spawnedEnemies == null)
        spawnedEnemies = new ArrayList<>();

      timeSinceSpawn -= interval;
      spawnedEnemies.add(spawnEnemy());
    }

    if (spawnCount == enemyCount) {
      finish();
    }
    return spawnedEnemies;
  }

  private void updateDelayingState(long elapsed) {
    delayTime += elapsed;
    if (delayTime >= delay) {
      waveState = State.SPAWNING;
      timeSinceSpawn = interval + (delayTime - delay);
    }
  }

  private Enemy spawnEnemy() {
    spawnCount++;
    return factory.createEnemy(0);
  }
}

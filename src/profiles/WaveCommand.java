package profiles;

import core.Enemy;
import core.EnemyFactory;

import java.util.ArrayList;
import java.util.List;

public class WaveCommand extends Command {
  private enum State {
    DELAYING, SPAWNING
  }


  private State waveState;
  private int enemyCount;
  private int interval;
  private int totalDelay;

  private int delayTime;
  private int timeSinceSpawn;
  private int spawnCount;
  private EnemyFactory factory;

  public WaveCommand(int enemyCount, int delay, int interval, EnemyFactory factory) {
    type = CommandType.COMMAND_WAVE;
    this.enemyCount = enemyCount;
    totalDelay = delay;
    this.interval = interval;
    this.factory = factory;
    waveState = State.DELAYING;
    timeSinceSpawn = 0;
    delayTime = 0;
    spawnCount = 0;
  }

  public List<Enemy> update(long elapsed) {

    List<Enemy> spawnedEnemies = null;

    switch (waveState) {
      case DELAYING:
        delayTime += elapsed;
        if (delayTime >= totalDelay) {
          waveState = State.SPAWNING;
          timeSinceSpawn = interval + (delayTime - totalDelay);
        }
        break;
      case SPAWNING:
        timeSinceSpawn += elapsed;
        while (timeSinceSpawn >= interval) {
          if (spawnedEnemies == null)
            spawnedEnemies = new ArrayList<Enemy>();

          timeSinceSpawn -= interval;
          spawnedEnemies.add(spawn());
        }

        if (spawnCount == enemyCount) {
          finish();
        }
        break;
    }

    return spawnedEnemies;
  }

  private Enemy spawn() {
    spawnCount++;
    return factory.createEnemy(0);
  }

  public int getEnemyCount() {
    return enemyCount;
  }

  public int getDelay() {
    return totalDelay;
  }
}

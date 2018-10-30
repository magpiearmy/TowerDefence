package levelprofiles;

import core.Enemy;

import java.util.List;

public abstract class ProfileStep {

  protected enum StepState {
    PENDING, RUNNING, FINISHED
  }


  protected StepType type;
  protected StepState state = StepState.PENDING;

  public StepType getType() {
    return type;
  }

  public boolean isFinished() {
    return state == StepState.FINISHED;
  }

  public void start() {
    state = StepState.RUNNING;
  }

  public void finish() {
    state = StepState.FINISHED;
  }

  public List<Enemy> update(long elapsed) {
    return null;
  }
}

package levelprofiles;

public class DelayStep extends ProfileStep {
  private int seconds;

  public DelayStep(int seconds) {
    type = StepType.DELAY;
    this.seconds = seconds;
  }
}

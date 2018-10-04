package profiles;

public class DelayCommand extends Command {
  private int seconds;

  public DelayCommand(int seconds) {
    type = CommandType.COMMAND_DELAY;
    this.seconds = seconds;
  }

  public int getSeconds() {
    return seconds;
  }
}

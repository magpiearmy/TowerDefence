package profiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnProfile {
  private List<Command> commands = new ArrayList<Command>();
  private Iterator<Command> iterator;

  public SpawnProfile() {
  }

  public void addCommand(Command command) {
    commands.add(command);
  }

  public void finish() {
    iterator = commands.iterator();
    iterator.next();
  }

  public Command getNextCommand() {
    if (iterator.hasNext())
      return iterator.next();
    else
      return null;
  }
}

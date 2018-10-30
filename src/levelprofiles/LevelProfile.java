package levelprofiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelProfile {
  private List<ProfileStep> steps = new ArrayList<>();
  private Iterator<ProfileStep> iterator;

  public LevelProfile() {
  }

  public void addStep(ProfileStep command) {
    steps.add(command);
  }

  public ProfileStep getNextStep() {
    if (iterator == null) {
      iterator = steps.iterator();
      iterator.next();
    }

    if (iterator.hasNext())
      return iterator.next();
    else
      return null;
  }
}

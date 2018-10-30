package towers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static towers.BasicElementType.*;

public class ElementProperties {

  private Set<BasicElementType> basicTypes = new HashSet<>();

  /**
   * Turns on an element flag, if allowed.
   *
   * @param type The BasicElementType to be added
   * @return false if the element type was already turned on or if a compound element
   * type is already in effect. Returns true otherwise.
   */
  public boolean addElement(BasicElementType type) {
    return basicTypes.add(type);
  }

  public BasicElementType getBasicType() {
    assert (getCompoundType() == null);

    if (!basicTypes.isEmpty()) {
      return basicTypes.iterator().next();
    } else {
      return null;
    }
  }

  /**
   * Returns the CompoundElementType based on the current basic element flags
   *
   * @return The deduced CompoundElementType, or null if no type
   * could be deduced
   */
  public CompoundElementType getCompoundType() {
    if (basicTypes.containsAll(Arrays.asList(FIRE, EARTH))) return CompoundElementType.LAVA;
    if (basicTypes.containsAll(Arrays.asList(FIRE, WATER))) return CompoundElementType.STEAM;
    if (basicTypes.containsAll(Arrays.asList(EARTH, WATER))) return CompoundElementType.MUD;
    return null;
  }
}

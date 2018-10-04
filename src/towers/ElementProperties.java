package towers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static towers.BasicElementType.*;

public class ElementProperties {

  private Set<BasicElementType> types = new HashSet<>();

  public ElementProperties() {
  }

  /**
   * Turns on an element flag if allowed.
   *
   * @param type The BasicElementType to be added
   * @return false if the element was already turned on or if a compound element
   * type is already in effect. Returns true otherwise.
   */
  public boolean addElement(BasicElementType type) {

    if (getCompoundType() != null)
      return false;

    return types.add(type);
  }

  public BasicElementType getBasicType() {
    assert (getCompoundType() == null);
    if (!types.isEmpty()) {
      return types.iterator().next();
    } else {
      return null;
    }
  }

  /**
   * Returns the CompoundElementType based on the current elements flags
   *
   * @return
   */
  public CompoundElementType getCompoundType() {
    if (types.containsAll(Arrays.asList(FIRE, EARTH)))
      return CompoundElementType.LAVA;
    if (types.containsAll(Arrays.asList(FIRE, WATER)))
      return CompoundElementType.STEAM;
    if (types.containsAll(Arrays.asList(EARTH, WATER)))
      return CompoundElementType.MUD;
    return null;
  }
}

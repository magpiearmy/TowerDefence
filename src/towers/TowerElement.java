package towers;

public class TowerElement {
	
	boolean _fireFlag = false;
	boolean _waterFlag = false;
	boolean _earthFlag = false;
	boolean _windFlag = false;
	
	public TowerElement() {
	}
	
	/**
	 * Turns on an element flag if allowed.
	 * @return false if the element was already turned on or if a dual-element
	 * type is already in effect. Returns true otherwise.
	 * @param type
	 */
	public boolean addElement(BasicElementType type) {
		
		// Can't add element if we already have two set 
		if (getDualType() != null)
			return false;
		
		switch(type)
		{
		case FIRE: 	if (_fireFlag) 	return false;	else _fireFlag	= true; break;
		case WATER: if (_waterFlag) return false; 	else _waterFlag	= true; break;
		case EARTH: if (_earthFlag) return false; 	else _earthFlag = true; break;
		case WIND: 	if (_windFlag) 	return false; 	else _windFlag 	= true; break;
		}
		return true;
	}
	
	public BasicElementType getBasicType() {
		assert(getDualType() == null);
		if (_fireFlag) return BasicElementType.FIRE;
		if (_waterFlag) return BasicElementType.WATER;
		if (_earthFlag) return BasicElementType.EARTH;
		return null;
	}
	
	/**
	 * Returns the DualElementType based on the current elements flags
	 * @return
	 */
	public DualElementType getDualType() {
		if (_fireFlag && _earthFlag) return DualElementType.LAVA;
		if (_fireFlag && _waterFlag) return DualElementType.STEAM;
		if (_waterFlag && _earthFlag) return DualElementType.MUD;
		return null;
	}
}

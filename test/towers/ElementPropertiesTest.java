package towers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static towers.BasicElementType.EARTH;
import static towers.BasicElementType.FIRE;
import static towers.BasicElementType.WATER;
import static towers.CompoundElementType.LAVA;
import static towers.CompoundElementType.MUD;
import static towers.CompoundElementType.STEAM;

class ElementPropertiesTest {

    @Test
    void addElementReturnsTrueForFirstTwoBasicTypes() {
        ElementProperties element = new ElementProperties();
        assertTrue(element.addElement(FIRE));
        assertTrue(element.addElement(WATER));
    }

    @Test
    void addElementReturnsFalseWhenTwoTypesAreAlreadyInAdded() {
        ElementProperties element = new ElementProperties();
        assertTrue(element.addElement(EARTH));
        assertTrue(element.addElement(FIRE));
        assertFalse(element.addElement(WATER));
    }

    @Test
    void addElementReturnsFalseWhenBasicTypeIsAlreadyAdded() {
        ElementProperties element = new ElementProperties();
        assertTrue(element.addElement(EARTH));
        assertFalse(element.addElement(EARTH));
    }

    @Test
    void getCompoundTypeReturnsCorrectTypes() {
        ElementProperties steam = new ElementProperties();
        steam.addElement(FIRE);
        steam.addElement(WATER);
        assertEquals(STEAM, steam.getCompoundType());

        ElementProperties lava = new ElementProperties();
        lava.addElement(FIRE);
        lava.addElement(EARTH);
        assertEquals(LAVA, lava.getCompoundType());

        ElementProperties mud = new ElementProperties();
        mud.addElement(WATER);
        mud.addElement(EARTH);
        assertEquals(MUD, mud.getCompoundType());
    }

    @Test
    void getCompoundTypeReturnsNullForOneOrZeroBasicTypes() {
        ElementProperties element = new ElementProperties();
        assertNull(element.getCompoundType());
        element.addElement(FIRE);
        assertNull(element.getCompoundType());
        element.addElement(WATER);
        assertNotNull(element.getCompoundType());
    }
}
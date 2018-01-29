package core;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class PopupMenu extends Rectangle {

    private List<Integer> options;

    public PopupMenu(int x, int y) {
        super(x, y);
        options = new ArrayList<Integer>();
    }

}

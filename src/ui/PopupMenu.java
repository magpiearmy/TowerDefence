package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial") public class PopupMenu extends Rectangle {

  private final int _x, _y;
  private List<Integer> _options;

  public PopupMenu(int x, int y) {
    _x = x;
    _y = y;
    _options = new ArrayList<Integer>();
  }



}

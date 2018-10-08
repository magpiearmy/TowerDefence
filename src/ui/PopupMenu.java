package ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class PopupMenu extends Rectangle {

  private List<Integer> options;

  public PopupMenu(int x, int y) {
    super(x, y);

    options = new ArrayList<>();
  }

}

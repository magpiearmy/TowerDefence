package core;

public interface ISelectable {

  boolean wasClicked(int clickX, int clickY);

  void select();

  void deselect();

}

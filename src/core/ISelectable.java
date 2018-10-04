package core;

public interface ISelectable {

  public boolean wasClicked(int clickX, int clickY);

  public void select();

  public void deselect();

}

package ui;

import towers.TowerType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ui.Button.BUTTON_SIZE;

public class TowerButtonContainer {
  private final int BUTTON_SPACING = 10;
  private final int BUTTON_SIZE_PLUS_SPACING = BUTTON_SIZE + BUTTON_SPACING;

  private List<Button<TowerType>> buttons = new ArrayList<>();
  private final int screenWidth;

  public TowerButtonContainer(int screenWidth) {
    this.screenWidth = screenWidth;
  }

  public void construct() {
    final int xOffset = (screenWidth - (buttons.size() * BUTTON_SIZE_PLUS_SPACING)) / 2;

    buttons.forEach(button -> {
      final int rectX = button.buttonIndex * BUTTON_SIZE_PLUS_SPACING + xOffset;
      final int rectY = 510;
      Rectangle rect = new Rectangle(rectX, rectY, BUTTON_SIZE, BUTTON_SIZE);
      button.setRect(rect);
    });
  }

  public void addTowerButton(String buttonImageId, TowerType towerType) {
    buttons.add(new Button<>(buttons.size(), towerType, buttonImageId));
  }

  public Optional<TowerType> getClickedTowerType(Point clickPt) {
    return buttons.stream()
      .filter(button -> button.rect.contains(clickPt))
      .map(button -> button.value)
      .findFirst();
  }

  public TowerType getTowerTypeByIndex(int idx) {
    return buttons.stream()
      .filter(button -> button.buttonIndex == idx)
      .findFirst()
      .orElseThrow(IndexOutOfBoundsException::new).value;
  }

  public String getImageIdOfTowerType(TowerType heldItemType) {
    return buttons.stream()
      .filter(button -> button.value.equals(heldItemType))
      .map(button -> button.imageId)
      .findFirst()
      .orElseThrow(RuntimeException::new);
  }

  public void handleMouseOver(Point clickPt) {
    buttons.forEach(button -> button.highlighted = button.rect.contains(clickPt));
  }

  public void drawButtons(Graphics2D g) {
    buttons.forEach(button -> button.draw(g));
  }
}

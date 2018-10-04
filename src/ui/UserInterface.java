package ui;

import core.ImageStore;
import core.Level;
import towers.TowerType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import static ui.Button.BUTTON_SIZE;

public class UserInterface {

  enum ShopState {IDLE, HOLDING_ITEM}


  private TowerButtonContainer towerButtonContainer;

  private Button<Boolean> startButton;
  private String startButtonImageId;
  private TowerType heldItemType;
  private Level level;
  private ImageStore imageStore;

  private ShopState state = ShopState.IDLE;

  public UserInterface(int screenWidth, Level level) {
    this.level = level;
    towerButtonContainer = new TowerButtonContainer(screenWidth);
  }

  public void addTowerButton(String buttonImageId, TowerType towerType) {
    towerButtonContainer.addTowerButton(buttonImageId, towerType);
  }

  public void setImageStore(ImageStore imgs) {
    this.imageStore = imgs;
  }

  public void buildInterface() {
    towerButtonContainer.construct();
    buildStartButton();
  }

  private void buildStartButton() {
    startButtonImageId = imageStore.loadImage("start.png");
    startButton = new Button<>(-1, false, startButtonImageId);
    startButton.setRect(new Rectangle(24, 510, BUTTON_SIZE, BUTTON_SIZE));
  }

  public Optional<TowerType> getHeldItemType() {
    if (state == ShopState.HOLDING_ITEM) {
      return Optional.ofNullable(heldItemType);
    } else {
      return Optional.empty();
    }
  }

  public void clearHeldItem() {
    state = ShopState.IDLE;
    heldItemType = null;
  }

  public void selectItemByIndex(int idx) {
    state = ShopState.HOLDING_ITEM;
    heldItemType = towerButtonContainer.getTowerTypeByIndex(idx);
  }

  public void onLevelStart() {
    startButton.enabled = false;
  }


  public void draw(Graphics2D g, int mouseX, int mouseY) {
    startButton.draw(g);
    towerButtonContainer.drawButtons(g);

    if (heldItemType != null) {
      String imageId = towerButtonContainer.getImageIdOfTowerType(heldItemType);
      Image heldImage = ImageStore.getInstance().getImage(imageId);
      int drawX = mouseX - heldImage.getWidth(null) / 2;
      int drawY = mouseY - heldImage.getHeight(null) / 2;
      g.drawImage(heldImage, drawX, drawY, null);
    }
  }

  /**
   * Mouse handling
   */
  public void handleMouseClick(MouseEvent e) {
    final Point clickPt = new Point(e.getX(), e.getY());

    switch (state) {
      case HOLDING_ITEM:
        handleClickWhenHoldingItem(clickPt);
        break;
      case IDLE:
        handleClickWhenIdle(clickPt);
        break;
      default:
        break;
    }
  }

  private void handleClickWhenHoldingItem(Point clickPt) {
    heldItemType = towerButtonContainer.getClickedTowerType(clickPt).orElse(null);
    if (heldItemType == null)
      state = ShopState.IDLE;
  }

  private void handleClickWhenIdle(Point clickPt) {
    if (startButton.rect.contains(clickPt)) {
      if (!level.isStarted())
        level.start();
    } else {
      heldItemType = towerButtonContainer.getClickedTowerType(clickPt).orElse(null);
      if (heldItemType != null)
        state = ShopState.HOLDING_ITEM;
    }
  }

  public void handleMouseMove(MouseEvent e) {
    final Point mousePoint = new Point(e.getX(), e.getY());

    if (startButton.rect.contains(mousePoint)) {
      startButton.highlighted = true;
    }

    towerButtonContainer.handleMouseOver(mousePoint);
  }
}

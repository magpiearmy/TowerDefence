package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.*;

import core.ImageStore;
import core.Level;
import core.Tile;
import towers.Tower;
import towers.TowerType;

public class Shop {

    private class Button {
        public Rectangle rect;
        public String imageId;

        public Button(String imageId) {
            this.imageId = imageId;
        }

        public void setRect(Rectangle rect) {
            this.rect = rect;
        }
    }

    private class ShopItem {
        public TowerType type;
        public Button button;

        public ShopItem(TowerType type, Button button) {
            this.type = type;
            this.button = button;
        }
    }

    public static final int BUTTON_MARGIN = 5;
    public static final int BUTTON_SIZE = Tile.WIDTH + (BUTTON_MARGIN * 2);

    private int screenWidth;
    private Map<TowerType, Button> towerButtons;
    private List<Button> allButtons = new ArrayList<>();
    private Rectangle highlightedButton;
    private TowerType heldItem;
    private Rectangle startButton;
    private String startButtonImageId;
    private Level level;

    private ImageStore imageStore;

    enum ShopState {IDLE, HOLDING_ITEM, PLACED_ITEM}

    private ShopState state = ShopState.IDLE;


    public Shop(int screenWidth, Level level) {
        this.screenWidth = screenWidth;
        this.level = level;
        towerButtons = new HashMap<>();
        startButton = new Rectangle(24, 510, BUTTON_SIZE, BUTTON_SIZE);
    }

    public void addItem(String imageId, TowerType towerType) {
        towerButtons.put(towerType, new Button(imageId));
    }

    public void setImageStore(ImageStore imgs) {
        this.imageStore = imgs;
    }

    public void construct() {
        final int buttonSpacing = 10;
        final int sizePlusSpacing = BUTTON_SIZE + buttonSpacing;
        int xOffset = (screenWidth - (towerButtons.size() * sizePlusSpacing)) / 2;

        towerButtons.forEach((type, button) -> {
            Rectangle rect = new Rectangle(towerCount++ * sizePlusSpacing + xOffset, 510, BUTTON_SIZE, BUTTON_SIZE);
            button.setRect(rect);
            allButtons.add(newButton);
        });

        startButtonImageId = imageStore.loadImage("start.png");
        allButtons.add(new Button());
    }

    public Image getImageOfHeldItem() {
        if (!Optional.ofNullable(heldItem).isPresent()) {
            return null;
        } else {
            return imageStore.getImage(imageIds.get(items.indexOf(heldItem)));
        }
    }

    public Optional<TowerType> getHeldItem() {
        if (state == ShopState.HOLDING_ITEM) {
            return Optional.ofNullable(heldItem);
        } else {
            return Optional.empty();
        }
    }

    public void clearHeldItem() {
        state = ShopState.IDLE;
        heldItem = null;
    }

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
        boolean clearItem = towerButtons.entrySet().stream()
                .filter(entry -> entry.getValue().contains(clickPt))
                .findFirst()
                .map(entry -> {
                    heldItem = entry.getKey();
                    return false;
                }).orElse(true);

        if (clearItem) {
            clearHeldItem();
        }
    }

    private void handleClickWhenIdle(Point clickPt) {
        towerButtons.entrySet().stream()
                .filter(entry -> entry.getValue().contains(clickPt))
                .findFirst()
                .ifPresent(entry -> {
                    if (entry.getValue() == startButton) {
                        if (!level.isStarted())
                            level.start();
                    } else {
                        state = ShopState.HOLDING_ITEM;
                        heldItem = entry.getKey();
                    }
                });
    }

    public void handleMouseMove(MouseEvent e) {
        final Point clickPt = new Point(e.getX(), e.getY());
        highlightedButton = towerButtons.values().stream()
                .filter(button -> button.contains(clickPt))
                .findFirst()
                .orElseGet(() -> {
                    if (startButton.contains(clickPt))
                        return startButton;
                    else
                        return null;
                });
    }

    public void selectItemByIndex(int idx) {
        state = ShopState.HOLDING_ITEM;
        heldItem = items.get(idx);
    }

    public void draw(Graphics2D g) {
        towerButtons.forEach((type, button) -> {

            if (button == highlightedButton) {
                g.setColor(new Color(130, 130, 140));
            } else {
                g.setColor(new Color(100, 100, 100));
            }

            if (button != startButton)
                drawButton(g, button, imageIds.get(items.indexOf(type)));
        });

        drawStartButton(g);
    }

    private void drawStartButton(Graphics2D g) {
        if (level.isStarted()) {
            g.setColor(new Color(50, 50, 50));
        } else {
            g.setColor(new Color(100, 100, 100));
        }

        drawButton(g, startButton, startButtonImageId);
    }

    private void drawButton(Graphics2D g, Rectangle button, String imageId) {
        g.fillRoundRect(button.x,
                button.y,
                button.width,
                button.height,
                6,
                6);

        Image img = imageStore.getImage(imageId);
        g.drawImage(img,
                button.x + BUTTON_MARGIN,
                button.y + BUTTON_MARGIN,
                null);


        g.setColor(new Color(200, 200, 200));
        g.drawRoundRect(button.x,
                button.y,
                button.width,
                button.height,
                6,
                6);
    }

}

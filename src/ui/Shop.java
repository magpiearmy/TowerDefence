package ui;

import core.ImageStore;
import core.Level;
import core.Tile;
import towers.TowerType;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class Shop {

    public static final int BUTTON_MARGIN = 5;
    public static final int BUTTON_SIZE = Tile.WIDTH + (BUTTON_MARGIN * 2);

    enum ShopState {IDLE, HOLDING_ITEM, PLACED_ITEM}

    private int screenWidth;
    private Map<TowerType, Button> towerButtons;
    private List<Button> allButtons = new ArrayList<>();
    private Button startButton;
    private String startButtonImageId;
    private Button highlightedButton;
    private TowerType heldItemType;
    private Level level;

    private ImageStore imageStore;

    private ShopState state = ShopState.IDLE;

    public Shop(int screenWidth, Level level) {
        this.screenWidth = screenWidth;
        this.level = level;
        towerButtons = new HashMap<>();
    }

    public void addItem(String imageId, TowerType towerType) {
        towerButtons.put(towerType, new Button(towerButtons.size(), imageId));
    }

    public void setImageStore(ImageStore imgs) {
        this.imageStore = imgs;
    }

    public void construct() {
        final int buttonSpacing = 10;
        final int sizePlusSpacing = BUTTON_SIZE + buttonSpacing;
        int xOffset = (screenWidth - (towerButtons.size() * sizePlusSpacing)) / 2;

        towerButtons.forEach((type, button) -> {
            Rectangle rect = new Rectangle(button.buttonIndex * sizePlusSpacing + xOffset, 510, BUTTON_SIZE, BUTTON_SIZE);
            button.setRect(rect);
            allButtons.add(button);
        });

        startButtonImageId = imageStore.loadImage("start.png");
        startButton = new Button(-1, startButtonImageId);
        startButton.setRect(new Rectangle(24, 510, BUTTON_SIZE, BUTTON_SIZE));
        allButtons.add(startButton);
    }

    public Optional<Image> getImageOfHeldItem() {
        if (!Optional.ofNullable(heldItemType).isPresent()) {
            return Optional.empty();
        } else {
            Image img = imageStore.getImage(towerButtons.get(heldItemType).imageId);
            return Optional.of(img);
        }
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
                .filter(entry -> entry.getValue().rect.contains(clickPt))
                .findFirst()
                .map(entry -> {
                    heldItemType = entry.getKey();
                    return false;
                }).orElse(true);

        if (clearItem) {
            clearHeldItem();
        }
    }

    private void handleClickWhenIdle(Point clickPt) {
        if (startButton.rect.contains(clickPt) && !level.isStarted()) {
            level.start();
        } else {
            towerButtons.entrySet().stream()
                    .filter(entry -> entry.getValue().rect.contains(clickPt))
                    .findFirst()
                    .ifPresent(entry -> {
                        if (entry.getValue() == startButton) {
                            if (!level.isStarted())
                                level.start();
                        } else {
                            state = ShopState.HOLDING_ITEM;
                            heldItemType = entry.getKey();
                        }
                    });
        }
    }

    public void handleMouseMove(MouseEvent e) {
        final Point clickPt = new Point(e.getX(), e.getY());
        highlightedButton = allButtons.stream()
                .filter(button -> button.rect.contains(clickPt))
                .findFirst()
                .orElseGet(() -> {
                    if (startButton.rect.contains(clickPt))
                        return startButton;
                    else
                        return null;
                });
    }

    public void selectItemByIndex(int idx) {
        state = ShopState.HOLDING_ITEM;
        heldItemType = towerButtons.entrySet().stream()
                .filter(entry -> entry.getValue().buttonIndex == idx)
                .findFirst()
                .orElseThrow(IndexOutOfBoundsException::new)
                .getKey();
    }

    public void draw(Graphics2D g) {
        allButtons.forEach(button -> {
            if (button == startButton && level.isStarted()) {
                g.setColor(new Color(50, 50, 50));
            } else if (button == highlightedButton) {
                g.setColor(new Color(130, 130, 140));
            } else {
                g.setColor(new Color(100, 100, 100));
            }

            drawButton(g, button);
        });
    }

    private void drawButton(Graphics2D g, Button button) {
        g.fillRoundRect(button.rect.x,
                button.rect.y,
                button.rect.width,
                button.rect.height,
                6,
                6);

        Image img = imageStore.getImage(button.imageId);
        g.drawImage(img,
                button.rect.x + BUTTON_MARGIN,
                button.rect.y + BUTTON_MARGIN,
                null);


        g.setColor(new Color(200, 200, 200));
        g.drawRoundRect(button.rect.x,
                button.rect.y,
                button.rect.width,
                button.rect.height,
                6,
                6);
    }

}

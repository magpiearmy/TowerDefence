package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import core.ImageStore;
import core.Level;
import core.Tile;

public class Shop {

    public static final int BUTTON_MARGIN = 5;
    public static final int BUTTON_SIZE = Tile.WIDTH + (BUTTON_MARGIN * 2);

    private int screenWidth;
    private Vector<Rectangle> buttons; // The bounds of all buttons
    private int highlightedButton = -1; // Array index of highlighted button
    private int heldItemIndex = -1; // Array index of the currently held item
    private Rectangle startButton;
    private Level level;

    private ImageStore imgs;
    private Vector<Integer> items; // Shop objects
    private Vector<String> images; // Shop object images

    enum ShopState {IDLE, HOLDING_ITEM, PLACED_ITEM}

    private ShopState state = ShopState.IDLE;


    public Shop(int screenWidth, Level level) {
        this.screenWidth = screenWidth;
        buttons = new Vector<Rectangle>();
        items = new Vector<Integer>();
        images = new Vector<String>();
        startButton = new Rectangle(24, 510, BUTTON_SIZE, BUTTON_SIZE);
        this.level = level;
    }

    public void addItem(String imgId) {
        items.add(items.size() + 1);
        images.add(imgId);
    }

    public void setImageStore(ImageStore imgs) {
        this.imgs = imgs;
    }

    public void construct() {
        final int buttonSpacing = 10;
        final int sizePlusSpacing = BUTTON_SIZE + buttonSpacing;
        int xOffset = (screenWidth - (items.size() * sizePlusSpacing)) / 2;

        for (int i = 0; i < items.size(); i++) {
            buttons.add(new Rectangle(i * sizePlusSpacing + xOffset, 510, BUTTON_SIZE, BUTTON_SIZE));
        }

        String startId = imgs.loadImage("start.png");
        images.add(startId);
        buttons.add(startButton);
    }

    public Image getImageOfHeldItem() {
        if (heldItemIndex == -1) return null;
        else {
            return imgs.getImage(images.elementAt(heldItemIndex));
        }
    }

    public int getHeldItem() {
        if (state == ShopState.HOLDING_ITEM) {
            return items.elementAt(heldItemIndex);
        } else
            return -1;
    }

    public void clearHeldItem() {
        state = ShopState.IDLE;
        heldItemIndex = -1;
    }

    public void handleMouseClick(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        Point clickPt = new Point(mx, my);

        switch (state) {
            case HOLDING_ITEM:

                boolean clearItem = true;

                // Check if the user clicked a shop item
                for (int i = 0; i < buttons.size(); i++) {
                    if (buttons.elementAt(i).contains(clickPt)) {
                        heldItemIndex = i;
                        clearItem = false;
                        break;
                    }
                }

                if (clearItem) {
                    clearHeldItem();
                }
                break;


            case IDLE:

                for (int i = 0; i < buttons.size(); i++) {
                    Rectangle thisButton = buttons.elementAt(i);
                    if (thisButton.contains(clickPt)) {
                        if (thisButton == startButton) {
                            if (!level.isStarted())
                                level.start();
                            return;
                        } else {
                            state = ShopState.HOLDING_ITEM;
                            heldItemIndex = i;
                            return;
                        }
                    }
                }
                break;

            default:
                break;
        }
    }

    public void handleMouseMove(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.elementAt(i).contains(new Point(mx, my))) {
                highlightedButton = i;
                return;
            }
        }
        highlightedButton = -1;
    }

    public void selectItem(int idx) {
        state = ShopState.HOLDING_ITEM;
        heldItemIndex = idx;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < buttons.size(); i++) {
            Rectangle thisButton = buttons.elementAt(i);
            Image img = imgs.getImage(images.elementAt(i));

            if (thisButton == startButton && level.isStarted())
                g.setColor(new Color(50, 50, 50));
            else if (i == highlightedButton)
                g.setColor(new Color(130, 130, 140));
            else
                g.setColor(new Color(100, 100, 100));

            g.fillRoundRect(thisButton.x,
                    thisButton.y,
                    thisButton.width,
                    thisButton.height,
                    6,
                    6);
            g.drawImage(img,
                    thisButton.x + BUTTON_MARGIN,
                    thisButton.y + BUTTON_MARGIN,
                    null);

            g.setColor(new Color(200, 200, 200));

            g.drawRoundRect(thisButton.x,
                    thisButton.y,
                    thisButton.width,
                    thisButton.height,
                    6,
                    6);
        }
    }

}

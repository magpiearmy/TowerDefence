package core;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import ui.Shop;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Runnable {

    private Thread thread = new Thread(this);

    private Level level;
    private int levelDrawOffsetX = 0;
    private int levelDrawOffsetY = 0;
    private Shop shop;
    private int mouseX;
    private int mouseY;

    public GameScreen() {
        level = new Level(this);
        shop = new Shop(900, level);
        setBackground(new Color(20, 60, 90));
        setFocusable(true);
    }

    public void init() {
        level.init(shop);

        setupInputHandlers();

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        thread.start();
    }

    private void setupInputHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if the point lies within the main level window
                if (getLevelBounds().contains(e.getPoint())) {

                    // Convert the mouse event into level coordinates
                    e.translatePoint(-levelDrawOffsetX, -levelDrawOffsetY);

                    // Notify the level of the click
                    level.handleMouseEvent(e);
                    e.consume();

                } else {
                    // Click was outside the level so let the shop handle it
                    shop.handleMouseClick(e);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                shop.handleMouseMove(e);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke('1'), "pressedNumber");
        getInputMap().put(KeyStroke.getKeyStroke('2'), "pressedNumber");
        getInputMap().put(KeyStroke.getKeyStroke('3'), "pressedNumber");
        getActionMap().put("pressedNumber", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                shop.selectItemByIndex(Integer.parseInt(arg0.getActionCommand()) - 1);
            }
        });
    }

    public Rectangle getLevelBounds() {
        return new Rectangle(levelDrawOffsetX, levelDrawOffsetY,
                level.getWidthInPixels(), level.getHeightInPixels());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int levelWidth = level.getWidthInPixels();
        int levelHeight = level.getHeightInPixels();
        levelDrawOffsetX = (getWidth() - levelWidth) / 2;    // x-Offset to centre the level in the panel
        levelDrawOffsetY = 5;                                // y-Offset

        // This buffered image is what the level will draw onto
        BufferedImage mapCanvas = new BufferedImage(levelWidth, levelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D mapGfx = (Graphics2D) mapCanvas.getGraphics();

        BufferedImage infoboxCanvas = new BufferedImage(100, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D infoboxGfx = (Graphics2D) infoboxCanvas.getGraphics();

        // Let level to the image canvas
        level.draw(mapGfx, infoboxGfx);

        // Add a small border
        mapGfx.setColor(Color.white);
        mapGfx.drawRect(0, 0, levelWidth - 1, levelHeight - 1);

        // Draw the map and infobox
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(mapCanvas, null, levelDrawOffsetX, levelDrawOffsetY);
        g2d.drawImage(infoboxCanvas, null, levelDrawOffsetX + levelWidth - infoboxCanvas.getWidth(), 520);

        // Draw the shop
        shop.draw(g2d);
        shop.getImageOfHeldItem().ifPresent(heldImage -> {
            int drawX = mouseX - heldImage.getWidth(null) / 2;
            int drawY = mouseY - heldImage.getHeight(null) / 2;
            g2d.drawImage(heldImage, drawX, drawY, null);
        });
    }

    @Override
    public void run() {

        boolean isRunning = true;
        long currentNanos;
        long previousNanos = System.nanoTime();
        while (isRunning) {
            currentNanos = System.nanoTime();
            long elapsedMillis = (currentNanos - previousNanos) / (long) 1000000;

            if (elapsedMillis > 10)
            {
                level.update(elapsedMillis);
                repaint();
                previousNanos = System.nanoTime();
            }

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

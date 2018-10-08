package core;

import ui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Runnable {

  private Thread thread = new Thread(this);

  private UserInterface userInterface;
  private Level level;
  private int levelDrawOffsetX = 0;
  private int levelDrawOffsetY = 0;
  private int mouseX;
  private int mouseY;

  public GameScreen() {
    level = new Level();
    userInterface = new UserInterface(900, level);
    setBackground(new Color(20, 60, 90));
    setFocusable(true);
  }

  public void init() {
    level.init(userInterface);

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
          // Click was outside the level so let the userInterface handle it
          userInterface.handleMouseClick(e);
        }
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        userInterface.handleMouseMove(e);
      }
    });

    getInputMap().put(KeyStroke.getKeyStroke('1'), "pressedNumber");
    getInputMap().put(KeyStroke.getKeyStroke('2'), "pressedNumber");
    getInputMap().put(KeyStroke.getKeyStroke('3'), "pressedNumber");
    getActionMap().put("pressedNumber", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        userInterface.selectItemByIndex(Integer.parseInt(arg0.getActionCommand()) - 1);
      }
    });
  }

  public Rectangle getLevelBounds() {
    return new Rectangle(levelDrawOffsetX, levelDrawOffsetY, level.getWidthInPixels(),
      level.getHeightInPixels());
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    int levelWidth = level.getWidthInPixels();
    int levelHeight = level.getHeightInPixels();
    levelDrawOffsetX = (getWidth() - levelWidth) / 2;
    levelDrawOffsetY = 5;

    // This buffered image is what the level will draw onto
    BufferedImage mapCanvas =
      new BufferedImage(levelWidth, levelHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D mapGfx = (Graphics2D) mapCanvas.getGraphics();

    BufferedImage infoboxCanvas = new BufferedImage(120, 50, BufferedImage.TYPE_INT_RGB);
    Graphics2D infoboxGfx = (Graphics2D) infoboxCanvas.getGraphics();
    infoboxGfx.setColor(new Color(29, 54, 64));
    infoboxGfx.fillRect(0, 0, infoboxCanvas.getWidth(), infoboxCanvas.getHeight());
    infoboxGfx.setColor(Color.WHITE);
    infoboxGfx.drawRect(0, 0, infoboxCanvas.getWidth() - 1, infoboxCanvas.getHeight() - 1);

    level.draw(mapGfx, infoboxGfx);

    // Add a small border
    mapGfx.setColor(Color.white);
    mapGfx.drawRect(0, 0, levelWidth - 1, levelHeight - 1);

    // Draw the map and infobox
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(mapCanvas, null, levelDrawOffsetX, levelDrawOffsetY);
    g2d.drawImage(infoboxCanvas, null, levelDrawOffsetX + levelWidth - infoboxCanvas.getWidth(),
      520);

    userInterface.draw(g2d, mouseX, mouseY);
  }

  @Override
  public void run() {

    boolean isRunning = true;
    long currentNanos;
    long previousNanos = System.nanoTime();
    while (isRunning) {
      currentNanos = System.nanoTime();
      long elapsedMillis = (currentNanos - previousNanos) / (long) 1000000;

      if (elapsedMillis > 10) {
        level.update(elapsedMillis);
        repaint();
        previousNanos = System.nanoTime();
      }

      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        isRunning = false;
      }
    }
  }

}

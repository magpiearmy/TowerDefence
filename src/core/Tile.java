package core;

import java.awt.*;
import java.io.*;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Tile extends Rectangle {

    public static final int WIDTH = 48;
    public static final int HEIGHT = 48;

    public Image texture;
    public String textureId;

    public Tile() {
        setBounds(0, 0, WIDTH, HEIGHT);
    }

    public Tile(int x, int y) {
        setBounds(x, y, WIDTH, HEIGHT);
    }

    public Point getCenter() {
        return new Point((int) getCenterX(), (int) getCenterY());
    }

    public void setTextureId(String id) {
        textureId = id;
    }

    public void loadTexture(String textureFilepath) {
        try {
            texture = ImageIO.read(new File(textureFilepath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

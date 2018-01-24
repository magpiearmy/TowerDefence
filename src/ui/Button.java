package ui;

import java.awt.*;

class Button {
    public int buttonIndex;
    public Rectangle rect;
    public String imageId;

    public Button(int buttonIndex, String imageId) {
        this.buttonIndex = buttonIndex;
        this.imageId = imageId;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}

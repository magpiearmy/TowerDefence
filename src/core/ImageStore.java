package core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageStore {
  private static final ImageStore INSTANCE = new ImageStore();
  private static final String RESOURCES_DIR = "resources/";

  private Map<String, BufferedImage> imageMap = new HashMap<>();

  public static ImageStore getInstance() {
    return INSTANCE;
  }

  private ImageStore() {
  }

  public String loadImage(String relativeFilepath) {
    String key = relativeFilepath;
    StringBuilder fullPath = new StringBuilder();
    fullPath.append(RESOURCES_DIR).append(relativeFilepath);

    try {
      BufferedImage img = ImageIO.read(new File(fullPath.toString()));
      imageMap.put(key, img);
    } catch (IOException e) {
      System.err.println("Failed to load image resource [" + relativeFilepath + "]");
      throw new RuntimeException(e);
    }

    return key;
  }

  public Image getImage(String imgId) {
    return imageMap.get(imgId);
  }
}

package core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    StringBuilder fullPath = new StringBuilder();
    fullPath.append(RESOURCES_DIR).append(relativeFilepath);

    try {
      final String key = relativeFilepath;
      BufferedImage img = ImageIO.read(new File(fullPath.toString()));
      imageMap.put(key, img);

      return key;

    } catch (IOException e) {
      System.err.println("Failed to load image resource [" + relativeFilepath + "]");
      throw new RuntimeException(e);
    }
  }

  public Image getImage(String imageId) {
    return imageMap.get(imageId);
  }
}

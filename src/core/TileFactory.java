package core;

public class TileFactory {
  private static final String GRASS_IMAGE_ID;
  private static final String PATH_IMAGE_ID;

  static {
    ImageStore images = ImageStore.getInstance();
    GRASS_IMAGE_ID = images.loadImage("grass.png");
    PATH_IMAGE_ID = images.loadImage("path.png");
  }

  public Tile createTile(int x, int y, int type) {
    Tile newTile = new Tile();

    switch (type) {
      case 0:
        newTile = new Tile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
        newTile.setImageId(GRASS_IMAGE_ID);
        break;
      case 1:
        newTile = new Tile(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
        newTile.setImageId(PATH_IMAGE_ID);
        break;
    }

    return newTile;
  }
}

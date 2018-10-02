package core;

public class TileFactory {

	private static final String RESOURCE_DIRECTORY_PATH = "resources/";
	
	public static final String FILEPATH_GRASS = RESOURCE_DIRECTORY_PATH + "grass.png";
	public static final String FILEPATH_PATH = RESOURCE_DIRECTORY_PATH + "path.png";	
	
	private static String grassImg = null;
	private static String pathImg = null;
	
	private ImageStore imgs;
	
	public TileFactory(ImageStore imgs)
	{
		this.imgs = imgs;
		grassImg = imgs.loadImage("grass.png");
		pathImg = imgs.loadImage("path.png");
	}
	
	public Tile createTile(int x, int y, int type)
	{
		Tile newTile = new Tile();
		
		switch (type)
		{
		case 0:
			newTile = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT);
			newTile.setTextureId(grassImg);
			break;
		case 1:
			newTile = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT);
			newTile.setTextureId(pathImg);
			break;
		}
		
		return newTile;
	}
}

package core;

public class TileFactory {

	private static final String RESOURCE_DIRECTORY_PATH = "Res/";
	
	public static final String FILEPATH_GRASS = RESOURCE_DIRECTORY_PATH + "grass.png";
	public static final String FILEPATH_PATH = RESOURCE_DIRECTORY_PATH + "path.png";	
	
	private static String _grassImg = null;
	private static String _pathImg = null;
	
	private ImageStore _imgs;
	
	public TileFactory(ImageStore imgs)
	{
		_imgs = imgs;
	}
	
	public Tile createTile(int x, int y, int type)
	{
		Tile newTile = new Tile();
		
		switch (type)
		{
		case 0:
			newTile = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT);
			if (_grassImg == null) _grassImg = _imgs.loadImage("grass.png");
			newTile.setTextureId(_grassImg);
			break;
		case 1:
			newTile = new Tile(x * Tile.WIDTH, y * Tile.HEIGHT);
			if (_pathImg == null) _pathImg = _imgs.loadImage("path.png");
			newTile.setTextureId(_pathImg);
			break;
		}
		
		return newTile;
	}
}

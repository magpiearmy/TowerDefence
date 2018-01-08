package core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageStore
{
	private static ImageStore _store = null;
	
	private final String 	_resDir;
	
	private Map<String, BufferedImage> _imageMap = new HashMap<String, BufferedImage>();
	
	public static ImageStore get() {
		if (_store == null)
			_store = new ImageStore();
		return _store;
	}
	
	protected ImageStore()
	{
		_resDir = "Res/";
	}
	
	public String loadImage(String relFilepath)
	{
		BufferedImage img;
		String key = relFilepath;
		StringBuilder fullPath = new StringBuilder();
		fullPath.append(_resDir).append(relFilepath);
		
		try
		{
			img = ImageIO.read(new File(fullPath.toString()));
			_imageMap.put(key, img);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return key;
	}
	
	public Image getImage(String imgId)
	{
		return _imageMap.get(imgId);
	}
}

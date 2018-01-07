package profiles;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import core.EnemyFactory;

public class ProfileParser
{
	private static final String PROFILE_PATH = "Res\\SpawnProfiles\\";
	private EnemyFactory _factory;
	
	public ProfileParser(EnemyFactory factory) {
		_factory = factory;
	}
	
	public SpawnProfile parse(String filepath)
	{
		SpawnProfile profile = new SpawnProfile();
		try
		{
			File fXmlFile = new File(PROFILE_PATH + filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			NodeList nList = doc.getElementsByTagName("*");
			
			for (int i = 0; i < nList.getLength(); i++)
			{
				profile.addCommand( parseCommandNode(nList.item(i)) );
			}
			
			profile.finalize();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return profile;
	}
	
	private CommandBase parseCommandNode(Node node)
	{
		if (node.getNodeName().equals("wave"))
		{
			NamedNodeMap attributes = node.getAttributes();
			int enemyCount = Integer.parseInt( attributes.getNamedItem("enemyCount").getTextContent() );
			int interval = Integer.parseInt( attributes.getNamedItem("interval").getTextContent() );
			int delay = Integer.parseInt( attributes.getNamedItem("delay").getTextContent() );
			return new WaveCommand(enemyCount, delay, interval, _factory);
		}
		else if (node.getNodeName().equals("delay"))
		{
			NamedNodeMap attributes = node.getAttributes();
			int seconds = Integer.parseInt( attributes.getNamedItem("seconds").getTextContent() );
			return new DelayCommand(seconds);
		}
		return null;
	}
}
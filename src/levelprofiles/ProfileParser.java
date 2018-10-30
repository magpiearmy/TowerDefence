package levelprofiles;

import core.EnemyFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ProfileParser {
  private static final String PROFILE_PATH = "/level_profiles/";
  private EnemyFactory factory;

  public ProfileParser(EnemyFactory factory) {
    this.factory = factory;
  }

  public LevelProfile parse(String filepath) {
    LevelProfile profile = new LevelProfile();

    try {
      InputStream xmlInputStream = getClass().getResourceAsStream(PROFILE_PATH + filepath);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlInputStream);

      NodeList nList = doc.getElementsByTagName("*");

      for (int i = 0; i < nList.getLength(); i++) {
        profile.addStep(parseStepNode(nList.item(i)));
      }

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return profile;
  }

  private ProfileStep parseStepNode(Node node) {
    if (node.getNodeName().equals("wave")) {
      NamedNodeMap attributes = node.getAttributes();
      int enemyCount = Integer.parseInt(attributes.getNamedItem("enemyCount").getTextContent());
      int interval = Integer.parseInt(attributes.getNamedItem("interval").getTextContent());
      int delay = Integer.parseInt(attributes.getNamedItem("delay").getTextContent());
      return new EnemyWaveStep(enemyCount, delay, interval, factory);
    }
    return null;
  }
}

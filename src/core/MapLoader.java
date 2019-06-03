package core;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
  private static final String MAPS_DIRECTORY_PATH = "/maps/";

  private BufferedReader reader;
  private String filename;

  public MapLoader(String filename) {
    this.filename = MAPS_DIRECTORY_PATH + filename;
  }

  public Map loadMap() {
    InputStream is = MapLoader.class.getResourceAsStream(filename);
    reader = new BufferedReader(new InputStreamReader(is));

    List<String> mapLines = new ArrayList<>();
    String thisLine;
    try {
      while ((thisLine = reader.readLine()) != null) {
        mapLines.add(thisLine);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to load map file");
    }

    Map map = parseMapLines(mapLines);
    if (map.getStart() == null || map.getEnd() == null)
      throw new RuntimeException("Map definition is missing start and/or end tile");

    map.loadPath();
    return map;
  }

  private Map parseMapLines(List<String> mapLines) {
    int[][] mapData;
    int mapWidth = mapLines.get(0).length();
    int mapHeight = mapLines.size();
    mapData = new int[mapHeight][mapWidth];

    Point start = null;
    Point end = null;

    for (int i = 0; i < mapHeight; i++) {
      for (int j = 0; j < mapWidth; j++) {
        char ch = mapLines.get(i).charAt(j);
        if (ch == 'S') {
          start = new Point(j, i);
          mapData[i][j] = 1;
        } else if (ch == 'E') {
          end = new Point(j, i);
          mapData[i][j] = 1;
        } else {
          mapData[i][j] = ch - '0';
        }
      }
    }

    return new Map(mapData, start, end);
  }

}

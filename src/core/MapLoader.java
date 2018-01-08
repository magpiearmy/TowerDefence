package core;

import java.io.*;
import java.util.Vector;

public class MapLoader {
    private static String MAPS_DIRECTORY_PATH = "/Maps/";
    private BufferedReader reader;

    public MapLoader(String mapFilepath) {
        mapFilepath = MAPS_DIRECTORY_PATH + mapFilepath;
        InputStream is = MapLoader.class.getResourceAsStream(mapFilepath);
        reader = new BufferedReader(new InputStreamReader(is));
    }

    public Map loadMap() throws Exception {
        Map map;
        int[][] mapData;

        Vector<String> stringMap = new Vector<String>();

        // Read the map file
        boolean eof = false;
        while (!eof) {
            try {
                // Read a single line
                String thisLine = reader.readLine();

                if (thisLine == null) {
                    eof = true;
                } else {
                    stringMap.add(thisLine);
                }
            } catch (IOException e) {
            }
        }

        // Allocate the map array
        int mapWidth = stringMap.elementAt(0).length();
        int mapHeight = stringMap.size();
        mapData = new int[mapHeight][mapWidth];

        Vector2D start = null;
        Vector2D end = null;

        // Go through the string map and build the actual map data
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                char ch = stringMap.elementAt(i).charAt(j);
                if (ch == 'S') {
                    start = new Vector2D(j, i);
                    mapData[i][j] = 1;
                } else if (ch == 'E') {
                    end = new Vector2D(j, i);
                    mapData[i][j] = 1;
                } else {
                    mapData[i][j] = ch - '0';
                }
            }
        }

        // A map must have a start and end point
        if (start == null || end == null)
            throw new Exception();

        // Create the final map object
        map = new Map(mapData, start, end);

        // Let the map parse the path and waypoints
        map.loadPath();

        return map;
    }

}

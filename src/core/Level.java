package core;

import profiles.ProfileParser;
import towers.Tower;
import towers.TowerFactory;
import towers.TowerType;
import ui.UserInterface;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Level {
    // Level data
    private Map map;
    private int towerMap[][];
    private Tile tiles[][];
    private MapLoader mapLoader;
    private UserInterface ui;
    private final int tileSize;
    private int widthInTiles;
    private int heightInTiles;

    private boolean started = false;

    // Resource data
    private String grassImg;
    private String pathImg;
    private String tower1Img;
    private String tower2Img;
    private String tower3Img;
    private ImageStore imgs = ImageStore.get();

    private BulletManager bulletManager;
    private TowerFactory towerFactory;
    private EnemyFactory enemyFactory;
    private Vector<Tower> towers = new Vector<>();
    private Vector<Enemy> enemies = new Vector<>();
    private Vector<ISelectable> clickables = new Vector<>();

    private Spawner spawner;

    // Player data
    private int livesRemaining;
    private int money;

    public Level() {
        tileSize = Tile.WIDTH;
        mapLoader = new MapLoader("Level_0.txt");
    }

    public void init(UserInterface ui) {
        this.ui = ui;

        loadMap();
        buildTileArray();
        buildTowerArray();

        // TODO This code should be part of a separate resource manager
        // Load the necessary textures into memory
//		grassImg = imgs.loadImage("grass.png");
//		pathImg = imgs.loadImage("path.png");
        tower1Img = imgs.loadImage("tower1.png");
        tower2Img = imgs.loadImage("tower2.png");
        tower3Img = imgs.loadImage("tower3.png");

        setupUI(ui);

        // Create the bullet manager and tower factory
        bulletManager = new BulletManager(getWidthInPixels(), getHeightInPixels());
        bulletManager.init();
        towerFactory = new TowerFactory(bulletManager);

        // Get the start positon from the map and get the corresponding tile
        Point startPos = map.getStart();
        Tile startTile = tiles[startPos.x][startPos.y];

        // Create enemy factory
        EnemyFactory enemyFactory = new EnemyFactory(startTile.getCenter(), map.getWaypoints(), imgs);

        ProfileParser profParser = new ProfileParser(enemyFactory);
        spawner = new Spawner(profParser.parse("profile1.xml"));

        livesRemaining = 5;
        money = 300;
    }

    private void setupUI(UserInterface ui) {
        ui.setImageStore(imgs);
        ui.addTowerButton(tower1Img, TowerType.PROJECTILE);
        ui.addTowerButton(tower2Img, TowerType.RAY);
        ui.addTowerButton(tower3Img, TowerType.AREA);
        ui.buildInterface();
    }

    private void loadMap() {
        if (mapLoader != null) {
            try {
                map = mapLoader.loadMap();
            } catch (Exception e) { //TODO: MapLoadException
                e.printStackTrace();
            }
        }

        widthInTiles = map.getWidth();
        heightInTiles = map.getHeight();
    }

    private void buildTileArray() {
        tiles = new Tile[widthInTiles][heightInTiles];
        TileFactory tileFactory = new TileFactory(imgs);
        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < widthInTiles; x++) {
                tiles[x][y] = tileFactory.createTile(x, y, map.getTile(x, y));
            }
        }
    }

    private void buildTowerArray() {
        towerMap = new int[widthInTiles][heightInTiles];
        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < widthInTiles; x++) {
                towerMap[x][y] = 0;
            }
        }
    }

    public int getWidthInPixels() {
        return widthInTiles * tileSize;
    }

    public int getHeightInPixels() {
        return heightInTiles * tileSize;
    }

    public void start() {
        started = true;
        ui.onLevelStart();
    }

    public boolean isStarted() {
        return started;
    }

    public void handleMouseEvent(MouseEvent e) {
        final int mx = e.getX();
        final int my = e.getY();

        if (ui.getHeldItemType().isPresent()) {
            placeTower(mx, my, ui.getHeldItemType().get());
        } else {
            for (ISelectable clickable : clickables) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (clickable.wasClicked(mx, my)) {
                        clickable.select();
                    } else {
                        clickable.deselect();
                    }
                }
            }
        }
    }

    public void placeTower(int clickX, int clickY, TowerType type) {
        int towerX = (clickX - clickX % Tile.WIDTH);
        int towerY = (clickY - clickY % Tile.HEIGHT);
        int mapX = towerX / Tile.WIDTH;
        int mapY = towerY / Tile.HEIGHT;

        if (canPlaceTowerAtTile(mapX, mapY)) {

            ui.clearHeldItem();

            Tower newTower = towerFactory.createTower(towerX, towerY, type);
            int cost = newTower.getCost(); // TODO store cost with a tower type instead of tower instance
            if (money >= cost) {
                towerMap[mapX][mapY] = 1; // Mark this tower in the map
                towers.add(newTower);
                clickables.add(newTower);
                money -= cost;
            } else {
                System.out.println(new StringBuilder("Cannot afford that tower! Cost [" + cost
                        + "] Funds [" + money + "]").toString());
            }
        }
    }

    private boolean canPlaceTowerAtTile(int mapX, int mapY) {
        return (towerMap[mapX][mapY] == 0 && map.getTile(mapX, mapY) != 1);
    }

    public void update(long elapsed) {
        if (!started) return;

        checkForEndGame();

        updateEnemies(elapsed);
        updateTowers(elapsed);
        bulletManager.updateBullets(elapsed);

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.elementAt(i);
            if (enemy.isDead()) {
                onEnemyKilled(enemy);
            } else if (enemy.hasEscaped()) {
                onEnemyEscaped(enemy);
            }
        }
    }

    private void onEnemyEscaped(Enemy thisEnemy) {
        enemies.remove(thisEnemy);
        livesRemaining -= thisEnemy.getEscapeCost();
    }

    private void onEnemyKilled(Enemy thisEnemy) {
        enemies.remove(thisEnemy);
        bulletManager.onTargetKilled(thisEnemy);
        money += thisEnemy.getYield();
    }

    private void updateTowers(long elapsed) {
        for (Tower tower : towers) {
            tower.update(elapsed); // Update the tower
            tower.fire(enemies); // Allow towers to fire at a target
        }
    }

    private void updateEnemies(long elapsed) {
        spawner.update(elapsed);

        if (spawner.hasSpawned()) {
            enemies.addAll(spawner.getSpawnedEnemies());
        }

        for (int i = 0; i < enemies.size(); i++) {
            enemies.elementAt(i).update(elapsed);
        }
    }

    private boolean checkForEndGame() {
        if (livesRemaining <= 0) return true;
        if (enemies.size() == 0) return true;
        return false;
    }

    /**
     * Drawing
     */
    public void draw(Graphics2D mapGfx, Graphics2D infoboxGfx) {
        mapGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        infoboxGfx.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        infoboxGfx.setFont(new Font("Consolas", Font.PLAIN, 14));
        infoboxGfx.drawString(new StringBuilder("Money: " + money).toString(), 10, 20);
        infoboxGfx.drawString(new StringBuilder("Lives: " + livesRemaining).toString(), 10, 40);

        // Tiles
        for (int y = 0; y < heightInTiles; y++) {
            for (int x = 0; x < widthInTiles; x++) {
                Tile thisTile = tiles[x][y];

                mapGfx.drawImage(imgs.getImage(thisTile.textureId), thisTile.x, thisTile.y, null);

                mapGfx.setColor(new Color(80, 120, 80));
                mapGfx.drawRect(thisTile.x, thisTile.y, thisTile.width, thisTile.height);

                // TODO: thisTile.draw();
            }
        }


        // Enemies
        List<HealthBar> healthBars = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                mapGfx.setColor(new Color(69, 67, 120));
            } else if (enemy.isDying()) {
                mapGfx.setColor(new Color(80, 30, 30, 120));
            }
            enemy.draw(mapGfx, imgs);

            if (enemy.isAlive()) {
                healthBars.add(enemy.getHealthBar());
            }
        }

        // Towers
        for (Tower tower : towers) {
            tower.draw(mapGfx);
            if (tower.isSelected()) {
                Circle range = tower.getFireRadius();
                mapGfx.setColor(new Color(200, 200, 200));
                mapGfx.drawOval(range.getX() - range.getRadius(), range.getY() - range.getRadius(),
                        range.getRadius() * 2, range.getRadius() * 2);
            }
        }

        for (HealthBar bar : healthBars) {
            bar.draw(mapGfx);
        }

        bulletManager.drawBullets(mapGfx);
    }

}

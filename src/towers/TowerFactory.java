package towers;

import core.BulletManager;
import core.ImageStore;
import core.Tile;

import java.awt.*;

public class TowerFactory {
  private static final String TOWER1_TEXTURE_ID;
  private static final String TOWER2_TEXTURE_ID;
  private static final String TOWER3_TEXTURE_ID;

  static {
    final String tower1Filename = "tower1.png";
    final String tower2Filename = "tower2.png";
    final String tower3Filename = "tower3.png";

    ImageStore images = ImageStore.getInstance();
    TOWER1_TEXTURE_ID = images.loadImage(tower1Filename);
    TOWER2_TEXTURE_ID = images.loadImage(tower2Filename);
    TOWER3_TEXTURE_ID = images.loadImage(tower3Filename);
  }

  private BulletManager bulletManager;

  public TowerFactory(BulletManager bulletManager) {
    this.bulletManager = bulletManager;
  }

  public Tower createTower(int x, int y, TowerType type) {

    Point towerCentre = new Point(x + Tile.TILE_WIDTH / 2, y + Tile.TILE_HEIGHT / 2);

    switch (type) {
      case PROJECTILE: {
        final int range = 120;
        BulletTower tower = new BulletTower(towerCentre, range, bulletManager);
        tower.setCost(50);
        tower.setDamage(BulletTower.MAX_DAMAGE * 3);
        tower.setBulletSpeed(400);
        tower.setReloadTime(1200);
        tower.setImageId(TOWER1_TEXTURE_ID);

        return tower;
      }

      case RAY: {
        final int range = 100;
        RayTower tower = new RayTower(towerCentre, range);
        tower.setCost(90);
        tower.setDamagePerSec(120);
        tower.setImageId(TOWER2_TEXTURE_ID);

        return tower;
      }

      case AREA: {
        final int range = 90;
        BlastTower tower = new BlastTower(towerCentre, range);
        tower.setCost(110);
        tower.setBlastDamage(BulletTower.MAX_DAMAGE * 4);
        tower.setReloadTime(2200);
        tower.setImageId(TOWER3_TEXTURE_ID);

        return tower;
      }

      default:
        throw new RuntimeException("Invalid TowerType: " + type);
    }
  }
}

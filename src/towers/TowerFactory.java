package towers;

import core.BulletManager;
import core.ImageStore;

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
    switch (type) {
      case PROJECTILE: {
        final int range = 120;
        BulletTower tower = new BulletTower(x, y, range, bulletManager);
        tower.setCost(50);
        tower.setDamage(BulletTower.MAX_DAMAGE * 3);
        tower.setBulletSpeed(400);
        tower.setReloadTime(1200);
        tower.setTextureId(TOWER1_TEXTURE_ID);

        return tower;
      }

      case RAY: {
        final int range = 100;
        RayTower tower = new RayTower(x, y, range);
        tower.setCost(90);
        tower.setDamagePerSec(120);
        tower.setTextureId(TOWER2_TEXTURE_ID);

        return tower;
      }

      case AREA: {
        final int range = 90;
        BlastTower tower = new BlastTower(x, y, range);
        tower.setCost(110);
        tower.setBlastDamage(BulletTower.MAX_DAMAGE * 4);
        tower.setReloadTime(2200);
        tower.setTextureId(TOWER3_TEXTURE_ID);

        return tower;
      }

      default:
        throw new RuntimeException("Invalid TowerType: " + type);
    }
  }
}

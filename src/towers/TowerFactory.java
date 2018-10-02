package towers;

import core.BulletManager;

public class TowerFactory {

    public static final String RESOURCE_DIRECTORY_PATH = "resources/";
    public static final String FILEPATH_TOWER1 = RESOURCE_DIRECTORY_PATH + "tower1.png";
    public static final String FILEPATH_TOWER2 = RESOURCE_DIRECTORY_PATH + "tower2.png";
    public static final String FILEPATH_TOWER3 = RESOURCE_DIRECTORY_PATH + "tower3.png";

    private BulletManager _bulletManager;

    public TowerFactory(BulletManager bulletManager) {
        _bulletManager = bulletManager;
    }

    public Tower createTower(int x, int y, TowerType type) {
        Tower newTower;

        switch (type) {
            case PROJECTILE: {
                final int range = 120;
                BulletTower tower = new BulletTower(x, y, range, _bulletManager);
                tower.setCost(50);
                tower.setDamage(BulletTower.MAX_DAMAGE * 2);
                tower.setBulletSpeed(500);
                tower.setReloadTime(500);
                tower.loadTexture(FILEPATH_TOWER1);

                newTower = tower;
                break;
            }

            case RAY: {
                final int range = 100;
                RayTower tower = new RayTower(x, y, range);
                tower.setCost(90);
                tower.setDamagePerSec(120);
                tower.loadTexture(FILEPATH_TOWER2);

                newTower = tower;
                break;
            }

            case AREA: {
                final int range = 90;
                BlastTower tower = new BlastTower(x, y, range);
                tower.setCost(110);
                tower.setBlastDamage(BulletTower.MAX_DAMAGE * 3);
                tower.setReloadTime(2200);
                tower.loadTexture(FILEPATH_TOWER3);

                newTower = tower;
                break;
            }

            default:
                throw new RuntimeException("Invalid TowerType: " + type);
        }
        return newTower;
    }
}

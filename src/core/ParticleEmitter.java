package core;

import towers.BasicElementType;
import towers.CompoundElementType;
import towers.ElementProperties;
import towers.RayTower;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class ParticleEmitter extends Entity {

  private String imageId;
  private List<StreamParticle> particles = new ArrayList<>();
  private RayTower owningTower;

  private final int rate = 80;
  private int timeSinceSpawn = 0;

  public ParticleEmitter(RayTower owningTower, ElementProperties element) {
    super(owningTower.getCentre());

    this.owningTower = owningTower;

    // Load the texture based on the element type
    CompoundElementType dualType = element.getCompoundType();
    if (dualType != null) {
      loadDualTypeImage(dualType);
    } else {
      BasicElementType basicType = element.getBasicType();
      if (basicType != null) {
        loadBasicTypeImage(basicType);
      }
    }
  }

  private void loadBasicTypeImage(BasicElementType basicType) {
    ImageStore images = ImageStore.getInstance();
    switch (basicType) {
      case FIRE:
        imageId = images.loadImage("particle_flame.png");
        break;
      case WATER:
        imageId = images.loadImage("particle_water.png");
        break;
      case EARTH:
        imageId = images.loadImage("particle_earth.png");
        break;
    }
  }

  private void loadDualTypeImage(CompoundElementType dualType) {
    ImageStore images = ImageStore.getInstance();
    switch (dualType) {
      case LAVA:
        imageId = images.loadImage("particle_lava.png");
        break;
      case STEAM:
        imageId = images.loadImage("particle_steam.png");
        break;
      case MUD:
        imageId = images.loadImage("particle_mud.png");
        break;
    }
  }

  @Override
  public void update(long elapsed) {

    // Check to see if a new particle should be emitted
    Enemy target = owningTower.getEnemyTarget();
    if (isTargetAlive(target)) {
      timeSinceSpawn += elapsed;
      if (timeSinceSpawn >= rate) {
        timeSinceSpawn -= rate;
        StreamParticle particle = new StreamParticle(new Point(position), target, imageId);
        particles.add(particle);
      }
    }

    particles.forEach(particle -> particle.update(elapsed));
    particles.removeIf(StreamParticle::isDead);
  }

  @Override
  public void draw(Graphics2D g) {
    particles.stream().forEach(particle -> particle.draw(g));
  }

  private boolean isTargetAlive(Enemy target) {
    return target != null && target.isAlive();
  }
}

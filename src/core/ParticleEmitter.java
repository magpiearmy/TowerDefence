package core;

import towers.BasicElementType;
import towers.CompoundElementType;
import towers.ElementProperties;
import towers.RayTower;

import java.awt.*;
import java.util.Vector;

public class ParticleEmitter {

  private String imgId;
  private Point pos;
  private Vector<StreamParticle> particles;
  private RayTower owningTower;

  final int rate = 80;
  int timeSinceSpawn = 0;

  public ParticleEmitter(Point pos, RayTower owningTower, ElementProperties element) {
    this.pos = pos;
    this.owningTower = owningTower;
    particles = new Vector<>();

    ImageStore images = ImageStore.getInstance();

    // Load the texture based on the element type
    CompoundElementType dualType = element.getCompoundType();
    if (dualType != null) {

      switch (dualType) {
        case LAVA:
          imgId = images.loadImage("particle_lava.png");
          break;
        case STEAM:
          imgId = images.loadImage("particle_steam.png");
          break;
        case MUD:
          imgId = images.loadImage("particle_mud.png");
          break;
      }

    } else { // No dual-type so check for basic type

      BasicElementType basicType = element.getBasicType();
      if (basicType != null) {
        switch (basicType) {
          case FIRE:
            imgId = images.loadImage("particle_flame.png");
            break;
          case WATER:
            imgId = images.loadImage("particle_water.png");
            break;
          case EARTH:
            imgId = images.loadImage("particle_earth.png");
            break;
        }
      }
    }
  }

  public Point getPos() {
    return pos;
  }

  public void update(long elapsed) {

    // Check to see if a new particle should be emitted
    Enemy target = owningTower.getEnemyTarget();
    if (isTargetAlive(target)) {
      timeSinceSpawn += elapsed;
      if (timeSinceSpawn >= rate) {
        timeSinceSpawn -= rate;
        StreamParticle particle = new StreamParticle(new Point(pos), target);
        particle.init(imgId);
        particles.add(particle);
      }
    }

    particles.forEach(particle -> particle.update(elapsed));
    particles.removeIf(StreamParticle::isDead);
  }

  private boolean isTargetAlive(Enemy target) {
    return target != null && target.isAlive();
  }

  public void drawParticles(Graphics2D gfx) {
    for (StreamParticle particle : particles) {
      if (!particle.isDead()) {
        particle.draw(gfx);
      }
    }
  }
}

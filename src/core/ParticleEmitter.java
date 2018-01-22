package core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import towers.BasicElementType;
import towers.CompoundElementType;
import towers.RayTower;
import towers.ElementProperties;

public class ParticleEmitter {

	private String imgId;
	private Point pos;
	private Vector<StreamParticle> particles;
	private RayTower owningTower;
	
	final int rate = 70;
	int elapsed = 0;

	public ParticleEmitter(Point pos, RayTower owningTower, ElementProperties element) {
		this.pos = pos;
		this.owningTower = owningTower;
		particles = new Vector<>();
		
		ImageStore images = ImageStore.get();

		// Load the texture based on the element type
		CompoundElementType dualType = element.getCompoundType();
		if (dualType != null) {
			
			switch(dualType) {
			case LAVA:	imgId = images.loadImage("particle_lava.png"); break;
			case STEAM:	imgId = images.loadImage("particle_steam.png"); break;
			case MUD: 	imgId = images.loadImage("particle_mud.png"); break;
			}
			
		} else { // No dual-type so check for basic type
			
			BasicElementType basicType = element.getBasicType();
			if (basicType != null) {
				switch (basicType) {
				case FIRE:	imgId = images.loadImage("particle_flame.png"); break;
				case WATER:	imgId = images.loadImage("particle_water.png"); break;
				case EARTH:	imgId = images.loadImage("particle_earth.png"); break;
				}
			}
		}
	}
	
	public Point getPos() {
		return pos;
	}
	
	public void update(long elapsed) {
		
		// Update the particles
		for (int i = 0; i < particles.size(); i++) {
			StreamParticle particle = particles.elementAt(i);
			particle.update(elapsed);
			if (particle.isDead())	
				;//particles.remove(i--);
		}
		
		// Check to see if a new particle should be emitted
		Enemy target = owningTower.getEnemyTarget();
		if (target != null) {
			this.elapsed += elapsed;
			if (this.elapsed >= rate) {
				this.elapsed -= rate;
				StreamParticle particle = new StreamParticle(this, target);
				particle.init(imgId);
				particles.add(particle);
			}
		}
	}
	
	public void drawParticles(Graphics2D gfx) {
		for (StreamParticle particle : particles) {
			if (!particle.isDead())
				particle.draw(gfx);
		}
		return;
	}
}

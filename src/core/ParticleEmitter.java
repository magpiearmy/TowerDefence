package core;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Vector;

import towers.BasicElementType;
import towers.CompoundElementType;
import towers.RayTower;
import towers.ElementProperties;

public class ParticleEmitter {

	private String _imgId;
	private Point _pos;
	private Vector<StreamParticle> _particles;
	private RayTower _owningTower;
	
	final int _rate = 70;
	int _elapsed = 0;

	public ParticleEmitter(Point pos, RayTower owningTower, ElementProperties element) {
		_pos = pos;
		_owningTower = owningTower;
		_particles = new Vector<StreamParticle>();
		
		ImageStore images = ImageStore.get();

		// Load the texture based on the element type
		CompoundElementType dualType = element.getCompoundType();
		if (dualType != null) {
			
			switch(dualType) {
			case LAVA:	_imgId = images.loadImage("particle_lava.png"); break;
			case STEAM:	_imgId = images.loadImage("particle_steam.png"); break;
			case MUD: 	_imgId = images.loadImage("particle_mud.png"); break;
			}
			
		} else { // No dual-type so check for basic type
			
			BasicElementType basicType = element.getBasicType();
			if (basicType != null) {
				switch (basicType) {
				case FIRE:	_imgId = images.loadImage("particle_flame.png"); break;
				case WATER:	_imgId = images.loadImage("particle_water.png"); break;
				case EARTH:	_imgId = images.loadImage("particle_earth.png"); break;
				}
			}
		}
	}
	
	public Point getPos() {
		return _pos;
	}
	
	public void update(long elapsed) {
		
		// Update the particles
		for (int i = 0; i < _particles.size(); i++) {
			StreamParticle particle = _particles.elementAt(i);
			particle.update(elapsed);
			if (particle.isDead())	
				;//_particles.remove(i--);
		}
		
		// Check to see if a new particle should be emitted
		Enemy target = _owningTower.getEnemyTarget();
		if (target != null) {
			_elapsed += elapsed;
			if (_elapsed >= _rate) {
				_elapsed -= _rate;
				StreamParticle particle = new StreamParticle(this, target);
				particle.init(_imgId);
				_particles.add(particle);
			}
		}
	}
	
	public void drawParticles(Graphics2D gfx) {
		for (StreamParticle particle : _particles) {
			if (!particle.isDead())
				particle.draw(gfx);
		}
		return;
	}
}

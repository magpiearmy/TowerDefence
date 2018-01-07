package core;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnimatedSprite {
	private enum State {
		NOT_ACTIVE, WARMING_UP, ANIMATING, COOLING_DOWN
	};
	private State	_state;
	
	// Image data
	private BufferedImage	_sheet;
	private int				_frameWidth		= 0;
	private int				_frameHeight	= 0;
	private int				_rowCount		= 0;
	private int				_framesPerRow	= 0;
	
	// Animation data
	private final int	_warmUpTime		= 120;	// ms
	private final int	_coolDownTime	= 300;	// ms
	private int			_phaseTime		= 0;	// ms
	private int			_frame			= 0;

	public AnimatedSprite(String fileName, int frameWidth, int frameHeight) {
		try {
			_sheet = ImageIO.read(new File("Res\\" + fileName));
		}
		catch (IOException e) {
		}

		_frameWidth = frameWidth;
		_frameHeight = frameHeight;
		_framesPerRow = _sheet.getWidth() / frameWidth;
		_rowCount = _sheet.getHeight() / frameHeight;
		_state = State.NOT_ACTIVE;
	}

	public void update(long elapsed) {
		
		switch(_state) {
		case WARMING_UP: {
			final int maxFrames = 7;
			final int timePerFrame = _warmUpTime/maxFrames;
			
			_phaseTime += elapsed;
			
			if (_phaseTime >= _warmUpTime) {
				_state = State.ANIMATING;
				_phaseTime = 0;
			}
			else {
				_frame = (_phaseTime / timePerFrame) % maxFrames;
			}
			break;
		}
		case ANIMATING: {
			final int maxFrames = 11;
			final int timePerFrame = 20;
			
			_phaseTime += elapsed;
			_frame = 7 + ((_phaseTime / timePerFrame) % maxFrames);
			
			break;
		}
		case COOLING_DOWN:
			final int maxFrames = 7;
			final int timePerFrame = _coolDownTime/maxFrames;
			
			_phaseTime += elapsed;
			
			if (_phaseTime >= _coolDownTime) {
				_state = State.NOT_ACTIVE;
			} else {
				_frame = 18 + ((_phaseTime / timePerFrame) % maxFrames);
			}
			break;
		default: break;
		}
	}
	
	public boolean isActive() {
		return _state != State.NOT_ACTIVE;
	}
	
	/**
	 * Activation starts the "warm up" animation sequence
	 */
	public void activate() {
		_state = State.WARMING_UP;
		_phaseTime = 0;
	}
	
	/**
	 * Deactivation starts the "cool down" animation sequence
	 */
	public void deactivate() {
		_state = State.COOLING_DOWN;
		_phaseTime = 0;
	}
	
	public void draw(Graphics2D gfx, int startX, int startY, double rotation) {
		
		final int x = (_frame % _framesPerRow) * _frameWidth;
		final int y = (int)(Math.floor(_frame / _framesPerRow)) * _frameHeight;
		
		BufferedImage img = _sheet.getSubimage(x, y, _frameWidth, _frameHeight);
		AffineTransform xform = new AffineTransform();
		xform.rotate(rotation, _frameWidth/2, _frameHeight/2);
		AffineTransformOp op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		
		int radius = _frameWidth/2;
		int baseX = (int) (startX + radius * Math.cos(rotation+Math.PI/2));
		int baseY = (int) (startY + radius * Math.sin(rotation+Math.PI/2));
		
		int drawX = (startX - _frameWidth/2) + (startX - baseX);
		int drawY = (startY - _frameHeight/2) + (startY - baseY);
		gfx.drawImage(img, drawX, drawY, null);
	}
}

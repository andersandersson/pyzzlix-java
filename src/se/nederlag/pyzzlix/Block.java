package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Block extends Sprite {
	private int boardX;
	private int boardY;
	private int offsetX;
	private int offsetY;
	private int type;
	
	private Animation blinkAnimation;
	private Animation pulseAnimation;
	private Animation normalAnimation;
	
	private static Texture texture = new Texture(Gdx.files.internal("data/blocks.png")); 
	
	public Block(int boardx, int boardy, int type, int offsetx, int offsety)
	{
		super(texture);
		
		this.boardX = boardx;
		this.boardY = boardy;
		this.type = type;
		this.offsetX = offsetx;
		this.offsetY = offsety;
		
		this.blinkAnimation = new Animation(this, 16, 16, type*16, 0, 16, 6*16, 0.0, 0.016, "pingpongloop", false);
		this.pulseAnimation = new Animation(this, 16, 16, type*16, 0, 16, 6*16, 0.0, 0.016, "pingpong", false);
		this.normalAnimation = new Animation(this, 16, 16, type*16, 0, 16, 16, 0.0, 0.2, "normal", false);
		
		this.setAnimation(blinkAnimation);
		this.setSize(32, 32);
		this.setPos(new Point(offsetX+boardX*32, offsetY+boardY*32));
		this.setCenter(new Point(16, 16));
	}
	
	public void doPulse() {
		this.setAnimation(pulseAnimation);
	}

	public void doBlink() {
		this.setAnimation(blinkAnimation);
	}

	public void doNormal() {
		this.setAnimation(normalAnimation);
	}
	
	public void moveToBoardCoord(int boardx, int boardy, double currentTime) {
		this.boardX = boardx;
		this.boardY = boardy;
	}

	public void setToBoardCoord(int boardx, int boardy) {		
	}

	public void animatePopup(double currentTime) {		
	}
}

package se.nederlag.pyzzlix;

import java.util.EnumSet;

import se.nederlag.pyzzlix.Animation.Mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.Map;

public class Block extends Sprite {
	public enum Status {
		MOVING, IN_CIRCLE, OFFSCREEN, WEIGHTLESS;
	}

	private int boardX;
	private int boardY;
	private int offsetX;
	private int offsetY;
	private int type;
	private int width;
	private int height;
	private Map<Status,Boolean> status;
	private int comboCounter;
	private int gravityDelay;
	
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
		this.type = type;
		this.status = new HashMap<Status,Boolean>();
                for(Status s : Status.values()) {
                    this.status.put(s, false);
                }
		this.comboCounter = 0;
		this.gravityDelay = 0;
		
		this.blinkAnimation = new Animation(this, 16, 16, type*16, 0, 16, 6*16, 0.0, 0.016, Animation.Mode.PINGPONGLOOP, false);
		this.pulseAnimation = new Animation(this, 16, 16, type*16, 0, 16, 6*16, 0.0, 0.02, Animation.Mode.NORMAL, true);
		this.normalAnimation = new Animation(this, 16, 16, type*16, 0, 16, 16, 0.0, 0.2, Animation.Mode.NORMAL, false);
		
		this.setAnimation(normalAnimation);
		this.setSize(16, 16);
		
		this.width = 16;
		this.height = 16;
		
		this.setPos(new Point(offsetX+boardX*16, offsetY-boardY*16));
		this.setCenter(new Point(8, 8));
	}
	
	public boolean addStatus(Status status) {
		return this.status.put(status, true);
	}
	
	public boolean hasStatus(Status status) {
		return this.status.get(status);
	}
	
	public boolean removeStatus(Status status) {
		return this.status.put(status, false);
	}
	
	public void setComboCounter(int comboCounter) {
		this.comboCounter = comboCounter;
	}
	
	public int getComboCounter() {
		return this.comboCounter;
	}
	
	public void incrementComboCounter() {
		this.comboCounter += 1;
	}

	public void resetComboCounter() {
		this.comboCounter = 0;
	}

	public int getGravityDelay() {
		return this.gravityDelay;
	}
	
	public void decrementGravityDelay() {
		this.gravityDelay -= 1;
	}

	public void setGravityDelay(int gravityDelay) {
		this.gravityDelay = gravityDelay;
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
		this.moveTo(new Point(boardX * width + offsetX, -boardY * height + offsetY), currentTime, 0.15, null);
	}

	public void setToBoardCoord(int boardx, int boardy) {		
		this.boardX = boardx;
		this.boardY = boardy;
		this.setPos(new Point(boardX * width + offsetX, -boardY * height + offsetY));
    }

	public void animatePopup(double currentTime) {		
        this.setCol(new Color(0.1f, 0.1f, 0.1f, 1.0f));
        this.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), currentTime, 1.0, null);
        this.setScale(new Point(0.0, 0.0));
        this.scaleTo(new Point(1.0, 1.0), currentTime, 0.15, null);
        this.setRot(180.0);
        this.rotateTo(0, currentTime, 0.3, null);
	}

	public int getType() {
		return this.type;
	}

	public int getBoardX() {
		return this.boardX;
	}

	public int getBoardY() {
		return this.boardY;
	}
}

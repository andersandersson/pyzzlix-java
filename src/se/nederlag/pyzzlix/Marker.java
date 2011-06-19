package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Marker extends Sprite {
	public double offsetX;
	public double offsetY;

	private int boardX;
	private int boardY;
	
	private double scaleX;
	private double scaleY;
	
	public Marker() {
		super(new Texture(Gdx.files.internal("data/marker.png")));
		
		this.boardX = 0;
		this.boardY = 0;
		this.offsetX = 0;
		this.offsetY = 0;
		this.scaleX = 16;
		this.scaleY = 16;
		
		this.setAnimation(new Animation(this, 32, 32, 0, 0, 32, 96, 0.0, 0.2, Animation.Mode.LOOP, false));		
		this.setPos(new Point(offsetX+boardX*32, offsetY+boardY*32));
		this.setSize(32, 32);
	}

	public void moveToBoardCoord(int boardx, int boardy, double currentTime) {
		this.boardX = boardx;
		this.boardY = boardy;
		this.moveTo(new Point(boardX * scaleX + offsetX, boardY * scaleY + offsetY), currentTime, 0.15, null);
	}

	public void move(int dx, int dy, double currentTime) {
		this.moveToBoardCoord(boardX+dx, boardY+dy, currentTime);
	}
	
	public void turn() {
	}

	public void fail() {
	}
}

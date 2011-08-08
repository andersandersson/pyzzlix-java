package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.audio.Audio;
import se.nederlag.pyzzlix.audio.Sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Marker extends Sprite {
	public double offsetX;
	public double offsetY;

	private int boardX;
	private int boardY;
	
	private double scaleX;
	private double scaleY;

	private Sound movesound;
	private Sound turnsound;
	private Sound failsound;
	
	public Marker() {
		super(new Texture(Gdx.files.internal("data/marker.png")));
		
		this.boardX = 0;
		this.boardY = 0;
		this.offsetX = 0;
		this.offsetY = 0;
		this.scaleX = 16;
		this.scaleY = 16;
		
		this.setAnimation(new Animation(this, 32, 32, 0, 0, 32, 96, 0.0, 0.2, Animation.Mode.LOOP, false));		
		this.setPos(new Point(offsetX+boardX*scaleX, offsetY-boardY*scaleX));
		this.setSize(32, 32);
		
		this.movesound = Resources.getSound("markermove");
		this.turnsound = Resources.getSound("markerturn");
		this.failsound = Resources.getSound("markerfail");
	}

	public void moveToBoardCoord(int boardx, int boardy, double currentTime) {
		this.boardX = boardx;
		this.boardY = boardy;
		//this.moveTo(new Point(boardX * scaleX + offsetX, boardY * scaleY + offsetY), currentTime, 0.15, null);
		this.setPos(new Point(boardX * scaleX + offsetX, offsetY - boardY*scaleY));
	}

	public void move(int dx, int dy, double currentTime) {
		Audio.playSound(this.movesound);
		this.moveToBoardCoord(boardX+dx, boardY+dy, currentTime);
	}
	
	public void turn() {
		Audio.playSound(this.turnsound);
	}

	public void fail() {
		Audio.playSound(this.failsound);
	}

	public int getBoardX() {
		return this.boardX;
	}
	
	public int getBoardY() {
		return this.boardY;
	}	
}

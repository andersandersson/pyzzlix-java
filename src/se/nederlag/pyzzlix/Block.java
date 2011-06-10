package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Block extends Sprite {
	private int boardX;
	private int boardY;
	private int offsetX;
	private int offsetY;
	private int type;
	
	public Block(int boardx, int boardy, int type, int offsetx, int offsety)
	{
		this.boardX = boardx;
		this.boardY = boardy;
		this.type = type;
		this.offsetX = offsetx;
		this.offsetY = offsety;
		
		Texture image = new Texture(Gdx.files.internal("data/sprite.png"));
		this.setTexture(image);
	}
}

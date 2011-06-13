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
	
	public Block(int boardx, int boardy, int type, int offsetx, int offsety)
	{
		super(new Texture(Gdx.files.internal("data/blocks.png")));
		
		this.boardX = boardx;
		this.boardY = boardy;
		this.type = type;
		this.offsetX = offsetx;
		this.offsetY = offsety;
		
		this.setAnimation(new Animation(this, 16, 16, 0, 0, 16, 5*16, 0, 0.05, "pingpongloop", false));
		this.setSize(16, 16);
	}
}

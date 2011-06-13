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
	
	private static Texture texture = new Texture(Gdx.files.internal("data/blocks.png")); 
	
	public Block(int boardx, int boardy, int type, int offsetx, int offsety)
	{
		super(texture);
		
		this.boardX = boardx;
		this.boardY = boardy;
		this.type = type;
		this.offsetX = offsetx;
		this.offsetY = offsety;
		
		this.setAnimation(new Animation(this, 16, 16, type*16, 0, 16, 1*16, 0, 0.02, "pingpongloop", false));
		this.setSize(32, 32);
		this.setPos(new Point(offsetX+boardX*32, offsetY+boardY*32));
	}
}

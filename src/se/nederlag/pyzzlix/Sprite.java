package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite{
	private List<Sprite> subSprites = new LinkedList<Sprite>();
	private Animation currentAnimation = null;
		
	public Sprite() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sprite(Sprite sprite) {
		super(sprite);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture) {
		super(texture);
		// TODO Auto-generated constructor stub
	}

	public Sprite(TextureRegion region, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(TextureRegion region) {
		super(region);
		// TODO Auto-generated constructor stub
	}

	public List<Sprite> getSubSprites() {
		return subSprites;
	}

	public void setAnimation(Animation animation)
	{
		currentAnimation = animation;
	}
	
	public void update(double currenttime)
	{
		if(currentAnimation != null) 
		{
			currentAnimation.updateFrame(currenttime);
		}
		
		for(Sprite sprite : subSprites)
		{
			sprite.update(currenttime);
		}
	}
}

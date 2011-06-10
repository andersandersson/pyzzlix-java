package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public abstract class Sprite {
	private List<Sprite> subSprites = new LinkedList<Sprite>();
	private com.badlogic.gdx.graphics.g2d.Sprite image = new com.badlogic.gdx.graphics.g2d.Sprite();
	
	public List<Sprite> getSubSprites() {
		return subSprites;
	}

	public void update()
	{
	}
	
	public void setTexture(Texture texture) {
		image.set(new com.badlogic.gdx.graphics.g2d.Sprite(texture));
	}
	
	public void setImage(com.badlogic.gdx.graphics.g2d.Sprite image) {
		this.image.set(image);
	}

	public com.badlogic.gdx.graphics.g2d.Sprite getImage() {
		return image;
	}
}

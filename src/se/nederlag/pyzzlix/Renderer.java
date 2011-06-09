package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum Renderer {
	INSTANCE;

	static final int SPRITES = 400000;

	Texture image;
	Sprite[] sprites;	
	SpriteBatch batch;
	
	private Renderer()
	{
		image = new Texture(Gdx.files.internal("data/sprite.png")); 												  
		
		sprites = new Sprite[SPRITES];
		for(int i = 0; i < SPRITES; i++) {
			Sprite sprite = new Sprite(image);
			sprite.setPosition((float)Math.random() * 800, (float)Math.random() * 600);
			sprites[i] = sprite;
		}
 
		batch = new SpriteBatch();		
	}
	
	public void render(float deltatime)
	{		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		 
		float scale = 1.0f - deltatime;		
 
		batch.begin();
		batch.enableBlending();
		for(int i = 0; i < SPRITES; i++) {				
			sprites[i].setRotation(sprites[i].getRotation() + deltatime * 45);			
			sprites[i].setScale(scale, scale);
			sprites[i].draw(batch);			
		}
		batch.end();			
	}
}

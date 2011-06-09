package se.nederlag.pyzzlix;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pyzzlix implements ApplicationListener  {
	static final int SPRITES = 400000;
	Texture image;
	Sprite[] sprites;	
	SpriteBatch batch;
	float timePassed = 0;
	int frames = 0;
	
	public void pause() {
		
	
	}
 
	public void resume() {
			
	}
	
	public void dispose() {
	
			
	}
	
	
	public void create() {	
		image = new Texture(Gdx.files.internal("data/sprite.png")); 												  
		
		sprites = new Sprite[SPRITES];
		for(int i = 0; i < SPRITES; i++) {
			Sprite sprite = new Sprite(image);
			sprite.setPosition((float)Math.random() * 800, (float)Math.random() * 600);
			sprites[i] = sprite;
		}
 
		batch = new SpriteBatch();		
	}
 
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		float elapsed = Gdx.graphics.getDeltaTime();
		float scale = timePassed>0.5?1-timePassed / 2:0.5f + timePassed / 2;
 
		batch.begin();
		batch.enableBlending();
		for(int i = 0; i < SPRITES; i++) {				
			sprites[i].setRotation(sprites[i].getRotation() + elapsed * 45);			
			sprites[i].setScale(scale, scale);
			sprites[i].draw(batch);			
		}
		batch.end();			
 
		timePassed += elapsed;
		frames++;
		if(timePassed > 1.0f) {
			Gdx.app.log("SpritePerformanceTest2", "fps: " + frames);
			timePassed = 0;
			frames = 0;
		}
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

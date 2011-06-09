package se.nederlag.pyzzlix;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Pyzzlix implements ApplicationListener  {
	static final int SPRITES = 400000;
	static final int LOGICS_PER_SEC = 20;
	
	Texture image;
	Sprite[] sprites;	
	SpriteBatch batch;

	int fpsCounter = 0;	

	float time = 0.0f;
	float nextUpdateTime = 0.0f;
	float lastRenderTime = 0.0f;
	float lastFpsUpdate = 0.0f;
	float logicLength = 1.0f / LOGICS_PER_SEC;
	
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
		float elapsed = Gdx.graphics.getDeltaTime();

		time += elapsed; 

		if(time - lastFpsUpdate >= 1.0f)
		{
			Gdx.app.log("SpritePerformanceTest2", "fps: " + fpsCounter);
			fpsCounter = 0;
			lastFpsUpdate = time;
		}
		
		if(time > nextUpdateTime)
		{
			while(time > nextUpdateTime)
			{
				nextUpdateTime += logicLength;
				
				SceneHandler.INSTANCE.doSceneTicks();
				Renderer.INSTANCE.render(0);
				lastRenderTime = time;
				fpsCounter++;
			}
		}
		else
		{
			Renderer.INSTANCE.render(time - lastRenderTime);
			lastRenderTime = time;
			fpsCounter++;
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
 
		float scale = (float)(Math.sin(elapsed*Math.PI)*Math.sin(elapsed*Math.PI));
 
		batch.begin();
		batch.enableBlending();
		for(int i = 0; i < SPRITES; i++) {				
			sprites[i].setRotation(sprites[i].getRotation() + elapsed * 45);			
			sprites[i].setScale(scale, scale);
			sprites[i].draw(batch);			
		}
		batch.end();			
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

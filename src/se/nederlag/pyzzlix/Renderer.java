package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Renderer {

	static final int SPRITES = 400000;
	static Renderer instance = null;
	
	double currentTime = 0.0;
	double deltaT = 0.0;
	
	Texture image;
	Sprite[] sprites;	
	SpriteBatch batch;
	
	private Renderer()
	{
		batch = new SpriteBatch();		
	}

	public static Renderer getInstance() 
	{
		if (instance == null)
			instance = new Renderer();
		return instance;
	}

	public void drawSprite(Sprite sprite, double currenttime)
	{
		Point pos = sprite.calcPos(currenttime);
		sprite.setPosition((float)pos.x, (float)pos.y);
		sprite.draw(batch);
	}
	
	public void render(double deltatime)
	{		
		deltaT = deltatime;
		currentTime += deltaT;
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);	 
		batch.begin();
		batch.enableBlending();

		SceneHandler.getInstance().renderScenes();

		batch.end();			
	}

	public void renderScene(Scene scene) {
		for(Sprite sprite : scene.getSprites()) {
			drawSprite(sprite, currentTime);
		}
	}
}

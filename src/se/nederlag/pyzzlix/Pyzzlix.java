package se.nederlag.pyzzlix;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class Pyzzlix implements ApplicationListener  {
	static final int LOGICS_PER_SEC = 20;
	
	int fpsCounter = 0;	

	float time = 0.0f;
	float nextUpdateTime = 0.0f;
	float lastRenderTime = 0.0f;
	float lastFpsUpdate = 0.0f;
	float logicLength = 1.0f / LOGICS_PER_SEC;
	
	public Pyzzlix() {
	}
	
	public void pause() {
	}
 
	public void resume() {
	}
	
	public void dispose() {
	}
	
	public void create() {
		SceneHandler.getInstance().pushScene(Scene_MainGame.getInstance());
	}
 
	public void resize(int width, int height) {
		//batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	
	public void render() {
		time += Gdx.graphics.getDeltaTime(); 

		if(time - lastFpsUpdate >= 1.0f)
		{
			Gdx.app.log("SpritePerformanceTest2", "fps: " + fpsCounter);
			fpsCounter = 0;
			lastFpsUpdate = time;
		}
		
		if(time > nextUpdateTime)
		{
			nextUpdateTime += logicLength;
			SceneHandler.getInstance().doSceneTicks();
			
			Renderer.getInstance().render(0);
			lastRenderTime = time;
			fpsCounter++;
		}
		else
		{
			Renderer.getInstance().render(time - lastRenderTime);
			lastRenderTime = time;
			fpsCounter++;
		}
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

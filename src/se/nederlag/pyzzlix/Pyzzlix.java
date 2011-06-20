package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Pyzzlix implements ApplicationListener  {
	static final int LOGICS_PER_SEC = 20;
	
	int fpsCounter = 0;	

	double time = 0.0f;
	double nextUpdateTime = 0.0f;
	double lastRenderTime = 0.0f;
	double lastFpsUpdate = 0.0f;
	double logicLength = 1.0f / LOGICS_PER_SEC;
	
	InputHandler inputHandler;
	
	public Pyzzlix() {
	}
	
	public void pause() {
	}
 
	public void resume() {
	}
	
	public void dispose() {
	}
	
	public void create() {
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		//SceneHandler.getInstance().pushScene(Scene_MainMenu.getInstance());
		SceneHandler.getInstance().pushScene(Scene_MainGame.getInstance());
		
	}
 
	public void resize(int width, int height) {		
		Renderer.getInstance().resize(width, height);
	}
	
	public void render() {
		time += Gdx.graphics.getDeltaTime(); 

		if(time - lastFpsUpdate >= 1.0f)
		{
			Gdx.app.log("SpritePerformanceTest2", "fps: " + fpsCounter);
			fpsCounter = 0;
			lastFpsUpdate = time;
		}
		
		if(time >= nextUpdateTime + logicLength)
		{
			nextUpdateTime += logicLength;

			while(EventHandler.getInstance().peek() != null) {
				Event event = EventHandler.getInstance().poll();
				SceneHandler.getInstance().handleEvent(event);
			}
			
			SceneHandler.getInstance().updateTimers(logicLength);
			SceneHandler.getInstance().doSceneTicks();
			
		}

		Renderer.getInstance().render(time - lastRenderTime);
		Mixer.getInstance().update(time - lastRenderTime);
		lastRenderTime = time;
		fpsCounter++;
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

package se.nederlag.pyzzlix;

import java.util.Locale;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Pyzzlix implements ApplicationListener  {
	static final int LOGICS_PER_SEC = 30;
	
	int fpsCounter = 0;	

	double time = 0.0f;
	double nextUpdateTime = 0.0f;
	double lastRenderTime = 0.0f;
	double lastFpsUpdate = 0.0f;
	double logicLength = 1.0f / LOGICS_PER_SEC;
	
	InputHandler inputHandler;
	
	//private FrameBuffer fbo;
	
	public Pyzzlix() {
	}
	
	public void pause() {
	}
 
	public void resume() {
	}
	
	public void dispose() {
		//this.fbo.dispose();
	}
	
	public void create() {
		Locale.setDefault(Locale.US);
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		Gdx.graphics.setVSync(true);
		//SceneHandler.getInstance().pushScene(Scene_DialogYesNo.getInstance());
		SceneHandler.getInstance().pushScene(Scene_Splash.getInstance());
		//SceneHandler.getInstance().pushScene(Scene_MainMenu.getInstance());
		//SceneHandler.getInstance().pushScene(Scene_MainGame.getInstance());
		//SceneHandler.getInstance().pushScene(Scene_Highscore.getInstance());
		//SceneHandler.getInstance().pushScene(Scene_Options.getInstance());
		//SceneHandler.getInstance().pushScene(Scene_Tutorial.getInstance());
		//Scene_Highscore.getInstance().display(true, null, null);
		
		//this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, 512, 512, false);	
	}
 
	public void resize(int width, int height) {		
		Renderer.getInstance().resize(width, height);
	}
	
	public void render() {
		time += Gdx.graphics.getDeltaTime(); 

		if(time - lastFpsUpdate >= 1.0f)
		{
			Gdx.app.log("Pyzzlix", "fps: " + fpsCounter);
			fpsCounter = 0;
			lastFpsUpdate = time;
		}
		
		if(time >= nextUpdateTime + logicLength)
		{
			nextUpdateTime += logicLength;

			while(EventHandler.getInstance().peek() != null) {
				Event event = EventHandler.getInstance().poll();

				if(event.type == Event.Type.EXIT) {
					Gdx.app.exit();
				} else {
					SceneHandler.getInstance().handleEvent(event);
				}
			}
			
			SceneHandler.getInstance().updateTimers(logicLength);
			SceneHandler.getInstance().doSceneTicks();
			
		}

		//this.fbo.begin();
		Renderer.getInstance().render(time - lastRenderTime);
		//this.fbo.end();
		
		Mixer.getInstance().update(time - lastRenderTime);
		lastRenderTime = time;
		fpsCounter++;
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

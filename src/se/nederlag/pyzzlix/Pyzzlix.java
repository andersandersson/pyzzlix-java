package se.nederlag.pyzzlix;

import java.util.Locale;

import org.lwjgl.opengl.Display;

import jsmug.audio.Audio;
import jsmug.audio.OpenALAudio;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Pyzzlix implements ApplicationListener  {
	public enum RunMode {APPLET, DESKTOP};
	
	public static Audio audio = new OpenALAudio();
	
	static final int LOGICS_PER_SEC = 30;
	
	int fpsCounter = 0;	

	double time = 0.0f;
	double nextUpdateTime = 0.0f;
	double lastRenderTime = 0.0f;
	double lastFpsUpdate = 0.0f;
	double logicLength = 1.0f / LOGICS_PER_SEC;
	
	InputHandler inputHandler;
	
	public static RunMode runMode;
	
	//private FrameBuffer fbo;
	
	public Pyzzlix(RunMode runMode) {
		Pyzzlix.runMode = runMode;
	}
	
	public void pause() {
	}
 
	public void resume() {
	}
	
	public void dispose() {
		//Pyzzlix.audio.finish();
		//this.fbo.dispose();
	}
	
	public void create() {
		Locale.setDefault(Locale.US);
		inputHandler = new InputHandler();
		Gdx.input.setInputProcessor(inputHandler);
		Pyzzlix.audio.init();
		Display.setVSyncEnabled(true);
		//Gdx.graphics.setVSync(true);
		
		//SceneHandler.getInstance().pushScene(Scene_DialogYesNo.getInstance());
		if(Pyzzlix.runMode != Pyzzlix.RunMode.APPLET) {
			SceneHandler.getInstance().pushScene(Scene_Splash.getInstance());
		} else {
			SceneHandler.getInstance().pushScene(Scene_MainMenu.getInstance());
		}
		//
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
			Gdx.app.log("Pyzzlix", "parent: " + Display.getParent());
			fpsCounter = 0;
			lastFpsUpdate = time;
		}
		
		if(time >= nextUpdateTime + logicLength)
		{
			nextUpdateTime += logicLength;

			while(EventHandler.getInstance().peek() != null) {
				Event event = EventHandler.getInstance().poll();

				if(event.type == Event.Type.EXIT) {
					System.exit(0);
				} else {
					SceneHandler.getInstance().handleEvent(event);
				}
			}
			
			SceneHandler.getInstance().updateTimers(logicLength);
			SceneHandler.getInstance().doSceneTicks();
			Pyzzlix.audio.update(logicLength);
			
		}

		//this.fbo.begin();
		Renderer.getInstance().render(time - lastRenderTime);
		//this.fbo.end();
		
		lastRenderTime = time;
		fpsCounter++;
	}
 
	public boolean needsGL20 () {
		return false;
	}
}

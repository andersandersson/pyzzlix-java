package se.nederlag.pyzzlix;

import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;

public class SceneHandler {
	private Stack<Scene> sceneStack;
	
	static SceneHandler instance = null;
	
	private SceneHandler() {
		sceneStack = new Stack<Scene>();
	}
	
	public static SceneHandler getInstance() {
		if (instance == null)
			instance = new SceneHandler();
		return instance;
	}
	
	public void pushScene(Scene scene) {
		sceneStack.push(scene);
	}

	public void removeScene(Scene scene) {
		sceneStack.remove(scene);
	}

	public void handleEvent() {		
	}
	
	public void updateTimers(double deltatime) {		
	}
	
	public void renderScenes() {
		Deque<Scene> scenes = new ArrayDeque<Scene>();
		
		for(Scene scene : sceneStack)
		{
			scenes.addLast(scene);
			
			if(scene.isBlockingUpdates())
				break;
		}
		
		for(Scene scene : scenes)
		{
			Renderer.getInstance().renderScene(scene);
		}
	}

	public void doSceneTicks() {
		for(Scene scene : sceneStack)
		{
			scene.tick();
			
			if(scene.isBlockingUpdates())
				break;
		}
	}
}

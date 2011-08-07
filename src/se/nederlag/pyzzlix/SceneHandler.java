package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;

import com.badlogic.gdx.Gdx;

import se.nederlag.pyzzlix.events.Event;

public class SceneHandler {
	private LinkedList<Scene> sceneStack;
	private LinkedList<Scene> scenesToAdd;
	private LinkedList<Scene> scenesToRemove;
	private double currentTime;
	
	static SceneHandler instance = null;
	
	private SceneHandler() {
		sceneStack = new LinkedList<Scene>();
		scenesToAdd = new LinkedList<Scene>();
		scenesToRemove = new LinkedList<Scene>();
	}
	
	public static SceneHandler getInstance() {
		if (instance == null)
			instance = new SceneHandler();
		return instance;
	}
	
	public void pushScene(Scene scene) {
		if(this.scenesToRemove.contains(scene)) {
			this.scenesToRemove.remove(scene);
		}
		
		this.scenesToAdd.add(scene);
		scene.show();
	}

	public void removeScene(Scene scene) {
		if(this.scenesToAdd.contains(scene)) {
			this.scenesToAdd.remove(scene);
		}
		
		this.scenesToRemove.add(scene);
		scene.hide();
	}

	public boolean handleEvent(Event event) {		
		for(Scene scene : sceneStack)
		{
			if(scene.handleEvent(event)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void updateTimers(double deltatime) {		
		currentTime += deltatime;
		boolean blocked = false;
		
		for(Scene scene : sceneStack)
		{
			if(!blocked)
			{
				scene.updateTimer(deltatime);
				scene.blockedUpdate = false;
			}
			else 
			{
				scene.blockedUpdate = true;
			}
			
			if(scene.isBlockingUpdates())
			{
				blocked = true;
			}
		}

		for(Scene scene : this.scenesToRemove) {
			this.sceneStack.remove(scene);
		}

		for(Scene scene : this.scenesToAdd) {
			this.sceneStack.addFirst(scene);
		}
		
		this.scenesToRemove.clear();
		this.scenesToAdd.clear();
	}
	
	public void renderScenes() {
		Deque<Scene> scenes = new ArrayDeque<Scene>();
		
		for(Scene scene : sceneStack)
		{
			scenes.addFirst(scene);
			
			if(scene.isBlockingRendering())
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

		for(Scene scene : this.scenesToRemove) {
			this.sceneStack.remove(scene);
		}

		for(Scene scene : this.scenesToAdd) {
			this.sceneStack.addFirst(scene);
		}
		
		this.scenesToRemove.clear();
		this.scenesToAdd.clear();
	}
}

package se.nederlag.pyzzlix;

import java.util.List;
import java.util.LinkedList;

import se.nederlag.pyzzlix.events.Event;

public abstract class Scene {
	protected boolean renderBlocker = false;
	protected boolean updateBlocker = false;
	protected boolean blockedUpdate = false;
	protected double currentTime = 0;
	protected double renderTime = 0;
	protected boolean done = false;
	
	private List<Sprite> sprites = new LinkedList<Sprite>();	
	private List<Sprite> spritesToAdd = new LinkedList<Sprite>();
	private List<Sprite> spritesToRemove = new LinkedList<Sprite>();
	
    public void preload()
    {
    }
    
    public void updateRenderTime(double deltaTime) 
    {
    	this.renderTime += deltaTime;
    }
    
    public void updateTimer(double deltaTime)
    {
    	this.currentTime += deltaTime;
    	this.renderTime = this.currentTime;
    	
    	for (Sprite sprite : this.sprites)
    	{
    		sprite.update(currentTime);
    	}
		
    	for(Sprite sprite : this.spritesToRemove) {
			this.sprites.remove(sprite);
		}

		for(Sprite sprite : this.spritesToAdd) {
			this.sprites.add(sprite);
		}    

		this.spritesToRemove.clear();
		this.spritesToAdd.clear();
    }
        
	public void addSprite(Sprite sprite) {
		if(this.spritesToRemove.contains(sprite)) {
			this.spritesToRemove.remove(sprite);
		}
		
		this.spritesToAdd.add(sprite);
	}
	
	public void removeSprite(Sprite sprite) {
		if(this.spritesToAdd.contains(sprite)) {
			this.spritesToAdd.remove(sprite);
		}

		this.spritesToRemove.add(sprite);
	}
	

	public void handleEvent()
    {
    	
    }

    public void hide()
    {
    	
    }

    public void show()
    {
    	
    }

    public boolean isDone()
    {
    	return done;
    }

    public boolean isBlockingUpdates()
    {
    	return updateBlocker;
    }
    
    public boolean isBlockingRendering()
    {
    	return renderBlocker;
    }

    public List<Sprite> getSprites() {
		return sprites;
	}

	public boolean handleEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void tick() {
	}
}

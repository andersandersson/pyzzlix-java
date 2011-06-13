package se.nederlag.pyzzlix;

import java.util.List;
import java.util.LinkedList;

public abstract class Scene {
	boolean renderBlocker = false;
	boolean updateBlocker = false;
	boolean blockedUpdate = false;
	double currentTime = 0;
	double renderTime = 0;
	boolean done = false;
	List<Sprite> sprites = new LinkedList<Sprite>();	
	
    public void preload()
    {
    }
       
    public void updateTimer(double deltaTime)
    {
    	currentTime += deltaTime;
    	renderTime = currentTime;
    	
    	for (Sprite s : sprites)
    	{
    		s.update(currentTime);
    	}
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

	public abstract void tick();
    
}

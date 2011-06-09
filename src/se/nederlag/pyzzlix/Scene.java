package se.nederlag.pyzzlix;

public abstract class Scene {
	boolean renderBlocker = false;
	boolean updateBlocker = false;
	boolean blockedUpdate = false;
	double currentTime = 0;
	double renderTime = 0;
	boolean done = false;
	Sprite[] sprites;
	
    public void preload()
    {
    }
       
    public void updateTimer(double deltaTime)
    {
    	currentTime += deltaTime;
    	renderTime = currentTime;
    }
        
    public void handleEvent();

    public void hide();

    public void show();

    public void isDone();

    public void isBlockingUpdates();
    
    public void isBlockingRendering();
    
    public abstract void tick();
    
}

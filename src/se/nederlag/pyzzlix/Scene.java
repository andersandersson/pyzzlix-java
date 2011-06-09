package se.nederlag.pyzzlix;

public interface Scene {
    public void preload();
       
    public void updateTimer(float deltaTime);
        
    public void handleEvent();

    public void hide();

    public void show();

    public void isDone();

    public void isBlockingUpdates();
    
    public void isBlockingRendering();
}

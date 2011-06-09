package se.nederlag.pyzzlix;

public class SceneHandler {
	
	static SceneHandler instance = null;
	
	private SceneHandler()
	{
		
	}
	
	public static SceneHandler getInstance()
	{
		if (instance == null)
			instance = new SceneHandler();
		return instance;
	}
	
	public void pushScene(Scene scene)
	{
	}

	public void renderScenes()
	{
	}

	public void doSceneTicks()
	{
	}
}

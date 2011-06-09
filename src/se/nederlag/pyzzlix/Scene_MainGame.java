package se.nederlag.pyzzlix;

public class Scene_MainGame extends Scene {

	static Scene_MainGame instance = null;
	
	public static Scene_MainGame getInstance()
	{
		if (instance == null)
			instance = new Scene_MainGame();
		return instance;
	}
	
	@Override
	public void tick() {
		
		
	}
	
}

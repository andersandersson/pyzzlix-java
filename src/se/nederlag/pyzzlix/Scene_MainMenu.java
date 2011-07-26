package se.nederlag.pyzzlix;

public class Scene_MainMenu extends Scene {
	private static Scene_MainMenu instance = null;
	
	private Scene_MainMenu() {
	}
	
	public static Scene_MainMenu getInstance() {
		if (instance == null)
			instance = new Scene_MainMenu();
		
		return instance;
	}

	@Override
	public void tick() {
	}
}

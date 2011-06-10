package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Scene_MainGame extends Scene {
	private static Scene_MainGame instance = null;
	
	private Scene_MainGame() {
		Sprite sprite = new Block(10, 10, 1, 0, 10);
		sprites.add(sprite);
	}
	
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

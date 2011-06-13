package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Scene_MainGame extends Scene {
	private static Scene_MainGame instance = null;
	
	private Scene_MainGame() {
		for(int y=0; y<5; y++)
		{
			for(int x=0; x<5; x++)
			{
				final SpriteCallback move_right;
				final SpriteCallback move_left;
				
				move_right = new SpriteCallback() {
					public void callback(Sprite sprite, double currenttime) {
						sprite.moveTo(new Point((Double)getArg(1)+100,(Double)getArg(2)+100), currenttime, 2.0, (SpriteCallback) getArg(0));
					}
				};
				
				move_left = new SpriteCallback() {
					public void callback(Sprite sprite, double currenttime) {
						sprite.moveTo(new Point((Double)getArg(1)+500,(Double)getArg(2)+500), currenttime, 2.0, (SpriteCallback) getArg(0));
					}
				};
				
				Sprite sprite = new Block(x, y, x%7, 0, 10);
				sprites.add(sprite);
				
				move_left.setArgs(move_right, x*32.0, y*32.0);
				move_right.setArgs(move_left, x*32.0, y*32.0);			

				move_right.callback(sprite, 0.0);
			}
		}
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

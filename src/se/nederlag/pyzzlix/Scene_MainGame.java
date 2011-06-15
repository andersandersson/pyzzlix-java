package se.nederlag.pyzzlix;

import com.badlogic.gdx.graphics.Color;

public class Scene_MainGame extends Scene {
	private static Scene_MainGame instance = null;
	
	private Scene_MainGame() {
		Sprite sprite = new Sprite();

		SpriteCallback pingpongrot;
		SpriteCallback pingpongrot_back;
		
		pingpongrot = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				sprite.setRot(0.0);
				sprite.rotateTo(360.0, currenttime, 5.0, (SpriteCallback) getArg(0));
				sprite.fadeTo(new Color(0.0f, 0.0f, 1.0f, 1.0f), currenttime, 5.0, null);
			}
		};	
	
		pingpongrot_back = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				sprite.setRot(0.0);
				sprite.rotateTo(360.0, currenttime, 5.0, (SpriteCallback) getArg(0));
				sprite.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), currenttime, 5.0, null);
			}
		};

		SpriteCallback movecallback;
		SpriteCallback movecallback_back;
		
		movecallback = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				sprite.moveTo(new Point(600,500), currenttime, 5.0, (SpriteCallback) getArg(0));
			}
		};	
	
		movecallback_back = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				sprite.moveTo(new Point(200,100), currenttime, 5.0, (SpriteCallback) getArg(0));
			}
		};
		
		movecallback.setArgs(movecallback_back);
		movecallback_back.setArgs(movecallback);

		pingpongrot.setArgs(pingpongrot_back);
		pingpongrot_back.setArgs(pingpongrot);
		
		sprite = new Block(0,0,0,0,0);
		sprite.setPos(new Point(200, 100));
		sprite.setScale(new Point(5, 5));
		sprite.setCenter(new Point(16, 16));
		pingpongrot.callback(sprite, 0.0);
		movecallback.callback(sprite, 0.0);

		sprites.add(sprite);

		Sprite last_sprite = sprite;

		for(int i = 0; i<10; i++)
		{
			sprite = new Block(0,0,0,0,0);
			sprite.setPos(new Point(16, 16));
			sprite.setScale(new Point(0.8, 0.8));
			sprite.setCenter(new Point(16, 16));
			sprite.setRot(10.0);
			pingpongrot.callback(sprite, 0.0);
			last_sprite.addSubSprite(sprite);
			last_sprite = sprite;
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

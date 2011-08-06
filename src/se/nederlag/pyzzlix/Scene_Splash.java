package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_Splash extends Scene {
	private static Scene_Splash instance = null;
	
	private Sprite splash;
	private Font font;
	private Text text1;
	private Text text2;
	private boolean fadingOut;
	
	private Scene_Splash() {
        this.renderBlocker = true;
        this.updateBlocker = true;
    
        this.splash = new Sprite(new Texture(Gdx.files.internal("data/splash.png")));
        this.splash.setPos(new Point(0, 40));
        this.splash.setCenter(new Point(64, 64));
        
        this.font = new Font("data/font_clean.png", 4, 8);
        this.text1 = new Text(0, -28, this.font, "Grumpy Entertainment presents");
        this.text1.setAnchor(Text.Anchor.CENTER);
        this.text1.setCol(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        
        this.text2 = new Text(0, -28, this.font, "A Game Design Course Project");
        this.text2.setAnchor(Text.Anchor.CENTER);
        this.text2.setCol(new Color(0.0f, 0.0f, 0.0f, 0.0f));

        this.addSprite(this.splash);
        this.addSprite(this.text1);
        this.addSprite(this.text2) ;   
        
        this.fadingOut = false;     
    }
	
	public static Scene_Splash getInstance() {
		if (instance == null)
			instance = new Scene_Splash();
		return instance;
	}

	@Override
	public void tick() {
	}

	public void show() {
        this.fadingOut = false;
        this.presents_logofadein(null);
	}
        
    public void presents_logofadein(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.presents_textfadein(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
        this.splash.setCol(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        
        this.splash.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 4.0, callback);
    }
        
    public void presents_textfadein(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.production_logofadein(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
        this.text1.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 4.0, callback);
    }
        
    public void production_logofadein(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.production_textfadein(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
    	this.splash.fadeTo(new Color(1.0f, 0.0f, 0.0f, 1.0f), this.currentTime, 3.0, callback);
    }
        
    public void production_textfadein(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.donefading(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
    	this.text1.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.0f), this.currentTime, 2.0, null);
    	this.text2.fadeTo(new Color(1.0f, 0.0f, 0.0f, 1.0f), this.currentTime, 4.0, callback);
    }
        
    public void donefading(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.fadeout(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
    	this.text2.fadeTo(new Color(1.0f, 0.0f, 0.0f, 1.0f), this.currentTime, 4.0, callback);
    }
        
    public void fadeout(Sprite sprite) {
    	this.fadingOut = true;
    	
    	SpriteCallback callback = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Scene_Splash scene_splash = (Scene_Splash) getArg(0);
				scene_splash.cleanup(sprite);
			}
    	};
    	
    	callback.setArgs(this);
    	
    	this.text2.fadeTo(new Color(0.0f, 0.0f, 0.0f, 1.0f), this.currentTime, 4.0, null);
    	this.splash.fadeTo(new Color(0.0f, 0.0f, 0.0f, 1.0f), this.currentTime, 4.0, callback);
    }
    
    public void cleanup(Sprite sprite) {
        SceneHandler.getInstance().removeScene(this);
        SceneHandler.getInstance().pushScene(Scene_MainMenu.getInstance());
    }
    
    public void hide() {
    }
        
    public boolean handleEvent(Event event) {
    	if(event.type == Event.Type.KEYBOARD) {
    		EventKeyState keyevent = (EventKeyState) event;
    		
    		if(keyevent.state == EventKeyState.State.DOWN) {
    			switch(keyevent.key) {
	    			case Input.Keys.ENTER:
	    				if(!this.fadingOut) {
	    					this.cleanup(null);
	    				}
	    				break;
    			}
    		}
    	}
    	return true;
    }
}

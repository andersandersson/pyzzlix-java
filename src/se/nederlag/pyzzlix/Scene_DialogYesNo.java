package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.Scene_GameOver.State;
import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_DialogYesNo extends Scene {
	private static Scene_DialogYesNo instance = new Scene_DialogYesNo();

	public enum State {
		SHOWING, FADING;
	};

	private Font font;
	private Sprite background;
	
	private String query;
	private Text queryText;
	
	private Callback yesCallback;
	private Callback noCallback;
	
	private Menu menu;
	private MenuItem menuYes;
	private MenuItem menuNo;
	
	private Sprite menuSprite;
	
	private State state;
	private Mixer.Sound moveSound;
	private Mixer.Sound selectSound;
	
	private Scene_DialogYesNo() {
        this.query = "This should be a question?";            
    	
        this.font = new Font("data/font_fat.png", 8, 8);

        this.queryText = new Text(0, -10, this.font, this.query);
        this.queryText.setAnchor(Text.Anchor.CENTER);

        this.menu = new Menu();
        this.menuYes = new MenuItem(-20, 10, this.font, "Yes", this.yesCallback, Text.Anchor.CENTER);
        this.menuNo = new MenuItem(20, 10, this.font, "No", this.noCallback, Text.Anchor.CENTER);
        this.menu.add(this.menuYes);
        this.menu.add(this.menuNo);
                
        this.menuSprite = new Sprite();
        this.menuSprite.addSubSprite(this.queryText);
        this.menuSprite.addSubSprite(this.menu);
        this.menuSprite.setPos(new Point(160, 100));
                
		this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.setScale(new Point(320, -240));
        this.background.setPos(new Point(0, -1));
        this.background.setCol(new Color(0, 0, 0, 0.7f));
        
        this.addSprite(this.background);
        this.addSprite(this.menuSprite);
            
        this.moveSound = Resources.getSound("menumove") ;
        this.selectSound = Resources.getSound("menuselect");
        
        this.state = State.SHOWING;
	}
	
	public static Scene_DialogYesNo getInstance() {
		return Scene_DialogYesNo.instance;
	}

	public void setQuery(String query, Callback yescall, Callback nocall) {
        this.query = query;
        this.yesCallback = yescall;
        this.noCallback = nocall;
        this.queryText.setText(query);
        this.menuYes.setCallback(this.yesCallback);
        this.menuNo.setCallback(this.noCallback);
    }
	
	@Override
	public void tick() {
	}

	public void show() {
		this.menu.reset();
        this.menu.focusItem(1);
        this.background.setCol(new Color(0, 0, 0, 0));
        this.background.fadeTo(new Color(0, 0, 0, 1), this.currentTime, 0.3, null);
        this.menuSprite.setCol(new Color(1, 1, 1, 0));
        this.menuSprite.fadeTo(new Color(1, 1, 1, 1), this.currentTime, 0.2, null);
        this.state = State.SHOWING;
        this.updateBlocker = true;
	}

	        
	public void remove(SpriteCallback callfunc) {;
        this.menuSprite.fadeTo(new Color(0, 0, 0, 0), this.currentTime, 0.2, null);
        this.background.fadeTo(new Color(0, 0, 0, 0), this.currentTime, 0.5, callfunc);
        this.state = State.FADING;
        this.updateBlocker = false;
	}
	        
	public boolean handleEvent(Event event) {
        if(this.state == State.FADING) {
        	return false; 
        }

        if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
            
			if(keyevent.state == EventKeyState.State.DOWN) {
				switch(keyevent.key) {
					case Input.Keys.LEFT:
						this.menu.prevItem();
						break;
					case Input.Keys.RIGHT:
						this.menu.nextItem();
						break;
					case Input.Keys.ENTER:
						Mixer.getInstance().playSound(this.selectSound, 1.0);
						this.menu.selectItem();
						break;
					case Input.Keys.ESCAPE:
						Mixer.getInstance().playSound(this.selectSound, 1.0);
						this.menu.getItem(1).select();
						break;
				}
			}
        }
        
		return true;
	}
}

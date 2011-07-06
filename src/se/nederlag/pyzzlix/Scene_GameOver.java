package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

public class Scene_GameOver extends Scene {
	private static Scene_GameOver instance = new Scene_GameOver();

	public enum State {
		SHOWING, FADING;
	};

	private Menu menu;
	private Font font;
	private Sprite background;	
	private Text gameoverText;
	private Text highscoreCaption;
	private Text highscoreName;
	
	private State state;

	private Callback replayCallback;
	private Callback exitCallback;
	
	private int level;
	private int score;
	
	private Scene_GameOver() {
		this.font = new Font("data/font_fat.png", 8, 8);
		
        this.gameoverText = new Text(160, 90, this.font, "GAME OVER");
        this.gameoverText.setAnchor(Text.Anchor.CENTER);
        this.gameoverText.setCenter(new Point(0,4));
        this.gameoverText.setScale(new Point(4.0f, 2.75f));

        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.setScale(new Point(320, -240));
        this.background.setPos(new Point(0, -1));
        this.background.setCol(new Color(0, 0, 0, 0.7f));

        Callback menuReplayCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_GameOver gameover = (Scene_GameOver) args[0];
				gameover.menu_playAgain();
				return null;
			}
        };
        
        Callback menuExitCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_GameOver gameover = (Scene_GameOver) args[0];
				gameover.menu_quit();
				return null;
			}
        };
        
        this.menu = new Menu();
        this.menu.setPos(new Point(160, 120));
        this.menu.add(new MenuItem(0, 0, this.font, "Play again", menuReplayCallback, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 24, this.font, "Exit to menu", menuExitCallback, Text.Anchor.CENTER));
        this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));

        this.highscoreCaption = new Text(160, 120, this.font, "New highscore!");
        this.highscoreCaption.setAnchor(Text.Anchor.CENTER);
        this.highscoreCaption.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        
        this.highscoreName = new Text(160, 130, this.font, "Please enter your initials");
        this.highscoreName.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.highscoreName.setAnchor(Text.Anchor.CENTER);
        
		this.updateBlocker = true;
        this.state = State.SHOWING;
        this.level = 0;
        this.score = 0;
        
        this.addSprite(this.background);
        this.addSprite(this.gameoverText);
        this.addSprite(this.menu);
        this.addSprite(this.highscoreCaption);
        this.addSprite(this.highscoreName);
	}
	
	public static Scene_GameOver getInstance() {
		return Scene_GameOver.instance;
	}
	
	@Override
	public void tick() {
	}

	public void display(int level, int score, Callback replayCallback, Callback exitCallback) {
        this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.highscoreCaption.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.highscoreName.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));    

        this.replayCallback = replayCallback;
        this.exitCallback = exitCallback;

        this.level = level;
        this.score = score;

        SceneHandler.getInstance().pushScene(this);
	}	

	public void showEnterHighscore() {
        this.highscoreCaption.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        this.highscoreName.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));

        Callback textEntered = new Callback(this) {
			public Object call(Object... params) {
				String text = (String) params[0];
				Scene_GameOver gameover = (Scene_GameOver) args[0];
				Scene_Highscore highscore = Scene_Highscore.getInstance();
				
				highscore.addNewHighscore(text, gameover.score, gameover.level);
				highscore.display(gameover.replayCallback, gameover.exitCallback);
			
				SceneHandler.getInstance().removeScene(gameover);
				
				return null;
			}
        };
           
        Scene_InputText enterText = Scene_InputText.getInstance();
        enterText.display(160, 146, 3, textEntered);
	}
	
	public void showGameOverMenu() {
		this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.menu.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.5, null);
    }
	
	@Override
	public void show() {
        SpriteCallback fadeDone = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_GameOver gameover = (Scene_GameOver) getArg(0);
				boolean enterHighscore = (Boolean) getArg(1);

				gameover.updateBlocker = true;
				gameover.state = State.SHOWING;

	            if(enterHighscore) {
	            	gameover.showEnterHighscore();
	            }
			}
        };


		Scene_Highscore highscore = Scene_Highscore.getInstance();
        if(highscore.isNewHighscore(this.score)) {
        	fadeDone.setArgs(this, true);
        } else {
        	fadeDone.setArgs(this, false);
            this.showGameOverMenu();
        }
            
        this.menu.focusItem(0);
        this.background.setCol(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        this.background.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.8f), this.currentTime, 0.5, fadeDone);
        this.gameoverText.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.gameoverText.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.5, null);
        this.state = State.FADING;
        this.updateBlocker = false;
	}
	
	public void fadeOutAndRemove() {
        SpriteCallback fadeDone = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_GameOver gameover = (Scene_GameOver) getArg(0);
				SceneHandler.getInstance().removeScene(gameover);
			}
        };

        fadeDone.setArgs(this);
        
        this.menu.fadeTo(new Color(1.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.2, null);
        this.gameoverText.fadeTo(new Color(1.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.2, null);
        this.background.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.5, fadeDone);
        this.state = State.FADING;
        this.updateBlocker = false;
    }
	
	public void menu_playAgain() {
		this.fadeOutAndRemove();

		if (this.replayCallback != null) {
			this.replayCallback.call();
		}
	}

	public void menu_quit() {
		this.fadeOutAndRemove();

		if (this.exitCallback != null) {
			this.exitCallback.call();
		}
	}	
	
	@Override
	public boolean handleEvent(Event event) {
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				switch(keyevent.key) {
					case Input.Keys.UP:
						this.menu.prevItem();
						break;
					case Input.Keys.DOWN:
						this.menu.nextItem();
						break;
					case Input.Keys.ENTER:
						this.menu.selectItem();
						break;
				}
			}
		}
		return true;
	}	
}

package se.nederlag.pyzzlix;

import jsmug.audio.*;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_MainMenu extends Scene {
	class Logo extends Sprite {
	    class LogoLetter extends Sprite {
	       public LogoLetter(int x, int y, int sx, int sy, int w, int h) {
	            super(new Texture(Gdx.files.internal("data/logo.png")), sx, sy, w, h);
	            this.setPos(new Point(x, y));
	       }
	    }

	    private double lastColorChange;
	    private int colorOrder;
	    private boolean cycling;

	    public Logo(int x, int y) {
            super(new Texture(Gdx.files.internal("data/logo.png")), 0, 0, 251, 73);
            
            int h_mid = 251/2;
            int v_mid = 73/2;
            
	        this.setCenter(new Point(h_mid, v_mid));
	        this.setPos(new Point(x, y));
	            
	        this.addSubSprite(new LogoLetter(-h_mid,   -v_mid, 0, 80, 48, 73));      // P
	        this.addSubSprite(new LogoLetter(-h_mid+32,-v_mid+9, 48, 96, 48, 48));   // y   
	        this.addSubSprite(new LogoLetter(-h_mid+48,-v_mid+9, 96, 96, 80, 48));   // z   
	        this.addSubSprite(new LogoLetter(-h_mid+85,-v_mid+9, 96, 96, 80, 48));   // z   
	        this.addSubSprite(new LogoLetter(-h_mid+128,-v_mid+9, 176, 96, 64, 48)); // l   
	        this.addSubSprite(new LogoLetter(-h_mid+176,-v_mid+9, 0, 160, 16, 64));   // i           
	        this.addSubSprite(new LogoLetter(-h_mid+160,-v_mid-7, 16, 160, 96, 80));  // x
	        
	        this.lastColorChange = 0;
	        this.colorOrder = 0;
	        this.cycling = true;
	    }
	    
	    public void setCycling(boolean cycling) {
	    	this.cycling = cycling;
	    }
	    
	    public boolean getCycling() {
	    	return this.cycling;
	    }
	    
	    public void cycleTextColor(int order, double currentTime, double length) {
	        int i = order % 25;

	        float R = 0.5f+(float)Math.sin((i*3.0f+0.0f)*1.3f)*0.5f;
	        float G = 0.5f+(float)Math.sin((i*3.0f+1.0f)*1.3f)*0.5f;
	        float B = 0.5f+(float)Math.sin((i*3.0f+2.0f)*1.3f)*0.5f;
	        
	        Color color = new Color(R, G, B, 1.0f);
	        this.setTextColor(color, currentTime, length);
	        this.lastColorChange = currentTime;
	    }
	    
	    public void setTextColor(Color color, double currentTime, double length) {
	        for(Sprite s : this.getSubSprites()) {

	            s.fadeTo(color, currentTime, length, null);
	        }
	    }
	    
	    public void update(double currentTime) {
	    	super.update(currentTime);
	    	
	        if(this.cycling) {
	            if (currentTime - this.lastColorChange > 3.0) {
	                this.colorOrder += 1;
	                this.cycleTextColor(this.colorOrder, currentTime, 3.0);
	            }
	        }
	    }
	                
	}
	
	public enum State {
		MENU, TRANSITION, ENTRANCE;
	};
	
	private static Scene_MainMenu instance = null;
	
	private Font menufont;
	private Font textfont;
	private Text crtext;
	private Menu menu;
	private MenuItem startmenu;
	private Sprite background;
	private Logo logo;
	private State state;
	
    private Sound music;
    private Sound menumove;
    private Sound selectsound;
    private Sound startsound;
	
	
	
	public static Scene_MainMenu getInstance() {
		if (instance == null)
			instance = new Scene_MainMenu();
		
		return instance;
	}

    public Logo newLogo(int x, int y) {
    	return new Logo(x, y);
    }
    
	private Scene_MainMenu() {
        this.menufont = new Font("data/font_fat.png", 8, 8);
        this.textfont = new Font("data/font_clean.png", 4, 8);
        
        this.renderBlocker = true;
        this.updateBlocker = true;
        
        this.crtext = new Text(0, -108, this.textfont, "Copyright Anders Andersson and Joel Lennartsson");
        this.crtext.setAnchor(Text.Anchor.CENTER);
        this.addSprite(this.crtext);
    

        Callback menuStartCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_start();
				return null;
			}
        };
        
        Callback menuHighscoresCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_highscores();
				return null;
			}
        };
        
        Callback menuOptionsCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_options();
				return null;
			}
        };
        
        Callback menuQuitCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_quit();
				return null;
			}
        };
        
        this.menu = new Menu();
        this.menu.add(new MenuItem(0, 48, this.menufont, "Start Game", menuStartCallback, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 32, this.menufont, "Highscores", menuHighscoresCallback, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 16, this.menufont, "Options", menuOptionsCallback, Text.Anchor.CENTER));
        //this.menu.add(MenuItem(0, 48, this.menufont, "Help", this.menu_help));
        if(Pyzzlix.runMode != Pyzzlix.RunMode.APPLET) {
        	this.menu.add(new MenuItem(0, 0, this.menufont, "Quit", menuQuitCallback, Text.Anchor.CENTER));
        }

        this.addSprite(this.menu);
        this.menu.setPos(new Point(0, 140));
            
        Callback menuEnterCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_enter();
				return null;
			}
        };
        
        this.startmenu = new MenuItem(0, -48, this.menufont, "Press Enter", menuEnterCallback, Text.Anchor.CENTER);
        this.addSprite(this.startmenu);

        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.scaleTo(new Point(320,240), 0, 0, null);
        this.background.fadeTo(new Color(0, 0, 0, 0.7f), 0, 0, null);
        
        this.logo = new Logo(10, 20);
        this.logo.setTextColor(new Color(1, 0, 0, 1), 0, 1.0);
        this.addSprite(this.logo);

        this.state = State.ENTRANCE;
                
        this.music = Resources.getMusic("menumusic");
        this.music.setLooping(true);
        this.menumove = Resources.getSound("menumove");
        this.selectsound = Resources.getSound("menuselect");
        this.startsound = Resources.getSound("menustart");        
	}

	public void setMusicVolume(float volume) {
		this.music.setVolume(volume);
	}
	
	public void tick() {
    }

    public void show() {
        this.state = State.ENTRANCE;;

        
        this.music.fadeIn(4.0);
        
        this.startmenu.setPos(new Point(0, -48));
        this.startmenu.setCol(new Color(1, 1, 1, 0));
        this.startmenu.fadeTo(new Color(1, 1, 1, 1), this.currentTime, 0.3, null);
        this.startmenu.focus(this.currentTime);
    }
       
    public void hide() {
    	this.music.stop();
    }
        
    public boolean handleEvent(Event event) {
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;

			if(keyevent.state == EventKeyState.State.DOWN) {
	            if(keyevent.key == Input.Keys.ESCAPE) {
	                this.menu_quit();
	            }
	                
	            if(this.state == State.MENU) {
	            	if(keyevent.key ==  Input.Keys.UP) {
	                    this.menumove.play();
	                    this.menu.prevItem();
	            	}
	                                 
	                if(keyevent.key == Input.Keys.DOWN) {
	                	this.menumove.play();
	                    this.menu.nextItem();
	                }
	                       
	                if(keyevent.key == Input.Keys.ENTER) {
	                    this.menu.selectItem();
	                }                                  
	            } else if(this.state == State.ENTRANCE) {   
	                if(keyevent.key == Input.Keys.ENTER) {
	                    this.startmenu.select();
	                }
	            }
			}
		}
		
		return true;
	}           

    public void menu_enter() {
    	SpriteCallback callback = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) getArg(0);
				mainmenu.menu_enter2(sprite);
			}    	
    	};
    	callback.setArgs(this);
    	
        this.state = State.TRANSITION;
        this.startmenu.moveTo(new Point(0, -148), this.currentTime, 0.4, callback);
        this.logo.moveTo(new Point(10, 70), this.currentTime, 0.4, null);
        this.startsound.play();
    }
    
    public void menu_enter2(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) getArg(0);
				mainmenu.menu_enter3(sprite);
			}    	
    	};
    	callback.setArgs(this);
    	
        this.menu.setPos(new Point(0, -148));
        this.menu.moveTo(new Point(0, -36), this.currentTime, 0.4, callback);
    }
 
    public void menu_enter3(Sprite sprite) { 
        this.menu.focusItem(0);
        this.state = State.MENU;
    }

    public void menu_start() {
    	this.startsound.play();
    	this.music.stop();
        SceneHandler.getInstance().pushScene(Scene_MainGame.getInstance());
    }
    
    public void menu_options() {
    	this.selectsound.play();
        SceneHandler.getInstance().pushScene(Scene_Options.getInstance());
    }

    public void menu_highscores() {
    	this.selectsound.play();
        Scene_Highscore.getInstance().display(null, null);
    }

    public void menu_quit() {
    	Callback yes_callback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) this.args[0];
				mainmenu.quitGame();
				return null;
			}    	
    	};

    	Callback no_callback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) this.args[0];
				mainmenu.doNothing();
				return null;
			}    	
    	};

    	this.selectsound.play();
        this.music.fadeTo(0.5f, 0.5f);
        Scene_DialogYesNo.getInstance().setQuery("Do you want to quit?", yes_callback, no_callback);
        SceneHandler.getInstance().pushScene(Scene_DialogYesNo.getInstance());
    }
  
    public void quitGame() {
    	EventHandler.getInstance().post(new Event(Event.Type.EXIT));
    }
        
    public void doNothing() {
    	SpriteCallback killDialog = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				SceneHandler.getInstance().removeScene(Scene_DialogYesNo.getInstance());
			}
    	};
    	
                
        Scene_DialogYesNo.getInstance().remove(killDialog);
        this.music.fadeTo(1.0f, 0.5f);
    }
}

package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.Event;
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
	        this.setCenter(new Point(251 / 2, 73 / 2));
	        this.setPos(new Point(x, y));
	            
	        this.addSubSprite(new LogoLetter(0, 0, 0, 80, 48, 80));      // P
	        this.addSubSprite(new LogoLetter(32, 16, 48, 96, 48, 48));   // y   
	        this.addSubSprite(new LogoLetter(48, 16, 96, 96, 80, 48));   // z   
	        this.addSubSprite(new LogoLetter(85, 16, 96, 96, 80, 48));   // z   
	        this.addSubSprite(new LogoLetter(128, 16, 176, 96, 64, 48)); // l   
	        this.addSubSprite(new LogoLetter(176, 0, 0, 160, 16, 64));   // i           
	        this.addSubSprite(new LogoLetter(160, 0, 16, 160, 96, 80));  // x
	        
	        this.lastColorChange = 0;
	        this.colorOrder = 0;
	        this.cycling = true;
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
	
    private Mixer.Music music;
    private Mixer.Sound menumove;
    private Mixer.Sound selectsound;
    private Mixer.Sound startsound;
	
	
	
	public static Scene_MainMenu getInstance() {
		if (instance == null)
			instance = new Scene_MainMenu();
		
		return instance;
	}

	private Scene_MainMenu() {
        this.menufont = new Font("data/font_fat.png", 8, 8);
        this.textfont = new Font("data/font_clean.png", 4, 8);
        
        this.renderBlocker = true;
        this.updateBlocker = true;
        
        this.crtext = new Text(160, 220, this.textfont, "Copyright Anders Andersson and Joel Lennartsson");
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
        this.menu.setPos(new Point(160, 100));
        this.menu.add(new MenuItem(0, 0, this.menufont, "Start Game", menuStartCallback, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 16, this.menufont, "Highscores", menuHighscoresCallback, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 32, this.menufont, "Options", menuOptionsCallback, Text.Anchor.CENTER));
        //this.menu.add(MenuItem(0, 48, this.menufont, "Help", this.menu_help));
        this.menu.add(new MenuItem(0, 48, this.menufont, "Quit", menuQuitCallback, Text.Anchor.CENTER));

        this.addSprite(this.menu);
        this.menu.setPos(new Point(160, 260));
            
        Callback menuEnterCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) args[0];
				mainmenu.menu_enter();
				return null;
			}
        };
        
        this.startmenu = new MenuItem(160, 160, this.menufont, "Press Enter", menuEnterCallback, Text.Anchor.CENTER);
        this.addSprite(this.startmenu);

        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.scaleTo(new Point(320,240), 0, 0, null);
        this.background.fadeTo(new Color(0, 0, 0, 0.7f), 0, 0, null);
        
        this.logo = new Logo(170, 100);
        this.logo.setTextColor(new Color(1, 0, 0, 1), 0, 1.0);
        this.addSprite(this.logo);

        this.state = State.ENTRANCE;
                
        this.music = Resources.getMusic("menumusic");
        this.menumove = Resources.getSound("menumove");
        this.selectsound = Resources.getSound("menuselect");
        this.startsound = Resources.getSound("menustart");        
	}

	public void tick() {
    }

    public void show() {
        this.state = State.ENTRANCE;;
        Mixer.getInstance().playMusic(this.music, 1.0, 0.0, true);
        this.startmenu.setPos(new Point(160, 160));
        this.startmenu.setCol(new Color(1, 1, 1, 0));
        this.startmenu.fadeTo(new Color(1, 1, 1, 1), this.currentTime, 0.3, null);
        this.startmenu.focus(this.currentTime);
    }
       
    public void hide() {
        Mixer.getInstance().stopMusic(this.music);
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
	                    Mixer.getInstance().playSound(this.menumove, 1.0);
	                    this.menu.prevItem();
	            	}
	                                 
	                if(keyevent.key == Input.Keys.DOWN) {
	                    Mixer.getInstance().playSound(this.menumove, 1.0);
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
        this.startmenu.moveTo(new Point(160, 260), this.currentTime, 0.4, callback);
        this.logo.moveTo(new Point(170, 50), this.currentTime, 0.4, null);
        Mixer.getInstance().playSound(this.startsound, 1.0);
    }
    
    public void menu_enter2(Sprite sprite) {
    	SpriteCallback callback = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_MainMenu mainmenu = (Scene_MainMenu) getArg(0);
				mainmenu.menu_enter3(sprite);
			}    	
    	};
    	callback.setArgs(this);
    	
        this.menu.setPos(new Point(160, 260));
        this.menu.moveTo(new Point(160, 100), this.currentTime, 0.4, callback);
    }
 
    public void menu_enter3(Sprite sprite) { 
        this.menu.focusItem(0);
        this.state = State.MENU;
    }

    public void menu_start() {
        Mixer.getInstance().playSound(this.startsound, 1.0);
        Mixer.getInstance().stopMusic(this.music);
        SceneHandler.getInstance().pushScene(Scene_MainGame.getInstance());
    }
    
    public void menu_options() {
        Mixer.getInstance().playSound(this.selectsound, 1.0);
        //SceneHandler.getInstance().pushScene(Scene_Options());
    }

    public void menu_highscores() {
        Mixer.getInstance().playSound(this.selectsound, 1.0);
        //SceneHandler.getInstance().pushScene(Scene_Highscore());
    }

    public void menu_help() {
        Mixer.getInstance().playSound(this.selectsound, 1.0);
        //SceneHandler.getInstance().pushScene(Scene_Help());
    }

    public void menu_quit() {
        Mixer.getInstance().playSound(this.selectsound, 1.0);
        Mixer.getInstance().setMusicVolume(this.music, 0.5, 0.5);
        //Scene_DialogYesNo().setQuery("Do you want to quit?", this.quitGame, this.doNothing)
        //SceneHandler.getInstance().pushScene(Scene_DialogYesNo())
    }
  
    public void quitGame() {
        /*pygame.event.post(pygame.event.Event(QUIT))*/
    }
        
        
    public void doNothing() {
    }
    
    public void killDialog(Sprite sprite) {
    	/*
        SceneHandler.getInstance().removeScene(Scene_DialogYesNo())        
        Scene_DialogYesNo().remove(killDialog)
        Mixer.getInstance().setMusicVolume(this.music, 1.0, 0.5)
        */
    }
}

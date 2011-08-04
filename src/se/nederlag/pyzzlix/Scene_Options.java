package se.nederlag.pyzzlix;

import java.util.HashMap;
import java.util.Map;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_Options extends Scene {
	public enum State {
		TOP, MUSIC, SOUND, TUTORIAL, FULLSCREEN;
	};
	
	private static Scene_Options instance = new Scene_Options();
	
	private Font font;
	private State state;
	
	private Sprite background;
	private Text title;
	
	private Color menuActiveCol;
	private Color menuInactiveCol;
	private Sprite menuSprite;
	
	private Menu menu;	
	private Menu menuVolumeMusic;
	private Menu menuVolumeSound;
	private Menu menuTutorials;
	private Menu menuFullscreen;
	private Menu subMenu;	
	
	private Mixer.Sound movesound;
	private Mixer.Sound selectsound;
	
	private Map<String,Color> focusColors;
	private Map<String,Point> focusScales;
	
	public static Scene_Options getInstance() {
		return Scene_Options.instance;
	}

	private Scene_Options() {		
		this.font = new Font("data/font_fat.png", 8, 8);

        this.menuActiveCol = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.menuInactiveCol = new Color(0.5f, 0.5f, 0.5f, 1.0f);
        
        this.menuSprite = new Sprite();
        this.menuSprite.setPos(new Point(76, 60));
        
        Callback focusVolumeMusicCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.focusVolumeMusic();
				return null;
        	}
        };
        
        Callback focusVolumeSoundCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.focusVolumeSound();
				return null;
        	}
        };
        
        Callback focusTutorialsCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.focusTutorials();
				return null;
        	}
        };
        
        Callback focusFullscreenCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.focusFullscreen();
				return null;
        	}
        };
        
        Callback resetHighscoresCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.resetHighscores();
				return null;
        	}
        };
        
        Callback closeOptionsCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.closeOptions();
				return null;
        	}
        };
        
        this.menu = new Menu();
        this.menu.add(new MenuItem(0, 0, this.font, "Music volume", focusVolumeMusicCallback, Text.Anchor.LEFT));
        this.menu.add(new MenuItem(0, 30, this.font, "Effects volume", focusVolumeSoundCallback, Text.Anchor.LEFT));
        this.menu.add(new MenuItem(0, 60, this.font, "Tutorial", focusTutorialsCallback, Text.Anchor.LEFT));
        this.menu.add(new MenuItem(0, 90, this.font, "Fullscreen", focusFullscreenCallback, Text.Anchor.LEFT));
        this.menu.add(new MenuItem(0, 120, this.font, "Reset highscores", resetHighscoresCallback, Text.Anchor.LEFT));
        this.menu.add(new MenuItem(84, 160, this.font, "Exit", closeOptionsCallback, Text.Anchor.CENTER));

        this.menu.setCol(this.menuActiveCol);
        
        Callback focusTopCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		options.focusTop();
				return null;
        	}
        };
        
        Callback updateOptionsCallback = new Callback(this) {
        	public Object call(Object... params) {
        		Scene_Options options = (Scene_Options) args[0];
        		String name = (String) params[0];
        		Object value = params[1];
        		options.updateOptions(name, value);
				return null;
        	}
        };
        
        this.menuVolumeMusic = create_scale_menu(this.font, 10, 13, 1.0, 1.0, "music_volume", focusTopCallback, updateOptionsCallback);
        this.menuVolumeMusic.setPos(new Point(20, 13));
        this.menuVolumeMusic.focusItem(10);
        this.menuVolumeMusic.setCol(this.menuInactiveCol);

        this.menuVolumeSound = create_scale_menu(this.font, 10, 13, 1.0, 1.0, "sound_volume", focusTopCallback, updateOptionsCallback);
        this.menuVolumeSound.setPos(new Point(20, 43));
        this.menuVolumeSound.focusItem(10);
        this.menuVolumeSound.setCol(this.menuInactiveCol);

        this.menuTutorials = create_on_off_menu(this.font, 10, 13, 1.0, 1.0, "show_tutorials", focusTopCallback, updateOptionsCallback);
        this.menuTutorials.setPos(new Point(20, 73));
        this.menuTutorials.focusItem(0);
        this.menuTutorials.setCol(this.menuInactiveCol);

        this.menuFullscreen = create_on_off_menu(this.font, 10, 13, 1.0, 1.0, "fullscreen", focusTopCallback, updateOptionsCallback);
        this.menuFullscreen.setPos(new Point(20, 103));
        this.menuFullscreen.focusItem(0);
        this.menuFullscreen.setCol(this.menuInactiveCol);

        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.setScale(new Point(320, -240));
        this.background.setPos(new Point(0, -1));
        this.background.setCol(new Color(0, 0, 0, 0.7f));
        
        this.title = new Text(160, 20, this.font, "OPTIONS");
        this.title.setScale(new Point(2.0, 2.0));
        this.title.setAnchor(Text.Anchor.CENTER);
        
        this.menuSprite.addSubSprite(this.menu);
        this.menuSprite.addSubSprite(this.menuVolumeMusic);
        this.menuSprite.addSubSprite(this.menuVolumeSound);
        this.menuSprite.addSubSprite(this.menuTutorials);
        this.menuSprite.addSubSprite(this.menuFullscreen);

        this.addSprite(this.background);
        this.addSprite(this.menuSprite);
        this.addSprite(this.title);

        this.movesound = Resources.getSound("menumove");
        this.selectsound = Resources.getSound("menuselect");

        this.state = State.TOP;

        this.subMenu = null;

        this.focusColors = new HashMap<String,Color>();
        this.focusScales = new HashMap<String,Point>();
        
        this.focusColors.put("submenu_focus", this.menu.getItem(0).focusColor);
        this.focusColors.put("submenu_unfocus", this.menu.getItem(0).focusColor);
        this.focusColors.put("topmenu_focus", this.menu.getItem(0).focusColor);
        this.focusColors.put("topmenu_unfocus", this.menu.getItem(0).unfocusColor);
        
        this.focusScales.put("submenu_focus", new Point(1.2, 1.2));
        this.focusScales.put("submenu_unfocus", this.menuVolumeMusic.getItem(0).focusScale);
        this.focusScales.put("topmenu_focus", this.menu.getItem(0).focusScale);
        this.focusScales.put("topmenu_unfocus", new Point(1.3, 1.3));

        Integer music_volume = (Integer) Options.get("music_volume");
        if(music_volume != null) {
            this.menuVolumeMusic.focusItem(music_volume);
        } else {
            this.menuVolumeMusic.focusItem(10);
        }

        Integer sound_volume = (Integer) Options.get("sound_volume");
        if(sound_volume != null) {
            this.menuVolumeSound.focusItem(sound_volume);
        } else {
            this.menuVolumeSound.focusItem(10);
        }

        Boolean show_tutorials = (Boolean) Options.get("show_tutorials");
        if(show_tutorials == null || show_tutorials) {
            this.menuTutorials.focusItem(0);
        } else {
            this.menuTutorials.focusItem(1);
        }

        Boolean fullscreen = (Boolean) Options.get("fullscreen");
        if(fullscreen == null || fullscreen) {
            this.menuFullscreen.focusItem(0);
        } else {
            this.menuFullscreen.focusItem(1);
        }
 	}

	public Menu create_scale_menu(Font font, int size, int width, double scale_x, double scale_y, String name, Callback callback, Callback update_callback) {
         Menu menu = new Menu();
         
         for(int i = 0; i < size+1; i++) {
        	 Callback menu_callback = new Callback(name, i, callback, update_callback) {
				public Object call(Object... params) {
					String name = (String) args[0];
					int value = (Integer) args[1];
					Callback callback = (Callback) args[2];
					Callback update_callback = (Callback) args[3];
					
					if(update_callback != null) {
						update_callback.call(name, value);
					}
					
					if(callback != null) {
						callback.call();
					}
					
					return null;
				}
        	 };
                 
             MenuItem item = new MenuItem(i*width, 0, font, Integer.toString(i), menu_callback, Text.Anchor.LEFT);
             item.focusScale = new Point(scale_x, scale_y);
             item.unfocusScale = new Point(scale_x, scale_y);
             menu.add(item);
         }
         
         return menu;
     }

     public Menu create_on_off_menu(Font font, int size, int width, double scale_x, double scale_y, String name, Callback callback, Callback update_callback) {
         Menu menu = new Menu();

    	 Callback menu_callback_on = new Callback(name, callback, update_callback) {
			public Object call(Object... params) {
				String name = (String) args[0];
				Callback callback = (Callback) args[1];
				Callback update_callback = (Callback) args[2];
				
				if(update_callback != null) {
					update_callback.call(name, true);
				}
				
				if(callback != null) {
					callback.call();
				}
				
				return null;
			}
    	 };

    	 Callback menu_callback_off = new Callback(name, callback, update_callback) {
			public Object call(Object... params) {
				String name = (String) args[0];
				Callback callback = (Callback) args[1];
				Callback update_callback = (Callback) args[2];
				
				if(update_callback != null) {
					update_callback.call(name, false);
				}
				
				if(callback != null) {
					callback.call();
				}
				
				return null;
			}
    	 };
    	 
    	 MenuItem item;
    	 item = new MenuItem(0, 0, font, "On", menu_callback_on, Text.Anchor.LEFT);
         item.focusScale = new Point(scale_x, scale_y);
         item.unfocusScale = new Point(scale_x, scale_y);

         menu.add(item);

         item = new MenuItem(24, 0, font, "Off", menu_callback_off, Text.Anchor.LEFT);
         item.focusScale = new Point(scale_x, scale_y);
         item.unfocusScale = new Point(scale_x, scale_y);

         menu.add(item);

         return menu;
    }
     
	public void updateOptions(String name, Object value) {
		/*
        if name == "fullscreen":
            Renderer().setFullScreen(value)
        */
            
        Options.set(name, value);
	}
        
    public void tick() {    	
    }
    
    public void show() {
        this.menu.focusItem(0);
        this.subMenu = this.menuVolumeMusic;
    }
    
    public void remove(Callback callfunc) {
    
    }

    public void focusVolumeMusic() {
        this.menu.unfocusItem();
        this.menuVolumeMusic.setCol(this.menuActiveCol);
        this.menuVolumeMusic.setFocusScale(this.focusScales.get("submenu_focus"));
        this.state = State.MUSIC;
    }

    public void focusVolumeSound() {
        this.menu.unfocusItem();
        this.menuVolumeSound.setCol(this.menuActiveCol);
        this.menuVolumeSound.setFocusScale(this.focusScales.get("submenu_focus"));
        this.state = State.SOUND;
    }

    public void focusTutorials() {
        this.menu.unfocusItem();
        this.menuTutorials.setCol(this.menuActiveCol);
        this.menuTutorials.setFocusScale(this.focusScales.get("submenu_focus"));
        this.state = State.TUTORIAL;
    }

    public void focusFullscreen() {
        this.menu.unfocusItem();
        this.menuFullscreen.setCol(this.menuActiveCol);
        this.menuFullscreen.setFocusScale(this.focusScales.get("submenu_focus"));
        this.state = State.FULLSCREEN;
    }

    public void focusTop() {
        this.menu.focusItem(this.menu.getFocus());
        this.menuTutorials.setCol(this.menuInactiveCol);
        this.menuTutorials.setFocusScale(this.focusScales.get("submenu_unfocus"));
        this.menuFullscreen.setCol(this.menuInactiveCol);
        this.menuFullscreen.setFocusScale(this.focusScales.get("submenu_unfocus"));
        this.menuVolumeMusic.setCol(this.menuInactiveCol);
        this.menuVolumeMusic.setFocusScale(this.focusScales.get("submenu_unfocus"));
        this.menuVolumeSound.setCol(this.menuInactiveCol);
        this.menuVolumeSound.setFocusScale(this.focusScales.get("submenu_unfocus"));
        this.state = State.TOP;
    }

    public void resetHighscores() {
        Callback reset_yes = new Callback() {
			public Object call(Object... params) {
	            Scene_Highscore.getInstance().resetHighscores();
	            SceneHandler.getInstance().removeScene(Scene_DialogYesNo.getInstance());
	            
	            return null;
			}
        };

        Callback reset_no = new Callback() {
			public Object call(Object... params) {
	            SceneHandler.getInstance().removeScene(Scene_DialogYesNo.getInstance());
	            
	            return null;
			}
        };

        Scene_DialogYesNo.getInstance().setQuery("Really reset highscores?", reset_yes, reset_no);
        SceneHandler.getInstance().pushScene(Scene_DialogYesNo.getInstance());
    }

    public void closeOptions() {
        SceneHandler.getInstance().removeScene(this);
    }
    
    public boolean handleEvent(Event event) {
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
	            if(this.state == State.TOP) {
	                if (keyevent.key == Input.Keys.UP) {
	                    Mixer.getInstance().playSound(this.movesound, 1.0);
	                    this.menu.prevItem();
	                }
	                                           
	                if (keyevent.key == Input.Keys.DOWN) {
	                    Mixer.getInstance().playSound(this.movesound, 1.0);
	                    this.menu.nextItem();
	                }
	            
	                if (keyevent.key == Input.Keys.ENTER) {
	                    Mixer.getInstance().playSound(this.selectsound, 1.0);
	                    this.menu.selectItem();
	                }

	                if (keyevent.key == Input.Keys.ESCAPE) {
	                    Mixer.getInstance().playSound(this.selectsound, 1.0);
	                    this.closeOptions();
	                }
                } else {
	                if (keyevent.key == Input.Keys.LEFT) {
	                    Mixer.getInstance().playSound(this.movesound, 1.0);
	                    this.subMenu.prevItem();
	                }
	                                           
	                if (keyevent.key == Input.Keys.RIGHT) {
	                    Mixer.getInstance().playSound(this.movesound, 1.0);
	                    this.subMenu.nextItem();
	                }
	            
	                if (keyevent.key == Input.Keys.ENTER) {
	                    Mixer.getInstance().playSound(this.selectsound, 1.0);
	                    this.subMenu.selectItem();
	                }

	                if (keyevent.key == Input.Keys.ESCAPE) {
	                    Mixer.getInstance().playSound(this.selectsound, 1.0);
	                    this.subMenu.selectItem();
	                }
                }
	        }
		}
		
		if(this.menu.getFocus() == 0) {
			this.subMenu = this.menuVolumeMusic;
		}
		if(this.menu.getFocus() == 1) {
			this.subMenu = this.menuVolumeSound;
		}
		if(this.menu.getFocus() == 2) {
			this.subMenu = this.menuTutorials;
		}
		if(this.menu.getFocus() == 3) {
			this.subMenu = this.menuFullscreen;
		}

		return true;
	}
}

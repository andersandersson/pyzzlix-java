package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_MainMenu extends Scene {
	class Logo extends Sprite {
	    class LogoLetter extends Sprite {
	       public LogoLetter(int x, int y, int sx, int sy, int w, int h) {
	            super(new Texture(Gdx.files.internal("data/logo.png")));
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
	        if(this.cycling) {
	            if (currentTime - this.lastColorChange > 3.0) {
	                this.colorOrder += 1;
	                this.cycleTextColor(this.colorOrder, currentTime, 3.0);
	            }
	        }
	    }
	                
	}
	
	private static Scene_MainMenu instance = null;
	
	private Font menufont;
	private Font textfont;
	private Text crtext;
	private Menu menu;
	private Sprite background;
	private Logo logo;
	
	public static Scene_MainMenu getInstance() {
		if (instance == null)
			instance = new Scene_MainMenu();
		
		return instance;
	}

	@Override
	public void tick() {
	}
	
	private Scene_MainMenu() {
		/*
        self.menufont = Font("font_fat.png", 8, 8)
        self.textfont = Font("font_clean.png", 4, 8)
        
        self.renderBlocker = True
        self.updateBlocker = True
        
        self.crtext = Text(160, 220, self.textfont, "Copyright Anders Andersson and Joel Lennartsson")
        self.crtext.setAnchor("center")
        self.sprites.add(self.crtext)
    
        self.menu = Menu()
        self.menu.setPos((160, 100))
        self.menu.add(MenuItem(0, 0, self.menufont, "Start Game", self.menu_start))
        self.menu.add(MenuItem(0, 16, self.menufont, "Highscores", self.menu_highscores))
        self.menu.add(MenuItem(0, 32, self.menufont, "Options", self.menu_options))
        #self.menu.add(MenuItem(0, 48, self.menufont, "Help", self.menu_help))
        self.menu.add(MenuItem(0, 48, self.menufont, "Quit", self.menu_quit))

        self.sprites.add(self.menu)    
        self.menu.setPos((160, 260))
            
        self.startmenu = MenuItem(160, 160, self.menufont, "Press Enter", self.menu_enter)
        self.sprites.add(self.startmenu)

        self.background = Sprite()
        self.background.setImage(loadImage("pixel.png"))
        self.background.scaleTo((320,240),0,0)
        self.background.fadeTo((0.0,0.0,0.0, 0.7),0,0)
        self.background._layer = 0
        
        self.logo = Logo(170, 100)
        self.logo.setTextColor((1.0, 0.0, 0.0, 1.0), 0, 1.0)
        self.sprites.add(self.logo)    

        self.music = None
        self.movesound = None
        self.selectsound = None
        self.startsound = None
        self.state = "menu"
        self.state = "transition"
        self.state = "entrance"
                
        self.music = Resources().getMusic("menumusic")
        self.menumove = Resources().getSound("menumove")
        self.selectsound = Resources().getSound("menuselect")
        print self.selectsound
        self.startsound = Resources().getSound("menustart")
        */
	}
        /*
    def tick(self):
        pass

    def show(self):
        print self, "is showing"
        self.state = "entrance"
        Mixer().playMusic(self.music, loops=-1)
        self.startmenu.setPos((160, 160))
        self.startmenu.setCol((1.0,1.0,1.0,0.0))
        self.startmenu.fadeTo((1.0,1.0,1.0,1.0), self.currentTime, 0.3)
        self.startmenu.focus(self.currentTime)
        
    def hide(self):
        print self, "is hiding"
        Mixer().stopMusic(self.music) 
        
    def handleEvent(self, event):
        if event.type == KEYDOWN:
            key = event.key

            if key == K_ESCAPE:
                self.menu_quit()
                
            if (self.state == "menu"):
                if (key == K_UP):
                    Mixer().playSound(self.menumove)
                    self.menu.prevItem()
                                 
                if (key == K_DOWN):
                    Mixer().playSound(self.menumove)
                    self.menu.nextItem()
                    
                    
                if (key == K_RETURN):
                    self.menu.selectItem()
                    
                
            elif (self.state == "entrance"):   
                if (key == K_RETURN):
                    self.startmenu.select()
                    
            

    def menu_enter(self):
        self.state = "transition"
        self.startmenu.moveTo((160, 260), self.currentTime, 0.4, self.menu_enter2)
        self.logo.moveTo((170, 50), self.currentTime, 0.4)
        Mixer().playSound(self.startsound)
        pass

    def menu_enter2(self, sprite):  
        self.menu.setPos((160, 260))
        self.menu.moveTo((160, 100), self.currentTime, 0.4, self.menu_enter3)
 
    def menu_enter3(self, sprite):  
        self.menu.focusItem(0)
        self.state = "menu" 

    def menu_start(self):
        Mixer().playSound(self.startsound)
        Mixer().stopMusic(self.music)
        SceneHandler().pushScene(scene_maingame.Scene_MainGame())
        pass
    
    def menu_options(self):
        Mixer().playSound(self.selectsound)
        SceneHandler().pushScene(Scene_Options())

    def menu_highscores(self):
        Mixer().playSound(self.selectsound)
        SceneHandler().pushScene(Scene_Highscore())

    def menu_help(self):
        Mixer().playSound(self.selectsound)
        SceneHandler().pushScene(Scene_Help())
        
    def menu_quit(self):
        Mixer().playSound(self.selectsound)
        Mixer().setMusicVolume(self.music, 0.5, 0.5)
        Scene_DialogYesNo().setQuery("Do you want to quit?", self.quitGame, self.doNothing)
        SceneHandler().pushScene(Scene_DialogYesNo())
        pass
      

  
    def quitGame(self):
        pygame.event.post(pygame.event.Event(QUIT))
        
        
    def doNothing(self):
        def killDialog(sprite):
            SceneHandler().removeScene(Scene_DialogYesNo())
        
        Scene_DialogYesNo().remove(killDialog)
        Mixer().setMusicVolume(self.music, 1.0, 0.5)
        pass*/
}

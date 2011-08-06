package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_Tutorial extends Scene {
	class Page extends Sprite {
		protected Font titleFont;
		protected Font textFont;
		
		protected Sprite background;
		
		protected int width;
		protected int height;
		
		protected Point center;
		
		protected Text enterText;
		protected Text leftArrow;
		protected Text rightArrow;
		protected Text pageText;
	
	    public Page(int x, int y, int page, int pageCount) {
	        this.titleFont = new Font("data/font_fat.png", 8, 8);
	        this.textFont = new Font("data/font_clean.png", 4, 8);

	        this.background = new Sprite(new Texture(Gdx.files.internal("data/splashbg.png")));
	        this.setPos(new Point(x, y));
	        this.width = 196;
	        this.height = 166;
	        this.center = new Point(this.width/2, this.height/2);
	        
	        this.addSubSprite(this.background);
	
	        this.enterText = new Text(this.width/2, this.height - 12, this.textFont, "Press Enter to start the game");
	        this.enterText.setAnchor(Text.Anchor.CENTER);
	        
	        this.addSubSprite(this.enterText);
	
	        this.leftArrow = new Text(4, this.height/2 - 4, this.titleFont, "<");
	        this.rightArrow = new Text(this.width - 12, this.height/2 - 4, this.titleFont, ">");
	        
	        this.addSubSprite(this.leftArrow);
	        this.addSubSprite(this.rightArrow);
	
	
	        this.pageText = new Text(this.width - 6, this.height - 12, this.titleFont, String.format("%d/%d", page, pageCount));
	        this.pageText.setAnchor(Text.Anchor.RIGHT);
	
	        this.addSubSprite(this.pageText);
	
	        SpriteCallback fadeInEnterText = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					SpriteCallback fadeOutEnterText = (SpriteCallback) getArg(0);
					sprite.fadeTo(new Color(1, 1, 1, 1), currentTime, 0.5, fadeOutEnterText);    
				}
	        };

	        SpriteCallback fadeOutEnterText = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					SpriteCallback fadeInEnterText = (SpriteCallback) getArg(0);
					sprite.fadeTo(new Color(0, 0, 0, 0), currentTime, 0.5, fadeInEnterText);    
				}
	        };
	        
	        fadeInEnterText.setArgs(fadeOutEnterText);
	        fadeOutEnterText.setArgs(fadeInEnterText);
	        
	        this.enterText.fadeTo(new Color(0, 0, 0, 0), 0, 0.5, fadeInEnterText);   
	        this.setCol(new Color(1, 1, 1, 0));
	    }
	}

class Page_Welcome extends Page {
    private Text welcomeText;
	private Scene_MainMenu.Logo logo;
    private List<Text> infoText;
	
	public Page_Welcome(int x, int y, int page, int pageCount) {
        super(x, y, page, pageCount);

        int dposx = this.width/2;
        int dposy = 10;

        this.welcomeText = new Text(dposx, dposy, this.titleFont, "WELCOME\nTO\n");
        this.welcomeText.setAnchor(Text.Anchor.CENTER);
        this.welcomeText.setScale(new Point(1.5, 1.5));

        this.logo = Scene_MainMenu.getInstance().newLogo(this.width/2, 46); 
        this.logo.setScale(new Point(0.1, 0.1));
        this.logo.setCycling(false);
        
        this.addSubSprite(this.welcomeText);
        this.addSubSprite(this.logo);

        dposy += 60;

        this.infoText = new LinkedList<Text>();
        this.infoText.add(new Text(dposx, dposy, this.textFont,
                                  "This tutorial will explain how to play\n" +
                                  "Pyzzlix! If this is your first time\n" +
                                  "playing, skip this at your own peril..."));

        dposy += 32;

        this.infoText.add(new Text(dposx, dposy, this.textFont,
                                  "Use the left and right ARROW keys to\n" + 
                                  "navigate the pages of this tutorial."));
                  
        dposy += 24;                      
                                  
        this.infoText.add(new Text(dposx, dposy, this.textFont,
                                  "You can choose whether or not to display\n" +
                                  "this tutorial from the options menu."));   
        for(Text t : this.infoText) {
            t.setAnchor(Text.Anchor.CENTER);
            this.addSubSprite(t);
        }
	}
        
    public void show(double currentTime) {
    }

    public void hide() {
    }
}
        /*
 *         
class Page_Controls(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "BASIC CONTROLS")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "When playing the game, use the\n" +
                             "ARROW keys to move the marker."))

        dposy += 35
        
        self.marker = Marker()
        self.marker.setPos((dposx - 16, dposy - 16))
        self.subSprites.append(self.marker)


        dposy += 18        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "The Z and X keys rotates the blocks\n" +
                                  "beneath the marker."))

        dposy += 36


        self.blocktypes = [0, 1,
                      2, 0]
        self.blockspos = (dposx - 8, dposy - 8)          

        
        self.marker2 = Marker()
        self.marker2.setPos((dposx - 16, dposy -16))


        dposy += 18
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "Z rotates counter-clockwise\n" +
                                  "and X rotates clockwise."))
        
        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)
            
    public void showInit(self):        
        self.blocks = createBlockGroup(self.blockspos[0], self.blockspos[1], self.blocktypes, 2)
        for b in self.blocks:
            self.subSprites.append(b)
            
        self.subSprites.append(self.marker2)

    public void show(self, currentTime):
        self.showInit()
     
        public void turnLeft(sprite):
            rotateBlocksInGroup(self.blocks, 1, 1, 2, self.currentTime, True)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, turnRight)

            
        public void turnRight(sprite):
            rotateBlocksInGroup(self.blocks, 1, 1, 2, self.currentTime, False)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, turnLeft) 

        self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), currentTime, 1.0, turnLeft)    

    public void hide(self):
        self.marker2.clearColCallbacks()
        for b in self.blocks:
            self.subSprites.remove(b)
        
        self.subSprites.remove(self.marker2)
        pass
            
        
class Page_Goal(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "THE GOAL")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "The goal of the game is to get a high\n" +
                             "score before the time runs out."))

        dposy += 24
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "Score and extended time are awarded by\n" +
                             "removing blocks. Remove blocks by\n" +
                             "creating loops of same-colored blocks!"))

        dposy += 56

        self.blocktypes = [0, 0, 1,
                      0, 2, 0,
                      2, 1, 2]
        self.blockspos = (dposx - 16, dposy - 16)         
                      

        self.marker2 = Marker()
        self.marker2.setPos((dposx - 8, dposy - 8))


        dposy += 30
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "The simplest loop is a 2x2 square.\n"))
 
                                         
        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)

    public void showInit(self):  
        self.blocks = createBlockGroup(self.blockspos[0], self.blockspos[1], self.blocktypes, 3)

        for b in self.blocks:
            self.subSprites.append(b)
        
        self.subSprites.append(self.marker2)

    public void show(self, currentTime):
        self.showInit()
 
        for b in self.blocks:
                b.doNormal()

        
        public void turnLeft(sprite):
            rotateBlocksInGroup(self.blocks, 2, 2, 3, self.currentTime, True)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.5, doBlink)

            
        public void turnRight(sprite):
            for i in range(3):
                for j in range(3):
                    self.blocks[i * 3 + j].doNormal()
                    
            rotateBlocksInGroup(self.blocks, 2, 2, 3, self.currentTime, False)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, turnLeft)

        public void doBlink(sprite):
            getBlock(self.blocks, 0, 0, 3).doBlink()
            getBlock(self.blocks, 1, 0, 3).doBlink()
            getBlock(self.blocks, 0, 1, 3).doBlink()            
            getBlock(self.blocks, 1, 1, 3).doBlink()

            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.5, turnRight)
            
            
        self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), currentTime, 1.0, turnLeft)    

    public void hide(self):
        self.marker2.clearColCallbacks()
        
        for b in self.blocks:
            self.subSprites.remove(b)
        
        self.subSprites.remove(self.marker2)
        pass


        
class Page_Level(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "LEVELS")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "Each level has a Special Block color.\n" +     
                             "In order to advance to the next level\n" +
                             "a specific number of Special Blocks\n" +
                             "needs to be removed."))

        dposy += 36
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "The Special Block and the progress of\n" +
                             "the current level is displayed in the\n" +
                             "level board."))
 
        dposy += 26

        self.level = 1
        self.levelwindow = Levelboard()
        self.levelwindow.setPos((dposx - 44, dposy))
        self.subSprites.append(self.levelwindow)

        dposy += 50

        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "You will score more points for each removed\n" +
                             "block on higher levels!"))
        
        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)
            
    public void showInit(self):   
        self.level = 1
        self.levelwindow.setNewLevel(1, 0, 20)


    public void show(self, currentTime):
        self.showInit()
        
        self.blocks = 0
        
        public void nextLevel():
            self.level += 1
            if (self.level > 99):
                self.level = 1
                
            self.levelwindow.setNewLevel(self.level, self.level % 3, 20)    


        public void addBlock1(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, addBlock2)
        public void addBlock2(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, addBlock3)
        public void addBlock3(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, addBlock4)
        public void addBlock4(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, addBlock5)
        public void addBlock5(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, addBlock6)
        public void addBlock6(sprite):
            self.blocks += 1
            self.levelwindow.updateLevelboard(self.blocks)
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, checkAndDelay)

            
        public void checkAndDelay(sprite):
            if (self.blocks > 20):
                nextLevel()
                self.blocks = 0
                
            self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, addBlock1)

        self.levelwindow.fadeTo((1.0, 1.0, 1.0, 1.0), currentTime, 0.6, addBlock1)
    
        
        pass
        
    public void hide(self):
        self.levelwindow.clearColCallbacks()
        pass

class Page_Timer(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "THE TIMER")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "If the hourglass on the timer board\n" +
                             "reaches zero the game is over."))

        dposy += 20

        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "Before running out of time, the borders\n" +
                             "will flash red as a warning."))

        dposy += 20
        
        self.hourglass = Hourglass()
        self.hourglass.setPos((dposx - 22, dposy))
        self.hourglass.setScale((0.5, 0.5))
        self.hourglass.scaleValue(0.2)

        self.subSprites.append(self.hourglass)
        

        dposy += 50
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                             "When removing blocks, the timer will also\n" +
                             "be a short while. The timer can only be\n" +
                             "stopped a maximum of 5 seconds though.\n" +
                             "Use this pause to plan your next move!"))
 
        dposy += 36

        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)
            
    public void show(self, currentTime):

        self.hourglass.reset(200)
        pass
        
    public void hide(self):
        self.hourglass.clearColCallbacks()
        pass

    public void setTimerState(self, state):
        if state == "low":
            public void saveTheDay(sprite):
                self.hourglass.value += 100
                self.hourglass.addPause(1.0)
            
            self.hourglass.pulseBorder((1.0, 0.0, 0.0, 0.0), (1.0, 0.0, 0.0, 1.0), 0.5)
            self.hourglass.fadeTo((1.0,1.0,1.0,1.0), self.currentTime, 2.5, saveTheDay)
            
        if state == "normal" or state == "high":
            self.hourglass.stopPulseBorder()
        
class Page_Advanced(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "ADVANCED")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "The loops you create do not have to be\n" +
                                  "symmetric, they can have any shape as long\n" +
                                  "as all blocks in the loop are connected."))

        dposy += 58
                         
        self.blocktypes = [1, 0, 0, 0,
                      0, 0, 2, 0,
                      0, 1, 0, 0,
                      0, 0, 2, 0]
        self.blockspos = (dposx - 24, dposy - 24)              

        self.marker2 = Marker()
        self.marker2.setPos((dposx, dposy))
        
        dposy += 36
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "When encircling other blocks in a loop,\n" +
                                  "they will also be removed. Large loops\n" +
                                  "gives more points than the blocks\n" +
                                  "individually."))
 
                                         
        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)
            
    public void showInit(self):  
        self.blocks = createBlockGroup(self.blockspos[0], self.blockspos[1], self.blocktypes, 4)
        for b in self.blocks:
            self.subSprites.append(b)
                
        self.subSprites.append(self.marker2)

    public void show(self, currentTime):
        self.showInit()
        
        # TODO, DOES NOTHING AT ALL THIS, FIX!:
        for i in range(4):
            for j in range(4):
                self.blocks[i * 4 + j].doNormal()
                self.blocks[i * 4 + j].setToBoardCoord(j, i)

        
        public void turnLeft(sprite):
            rotateBlocksInGroup(self.blocks, 3, 3, 4, self.currentTime, True)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.5, doBlink)
            
        public void turnRight(sprite):
            for i in range(4):
                for j in range(4):
                    self.blocks[i * 4 + j].doNormal()
            
            rotateBlocksInGroup(self.blocks, 3, 3, 4, self.currentTime, False)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, turnLeft)

        public void doBlink(sprite):
            for b in self.blocks:
                b.doBlink()

            getBlock(self.blocks, 0, 0, 4).doNormal()
            getBlock(self.blocks, 2, 1, 4).doNormal()
            getBlock(self.blocks, 1, 2, 4).doNormal()            
            getBlock(self.blocks, 3, 3, 4).doNormal()

            public void doWholeBlink(sprite):
                getBlock(self.blocks, 2, 1, 4).doBlink()
                getBlock(self.blocks, 1, 2, 4).doBlink()            
                
                self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.5, turnRight)
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, doWholeBlink)

            
            
            
        self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), currentTime, 1.0, turnLeft)    

    public void hide(self):
        self.marker2.clearColCallbacks()
        
        for b in self.blocks:
            self.subSprites.remove(b)
                
        self.subSprites.remove(self.marker2)
        
        pass
 
class Page_Combos(Page):
    public void __init__(self, x, y, page, pageCount):
        Page.__init__(self, x, y, page, pageCount)

        dposx = self.width/2
        dposy = 10

        
        self.titletext = Text(dposx, dposy, self.titlefont, "COMBOS")
        self.titletext.setAnchor("center")
        self.titletext.setScale((1.5, 1.5))
        self.subSprites.append(self.titletext)

        dposy += 15
        
        self.infotext = []
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "If a loop is created as a result of blocks\n" +
                                  "landing on other blocks after a fall, \n" +
                                  "a combo is performed."))

        dposy += 58
                         
        self.blocktypes = [1, 2, 2, 0,
                      0, 1, 1, 2,
                      2, 1, 2, 1,
                      2, 2, 2, 2]
        self.blockspos = (dposx - 24, dposy - 24)       

        self.marker2 = Marker()
        self.marker2.setPos((dposx, dposy))


        dposy += 36
        
        self.infotext.append(Text(dposx, dposy, self.textfont,
                                  "Combos give an extra big score bonus.\n" +
                                  "Successive combos will give an increasingly\n" +
                                  "large score bonus for each combo.\n" +
                                  "Try to perform many combos!"))
 
                                         
        for t in self.infotext:
            t.setAnchor("center")
            self.subSprites.append(t)
            
    public void showInit(self):        
        self.blocks = createBlockGroup(self.blockspos[0], self.blockspos[1], self.blocktypes, 4)
        for b in self.blocks:
            self.subSprites.append(b)
                
        self.subSprites.append(self.marker2)   
        
    public void show(self, currentTime):
        self.showInit()
        
        # TODO, DOES NOTHING AT ALL THIS, FIX!:
        for i in range(4):
            for j in range(4):
                self.blocks[i * 4 + j].doNormal()
                self.blocks[i * 4 + j].setToBoardCoord(j, i)

        
        public void turnLeft(sprite):
            rotateBlocksInGroup(self.blocks, 3, 3, 4, self.currentTime, True)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.5, doBlink1)
            
        public void turnRight(sprite):
            for i in range(4):
                for j in range(4):
                    self.blocks[i * 4 + j].doNormal()
            
            rotateBlocksInGroup(self.blocks, 3, 3, 4, self.currentTime, False)
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.5, turnLeft)

        public void doBlink1(sprite):
            getBlock(self.blocks, 1, 1, 4).doBlink()
            getBlock(self.blocks, 1, 2, 4).doBlink()
            getBlock(self.blocks, 2, 1, 4).doBlink()
            getBlock(self.blocks, 2, 2, 4).doBlink()
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, removeBlocks)
            
        public void removeBlocks(sprite):
            getBlock(self.blocks, 1, 1, 4).doNormal()
            getBlock(self.blocks, 1, 2, 4).doNormal()
            getBlock(self.blocks, 2, 1, 4).doNormal()
            getBlock(self.blocks, 2, 2, 4).doNormal()
  
            public void remove1(sprite):
                getBlock(self.blocks, 1, 1, 4).fadeTo((0.0, 0.0, 0.0, 0.0), self.currentTime, 0.1, remove2)
            public void remove2(sprite):
                getBlock(self.blocks, 2, 1, 4).fadeTo((0.0, 0.0, 0.0, 0.0), self.currentTime, 0.1, remove3)
            public void remove3(sprite):
                getBlock(self.blocks, 1, 2, 4).fadeTo((0.0, 0.0, 0.0, 0.0), self.currentTime, 0.1, remove4)
            public void remove4(sprite):
                getBlock(self.blocks, 2, 2, 4).fadeTo((0.0, 0.0, 0.0, 0.0), self.currentTime, 0.1, done)
                
            public void done (sprite):
                self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.5, fallDown)
                
            remove1(None)
                
   
        public void fallDown(sprite):

            b = getBlock(self.blocks, 1, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
            b = getBlock(self.blocks, 1, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)   
            b = getBlock(self.blocks, 2, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
            b = getBlock(self.blocks, 2, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
                
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, fallDown2)
      
        public void fallDown2(sprite):
            b = getBlock(self.blocks, 1, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
            b = getBlock(self.blocks, 1, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)   
            b = getBlock(self.blocks, 2, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
            b = getBlock(self.blocks, 2, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy + 1, self.currentTime)
                
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.2, doBlink2)
                 
        public void doBlink2(sprite):
            getBlock(self.blocks, 1, 0, 4).doBlink()
            getBlock(self.blocks, 1, 3, 4).doBlink()
            getBlock(self.blocks, 2, 0, 4).doBlink()
            getBlock(self.blocks, 2, 3, 4).doBlink()
            getBlock(self.blocks, 0, 2, 4).doBlink()
            getBlock(self.blocks, 0, 3, 4).doBlink()
            getBlock(self.blocks, 3, 2, 4).doBlink()
            getBlock(self.blocks, 3, 3, 4).doBlink()
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.5, fallUp)
            
        public void fallUp(sprite):
            getBlock(self.blocks, 1, 0, 4).doNormal()
            getBlock(self.blocks, 1, 3, 4).doNormal()
            getBlock(self.blocks, 2, 0, 4).doNormal()
            getBlock(self.blocks, 2, 3, 4).doNormal()
            getBlock(self.blocks, 0, 2, 4).doNormal()
            getBlock(self.blocks, 0, 3, 4).doNormal()
            getBlock(self.blocks, 3, 2, 4).doNormal()
            getBlock(self.blocks, 3, 3, 4).doNormal()
        
            b = getBlock(self.blocks, 1, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            b = getBlock(self.blocks, 1, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)   
            b = getBlock(self.blocks, 2, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            b = getBlock(self.blocks, 2, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, fallUp2)
            
        public void fallUp2(sprite):
            b = getBlock(self.blocks, 1, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            b = getBlock(self.blocks, 1, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)   
            b = getBlock(self.blocks, 2, 0, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            b = getBlock(self.blocks, 2, 1, 4)
            b.moveToBoardCoord(b.boardx, b.boardy - 1, self.currentTime)
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.5, addBlocks)
                      
        public void addBlocks(sprite):
        
            public void add1(sprite):
                getBlock(self.blocks, 2, 2, 4).fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, doBlink3)
            public void add2(sprite):
                getBlock(self.blocks, 1, 2, 4).fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, add1)
            public void add3(sprite):
                getBlock(self.blocks, 2, 1, 4).fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, add2)
            public void add4(sprite):
                getBlock(self.blocks, 1, 1, 4).fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 0.1, add3)
                
            add4(None)
            
        public void doBlink3(sprite):
            getBlock(self.blocks, 1, 1, 4).doBlink()
            getBlock(self.blocks, 1, 2, 4).doBlink()
            getBlock(self.blocks, 2, 1, 4).doBlink()            
            getBlock(self.blocks, 2, 2, 4).doBlink()
            
            self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), self.currentTime, 1.0, turnRight)   
                   
            
        self.marker2.fadeTo((1.0, 1.0, 1.0, 1.0), currentTime, 1.0, turnLeft)    

    public void hide(self):
        self.marker2.clearColCallbacks()
        
        for b in self.blocks:
            self.subSprites.remove(b)
                
        self.subSprites.remove(self.marker2)   
        pass
            

           

    */

	public enum State { NORMAL, TRANSITION; }
	
	private static Scene_Tutorial instance = null;
	
	private Font titleFont;
	private Font textFont;
	
	private int pageCount;
	private int currentPage;
	private int newPage;
	
	private List<Page> pages;
	
	private State state;
	
	private Page page_welcome;
	
    private Scene_Tutorial() {
        this.updateBlocker = true;
        this.renderBlocker = false;
        
        this.titleFont = new Font("data/font_fat.png", 8, 8);
        this.textFont = new Font("data/font_clean.png", 8, 8);

        this.pageCount = 1;
        this.page_welcome = new Page_Welcome(160, 120, 1, this.pageCount);
        /*this.page_controls = Page_Controls(160, 120, 2, this.pageCount)
        this.page_goal = Page_Goal(160, 120, 3, this.pageCount)
        this.page_level = Page_Level(160, 120, 4, this.pageCount)
        this.page_timer = Page_Timer(160, 120, 5, this.pageCount)
        this.page_advanced = Page_Advanced(160, 120, 6, this.pageCount)
        this.page_combos = Page_Combos(160, 120, 7, this.pageCount)*/   
     
        /*
        this.pages = {0 : this.page_welcome,
                      1 : this.page_controls,
                      2 : this.page_goal,
                      3 : this.page_level,
                      4 : this.page_timer,
                      5 : this.page_advanced,
                      6 : this.page_combos}
        */
        
        this.pages = new LinkedList<Page>();
        this.pages.add(this.page_welcome);
        
        this.currentPage = 0;
        this.newPage = 0;
        this.addSprite(this.pages.get(this.currentPage));
        this.pages.get(this.currentPage).fadeTo(new Color(1, 1, 1, 1), 0, 0.2, null);
        
        this.state = State.NORMAL;
    }

    public static Scene_Tutorial getInstance() {
    	if(Scene_Tutorial.instance == null) {
    		Scene_Tutorial.instance = new Scene_Tutorial(); 
    	}
    	
    	return Scene_Tutorial.instance;
    }
    
    public void turnToPage(int page) {

        if(page >= this.pageCount) {
            page = this.pageCount - 1;
        }
        if(page < 0) {
            page = 0;
        }

        if(page != this.currentPage) {
            this.state = State.TRANSITION;
       
            this.pages.get(this.newPage).clearColCallbacks();
            this.pages.get(this.currentPage).clearColCallbacks();
            
            this.newPage = page;
            
            SpriteCallback switchPage = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Scene_Tutorial scene_tutorial = (Scene_Tutorial) getArg(0);
					scene_tutorial.removeSprite(sprite);
	                sprite.clearColCallbacks();

	                scene_tutorial.addSprite(scene_tutorial.pages.get(scene_tutorial.newPage));
	                //scene_tutorial.pages.get(scene_tutorial.newPage).show(scene_tutorial.currentTime);
	                scene_tutorial.pages.get(scene_tutorial.newPage).fadeTo(new Color(1, 1, 1, 1), scene_tutorial.currentTime, 0.1, null);
	                scene_tutorial.pages.get(scene_tutorial.newPage).moveTo(new Point(160, 120), scene_tutorial.currentTime, 0.1, null);
	                scene_tutorial.state = State.NORMAL;
				}
            };
 
            switchPage.setArgs(this);
            
            this.pages.get(this.newPage).setPos(new Point(160 + (100 * (this.newPage - this.currentPage)), 120));
            this.pages.get(this.currentPage).moveTo(new Point(160 + (100 * -(this.newPage - this.currentPage)), 120), this.currentTime, 0.1, null);
            this.pages.get(this.currentPage).fadeTo(new Color(1, 1, 1, 0), this.currentTime, 0.1, switchPage);
        }
        
        this.currentPage = this.newPage;
    }
/*        
    public void show(self):
        print self, "is showing"
        self.state = self.statelist["normal"]
        
    public void hide(self):
        print self, "is hiding"
        
    public void handleEvent(self, event):
        if event.type == KEYDOWN:
            key = event.key

            if key == K_ESCAPE:
                public void killDialog(sprite):
                    SceneHandler().removeScene(Scene_DialogYesNo())
                public void quit_game():
                    SceneHandler().removeScene(Scene_DialogYesNo())
                    SceneHandler().removeScene(scene_maingame.Scene_MainGame())
                    SceneHandler().removeScene(self)
                public void do_nothing():
                    Scene_DialogYesNo().remove(killDialog)
                    
                Scene_DialogYesNo().setQuery("Do you want to exit to the menu?", quit_game, do_nothing)
                SceneHandler().pushScene(Scene_DialogYesNo())
            
            if (key == K_RETURN):
                scene_maingame.Scene_MainGame().startGame()
                SceneHandler().removeScene(self)
            
            if (self.state == self.statelist["normal"]):
                if (key == K_LEFT):
                    self.turnToPage(self.currentPage - 1)
                    
                if (key == K_RIGHT):
                    self.turnToPage(self.currentPage + 1)

        if event.type == EVENT_TIMER_STATE_CHANGED:
            self.page_timer.setTimerState(event.state)
                
                
        return True
 */
	public List<Block> createBlockGroup(int posx, int posy, int[] types, int size) {
		List<Block> blocks = new LinkedList<Block>();
	    
	    for(int i=0; i<size; i++) {
	    	for(int j=0; j<size; j++) {
	            Block b = new Block(j, i, types[i*size + j], posx, posy);
	            blocks.add(b);
	    	}
	    }
	    
	    return blocks;
	}
                 
	public Block getBlock(List<Block> blocks, int x, int y, int size) {
	    return blocks.get(y * size + x);
	}

	public void setBlock(List<Block> blocks, int x, int y, int size, Block block) {
	    blocks.set(y * size + x, block);
	}
	
	public void rotateBlocksInGroup(List<Block> blocks, int blockx, int blocky, int size, double currentTime, boolean left) {
	    Block block1 = getBlock(blocks, blockx - 1, blocky - 1, size); 
	    Block block2 = getBlock(blocks, blockx, blocky - 1, size);
	    Block block3 = getBlock(blocks, blockx - 1, blocky, size);
	    Block block4 = getBlock(blocks, blockx, blocky, size);
	             
	    if(left) {
	        block1.moveToBoardCoord(block1.getBoardX(),
	                                block1.getBoardY() + 1, currentTime);
	        block2.moveToBoardCoord(block2.getBoardX() - 1,
	                                block2.getBoardY(), currentTime);
	        block3.moveToBoardCoord(block3.getBoardX() + 1,
	                                block3.getBoardY(), currentTime);
	        block4.moveToBoardCoord(block4.getBoardX(),
	                                block4.getBoardY() - 1, currentTime);
	    } else {
	        block1.moveToBoardCoord(block1.getBoardX() + 1,
	                                block1.getBoardY(), currentTime);
	        block2.moveToBoardCoord(block2.getBoardX(),
	                                block2.getBoardY() + 1, currentTime);
	        block3.moveToBoardCoord(block3.getBoardX(),
	                                block3.getBoardY() - 1, currentTime);
	        block4.moveToBoardCoord(block4.getBoardX() - 1,
	                                block4.getBoardY(), currentTime);
	    }
	    setBlock(blocks, block1.getBoardX(), block1.getBoardY(), size, block1);
	    setBlock(blocks, block2.getBoardX(), block2.getBoardY(), size, block2);
	    setBlock(blocks, block3.getBoardX(), block3.getBoardY(), size, block3);
	    setBlock(blocks, block4.getBoardX(), block4.getBoardY(), size, block4);
	}
}

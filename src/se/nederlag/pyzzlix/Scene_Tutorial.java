package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;
import se.nederlag.pyzzlix.events.EventTimeState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

		protected Text titleText;
		protected List<Text> infoText;
	
	    public Page(int x, int y, int page, int pageCount) {
	        this.titleFont = new Font("data/font_fat.png", 8, 8);
	        this.textFont = new Font("data/font_clean.png", 4, 8);

	        this.width = 196;
	        this.height = 166;
	        
	        this.background = new Sprite(new Texture(Gdx.files.internal("data/splashbg.png")), 0, 0, 196, 166);
	        this.background.setPos(new Point(-this.width/2, -this.height/2));
	        this.setSize(this.width,this.height);
	        this.setPos(new Point(x, y));
	        this.setCenter(new Point(this.width/2, this.height/2));
	        
	        this.addSubSprite(this.background);
	
	        this.enterText = new Text(0, -this.height/2 + 4, this.textFont, "Press Enter to start the game");
	        this.enterText.setAnchor(Text.Anchor.CENTER);
	        
	        this.addSubSprite(this.enterText);
	
	        this.leftArrow = new Text(-this.width/2 + 4, -4, this.titleFont, "<");
	        this.rightArrow = new Text(this.width/2 - 12, -4, this.titleFont, ">");
	        
	        this.addSubSprite(this.leftArrow);
	        this.addSubSprite(this.rightArrow);
	
	
	        this.pageText = new Text(this.width/2 - 6, -this.height/2 + 4, this.titleFont, String.format("%d/%d", page, pageCount));
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
	    
	    public void show(double time) {	    	
	    }

	    public void hide() {	    	
	    }
	}

	class Page_Welcome extends Page {
	    private Text welcomeText;
		private Scene_MainMenu.Logo logo;
		
		public Page_Welcome(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 24;
	
	        this.welcomeText = new Text(dposx, dposy, this.titleFont, "WELCOME\nTO\n");
	        this.welcomeText.setAnchor(Text.Anchor.CENTER);
	        this.welcomeText.setScale(new Point(1.5, 1.5));
	
	        this.logo = Scene_MainMenu.getInstance().newLogo(0, this.height/2-46); 
	        this.logo.setScale(new Point(0.5, 0.5));
	        this.logo.setCycling(false);
	        
	        this.addSubSprite(this.welcomeText);
	        this.addSubSprite(this.logo);
	
	        dposy -= 60;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
	                                  "This tutorial will explain how to play\n" +
	                                  "Pyzzlix! If this is your first time\n" +
	                                  "playing, skip this at your own peril..."));
	
	        dposy -= 24;
	
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
	                                  "Use the left and right ARROW keys to\n" + 
	                                  "navigate the pages of this tutorial."));
	                  
	        dposy -= 24;                      
	                                  
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
	
	class Page_Controls extends Page {
		private Marker marker;
		private Marker marker2;
		private List<Block> blocks;
		
		private int block_x;
		private int block_y;
	    
		public Page_Controls(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "BASIC CONTROLS");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 20;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, "When playing the game, use the\n" +
	                												"ARROW keys to move the marker."));
	                
	        dposy -= 35;
	        
	        this.marker = new Marker();
	        this.marker.setPos(new Point(dposx - 16, dposy));
	        this.addSubSprite(this.marker);
	        
	        dposy -= 18;        
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
	                             "The Z and X keys rotates the blocks\n" +
	                             "beneath the marker."));
	        
	        dposy -= 35;
	        
	        //self.blocktypes = [0, 1,                         2, 0]
	        this.block_x = dposx - 8;
	        this.block_y = dposy + 24;
	             
	        this.marker2 = new Marker();
	        this.marker2.setPos(new Point(dposx - 16, dposy));
	        
	        dposy -= 18;
	        
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
	                                  "Z rotates counter-clockwise\n" +
	                                  "and X rotates clockwise."));
	        
	        for(Text t : this.infoText) {
	            t.setAnchor(Text.Anchor.CENTER);
	            this.addSubSprite(t);
	        }
		}
	
		public void showInit() {        
	        this.blocks = Scene_Tutorial.createBlockGroup(this.block_x, this.block_y, new int[]{1, 0, 0, 2}, 2);
	        for(Block b : this.blocks) {
	            this.addSubSprite(b);
	        }
	            
	        this.addSubSprite(this.marker2);
		}
	
	    public void show(double currentTime) {
	        this.showInit();
	     
	        SpriteCallback turnLeft = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Controls page = (Page_Controls) getArg(0);
					SpriteCallback turnRight = (SpriteCallback) getArg(1);
					
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 1, 1, 2, page.currentTime, true);
					page.marker2.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, turnRight); 
				}
	        };
	
	        SpriteCallback turnRight = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Controls page = (Page_Controls) getArg(0);
					SpriteCallback turnLeft = (SpriteCallback) getArg(1);
					
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 1, 1, 2, page.currentTime, false);
					page.marker2.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, turnLeft);
				}
	        };
	
	        turnLeft.setArgs(this, turnRight);
	        turnRight.setArgs(this, turnLeft);
	        this.marker2.fadeTo(new Color(1, 1, 1, 1), currentTime, 1.0, turnLeft);  
	    }
	    
	    public void hide() {
	        this.marker2.clearColCallbacks();
	        for(Block b : this.blocks) {
	            this.removeSubSprite(b);
	        }
	        
	        this.removeSubSprite(this.marker2);
	    }
	}
	
	class Page_Goal extends Page {
		private Marker marker;
		private List<Block> blocks;
		
		private int block_x;
		private int block_y;
	    
		public Page_Goal(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "THE GOAL");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 20;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, "The goal of the game is to get a high\n" +
	        														"score before the time runs out."));
	
	                
	        dposy -= 32;        
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
					                "Score and extended time are awarded by\n" +
					                "removing blocks. Remove blocks by\n" +
					                "creating loops of same-colored blocks!"));
	        
	        dposy -= 35;
	        
	        this.block_x = dposx - 16;
	        this.block_y = dposy + 20;
	             
	        this.marker = new Marker();
	        this.marker.setPos(new Point(dposx - 8, dposy - 20));
	
	        dposy -= 35;
	
	        this.infoText.add(new Text(dposx, dposy, this.textFont,	
		    "The simplest loop is a 2x2 square."));
		
		    for(Text t : this.infoText) {
		            t.setAnchor(Text.Anchor.CENTER);
		            this.addSubSprite(t);
		    }
		}
	
		public void showInit() {        
	        this.blocks = Scene_Tutorial.createBlockGroup(this.block_x, this.block_y, new int[]{0, 0, 1, 0, 2, 0, 2, 1, 2}, 3);
	        for(Block b : this.blocks) {
	            this.addSubSprite(b);
	        }
	            
	        this.addSubSprite(this.marker);
		}
	
	    public void show(double currentTime) {
	        this.showInit();
	     
	        SpriteCallback turnLeft = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Goal page = (Page_Goal) getArg(0);
					SpriteCallback doBlink = (SpriteCallback) getArg(1);
					
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 2, 2, 3, page.currentTime, true);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.5, doBlink); 
				}
	        };
	
	        SpriteCallback turnRight = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Goal page = (Page_Goal) getArg(0);
					SpriteCallback turnLeft = (SpriteCallback) getArg(1);
	
			        for(int i=0; i<3; i++) {
			           for(int j=0; j<3; j++) {
			        	   Scene_Tutorial.getBlock(page.blocks, i, j, 3).doNormal();
			           }
			        }
			                    
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 2, 2, 3, page.currentTime, false);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.5, turnLeft);
				}
	        };
	
	        SpriteCallback doBlink = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Goal page = (Page_Goal) getArg(0);
					SpriteCallback turnRight = (SpriteCallback) getArg(1);
		            
					Scene_Tutorial.getBlock(page.blocks, 0, 0, 3).doBlink();
					Scene_Tutorial.getBlock(page.blocks, 1, 0, 3).doBlink();
					Scene_Tutorial.getBlock(page.blocks, 0, 1, 3).doBlink();           
					Scene_Tutorial.getBlock(page.blocks, 1, 1, 3).doBlink();
		            
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, turnRight);
				}
	        };
	
	        turnLeft.setArgs(this, doBlink);
	        turnRight.setArgs(this, turnLeft);
	        doBlink.setArgs(this, turnRight);
	        
	        this.marker.fadeTo(new Color(1, 1, 1, 1), currentTime, 1.0, turnLeft);  
	    }
	    
	    public void hide() {
	        this.marker.clearColCallbacks();
	        for(Block b : this.blocks) {
	            this.removeSubSprite(b);
	        }
	        
	        this.removeSubSprite(this.marker);
	    }
	}
	
	class Page_Level extends Page {
		private Levelboard levelboard;
	
		private int level;
		private int blocks;
	    
		public Page_Level(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "LEVELS");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 36;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, 
	                "Each level has a Special Block color.\n" +     
	                "In order to advance to the next level\n" +
	                "a specific number of Special Blocks\n" +
	                "needs to be removed."));
	
	                
	        dposy -= 26;        
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
	                "The Special Block and the progress of\n" +
	                "the current level is displayed in the\n" +
	                "level board."));
	        
	        dposy -= 49;
	        
	        this.level = 1;
	        this.blocks = 0;
	        this.levelboard = new Levelboard();
	        this.levelboard.setPos(new Point(dposx - 44, dposy));
	        this.addSubSprite(this.levelboard);
	        
	        dposy -= 17;
	        this.infoText.add(new Text(dposx, dposy, this.textFont,	
	        		"You will score more points for each removed\n" +
	        		"block on higher levels!"));
		
		    for(Text t : this.infoText) {
		            t.setAnchor(Text.Anchor.CENTER);
		            this.addSubSprite(t);
		    }
		}
	
		public void newLevel() {
	        this.level += 1;
	        if (this.level > 99) {
	            this.level = 1;
	        }
	            
	        this.levelboard.setNewLevel(this.level, this.level % 3, 20);    
		}
		
		public void showInit() {        
	        this.level = 1;
	        this.levelboard.setNewLevel(1, 0, 20);
		}
	
	    public void show(double currentTime) {
	        this.showInit();     
	
	        SpriteCallback addBlock1 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback addBlock2 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback addBlock3 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback addBlock4 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback addBlock5 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback addBlock6 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					page.blocks += 1;
					page.levelboard.updateLevelboard(page.blocks);
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback checkAndDelay = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Level page = (Page_Level) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					
					if(page.blocks > 20) {
						page.newLevel();
						page.blocks = 0;
					}
					
					page.levelboard.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, next);
				}
	        };
	       
	        addBlock1.setArgs(this, addBlock2);
	        addBlock2.setArgs(this, addBlock3);
	        addBlock3.setArgs(this, addBlock4);
	        addBlock4.setArgs(this, addBlock5);
	        addBlock5.setArgs(this, addBlock6);
	        addBlock6.setArgs(this, checkAndDelay);
	        checkAndDelay.setArgs(this, addBlock1);
	        
	        this.levelboard.fadeTo(new Color(1, 1, 1, 1), this.currentTime, 1.0, addBlock1);
	    }
	    
	    public void hide() {
	    	this.levelboard.clearColCallbacks();
	    }
	}
	
	class Page_Timer extends Page {
		private Hourglass hourglass;
	
		public Page_Timer(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "THE TIMER");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 18;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, 
                    "If the hourglass on the timer board\n" +
            		"reaches zero the game is over."));
	
	                
	        dposy -= 18;        
	        this.infoText.add(new Text(dposx, dposy, this.textFont,
                    "Before running out of time, the borders\n" +
	        		"will flash red as a warning."));
	        
	        dposy -= 52;

	        this.hourglass = new Hourglass();
	        this.hourglass.setPos(new Point(dposx - 22, dposy));
	        this.hourglass.setScale(new Point(0.5, 0.5));
	        this.hourglass.scaleValue(0.2);

	        this.addSubSprite(this.hourglass);
	        
	        dposy -= 36;
	        this.infoText.add(new Text(dposx, dposy, this.textFont,	
                    "When removing blocks, the timer will also\n" +
                    "be a short while. The timer can only be\n" +
                    "stopped a maximum of 5 seconds though.\n" +
                    "Use this pause to plan your next move!"));
		
		    for(Text t : this.infoText) {
		            t.setAnchor(Text.Anchor.CENTER);
		            this.addSubSprite(t);
		    }
		}
	
		public void showInit() {        
		}
	
	    public void show(double currentTime) {
	        this.showInit();     
	        this.hourglass.reset(200);
	    }
	    
	    public void setTimerState(Hourglass.State state) {
	        if(state == Hourglass.State.LOW) {
		        SpriteCallback saveTheDay = new SpriteCallback() {
					public void callback(Sprite sprite, double currenttime) {
						Page_Timer page = (Page_Timer) getArg(0);
						
						page.hourglass.addValue(200);
						page.hourglass.addPause(1.0);
					}
				};
				saveTheDay.setArgs(this);
				
	            this.hourglass.pulseBorder(new Color(1, 0, 0, 0), new Color(1, 0, 0, 1), 0.5);
	            this.hourglass.fadeTo(new Color(1, 1, 1, 1), this.currentTime, 2.5, saveTheDay);
	        }
	            
	        if(state == Hourglass.State.NORMAL || state == Hourglass.State.HIGH) {
	        	this.hourglass.stopPulseBorder();
	        }
	    }
	
	    public void hide() {
	    }
	}

	class Page_Advanced extends Page {
		private Marker marker;
		private List<Block> blocks;
		
		private int block_x;
		private int block_y;
	    
		public Page_Advanced(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "ADVANCED");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 26;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, 
                    "The loops you create do not have to be\n" +
                    "symmetric, they can have any shape as long\n" +
                    "as all blocks in the loop are connected."));
	
	                
	        
	        dposy -= 32;
	        
	        this.block_x = dposx - 24;
	        this.block_y = dposy + 20;
	             
	        this.marker = new Marker();
	        this.marker.setPos(new Point(dposx, dposy - 36));
	
	        dposy -= 72;
	
	        this.infoText.add(new Text(dposx, dposy, this.textFont,	
                    "When encircling other blocks in a loop,\n" +
                    "they will also be removed. Large loops\n" +
                    "gives more points than the blocks\n" +
                    "individually."));
		
		    for(Text t : this.infoText) {
		            t.setAnchor(Text.Anchor.CENTER);
		            this.addSubSprite(t);
		    }
		}
	
		public void showInit() {        
	        this.blocks = Scene_Tutorial.createBlockGroup(this.block_x, this.block_y, new int[]{1, 0, 0, 0,
                    0, 0, 2, 0,
                    0, 1, 0, 0,
                    0, 0, 2, 0}, 4);
	        
	        for(Block b : this.blocks) {
	            this.addSubSprite(b);
	        }
	            
	        this.addSubSprite(this.marker);
		}
	
	    public void show(double currentTime) {
	        this.showInit();
	     
	        SpriteCallback turnLeft = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Advanced page = (Page_Advanced) getArg(0);
					SpriteCallback doBlink = (SpriteCallback) getArg(1);
					
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 3, 3, 4, page.currentTime, true);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.5, doBlink); 
				}
	        };
	
	        SpriteCallback turnRight = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Advanced page = (Page_Advanced) getArg(0);
					SpriteCallback turnLeft = (SpriteCallback) getArg(1);
	
			        for(int i=0; i<4; i++) {
			           for(int j=0; j<4; j++) {
			        	   Scene_Tutorial.getBlock(page.blocks, i, j, 4).doNormal();
			           }
			        }
			                    
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 3, 3, 4, page.currentTime, false);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.5, turnLeft);
				}
	        };
	
	        SpriteCallback doBlink = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Advanced page = (Page_Advanced) getArg(0);
					SpriteCallback turnRight = (SpriteCallback) getArg(1);
		            
					for(Block b : page.blocks) {
						b.doBlink();
					}
					
					Scene_Tutorial.getBlock(page.blocks, 0, 0, 4).doNormal();
					Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).doNormal();
					Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).doNormal();           
					Scene_Tutorial.getBlock(page.blocks, 3, 3, 4).doNormal();
					
					SpriteCallback doWholeBlink = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Advanced page = (Page_Advanced) getArg(0);
							SpriteCallback turnRight = (SpriteCallback) getArg(1);

							Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).doBlink();
							Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).doBlink();           
							
							page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, turnRight);
						}
					};
					doWholeBlink.setArgs(page, turnRight);
		            
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, doWholeBlink);
				}
	        };
	
	        turnLeft.setArgs(this, doBlink);
	        turnRight.setArgs(this, turnLeft);
	        doBlink.setArgs(this, turnRight);
	        
	        this.marker.fadeTo(new Color(1, 1, 1, 1), currentTime, 1.0, turnLeft);  
	    }
	    
	    public void hide() {
	        this.marker.clearColCallbacks();
	        for(Block b : this.blocks) {
	            this.removeSubSprite(b);
	        }
	        
	        this.removeSubSprite(this.marker);
	    }
	}
	
	class Page_Combos extends Page {
		private Marker marker;
		private List<Block> blocks;
		
		private int block_x;
		private int block_y;
	    
		public Page_Combos(int x, int y, int page, int pageCount) {
	        super(x, y, page, pageCount);
	
	        int dposx = 0;
	        int dposy = this.height/2 - 10 - 12;
	
	        this.titleText = new Text(dposx, dposy, this.titleFont, "COMBOS");
	        this.titleText.setAnchor(Text.Anchor.CENTER);
	        this.titleText.setScale(new Point(1.5, 1.5));
	
	        this.addSubSprite(this.titleText);
	
	        dposy -= 26;
	
	        this.infoText = new LinkedList<Text>();
	        this.infoText.add(new Text(dposx, dposy, this.textFont, 
                    "If a loop is created as a result of blocks\n" +
                    "landing on other blocks after a fall, \n" +
                    "a combo is performed."));
	
	                
	        
	        dposy -= 32;
	        
	        this.block_x = dposx - 24;
	        this.block_y = dposy + 20;
	             
	        this.marker = new Marker();
	        this.marker.setPos(new Point(dposx, dposy - 36));
	
	        dposy -= 72;
	
	        this.infoText.add(new Text(dposx, dposy, this.textFont,	
                    "Combos give an extra big score bonus.\n" +
                    "Successive combos will give an increasingly\n" +
                    "large score bonus for each combo.\n" +
                    "Try to perform many combos!"));
		
		    for(Text t : this.infoText) {
		            t.setAnchor(Text.Anchor.CENTER);
		            this.addSubSprite(t);
		    }
		}
	
		public void showInit() {        
	        this.blocks = Scene_Tutorial.createBlockGroup(this.block_x, this.block_y, new int[]{1, 2, 2, 0,
                    0, 1, 1, 2,
                    2, 1, 2, 1,
                    2, 2, 2, 2}, 4);
	        
	        for(Block b : this.blocks) {
	            this.addSubSprite(b);
	        }
	            
	        this.addSubSprite(this.marker);
		}
	
	    public void show(double currentTime) {
	        this.showInit();
	     
	        SpriteCallback turnLeft = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 3, 3, 4, page.currentTime, true);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.5, next); 
				}
	        };
	
	        SpriteCallback turnRight = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
	
			        for(int i=0; i<4; i++) {
			           for(int j=0; j<4; j++) {
			        	   Scene_Tutorial.getBlock(page.blocks, i, j, 4).doNormal();
			           }
			        }
			                    
					Scene_Tutorial.rotateBlocksInGroup(page.blocks, 3, 3, 4, page.currentTime, false);
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.5, next);
				}
	        };
	
	        SpriteCallback doBlink1 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
		            
					Scene_Tutorial.getBlock(page.blocks, 1, 1, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 2, 2, 4).doBlink();
					
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, next);
				}
	        };
	
	        SpriteCallback removeBlocks = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					
					SpriteCallback remove1 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 1, 1, 4).fadeTo(new Color(0, 0, 0, 0), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback remove2 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).fadeTo(new Color(0, 0, 0, 0), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback remove3 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).fadeTo(new Color(0, 0, 0, 0), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback remove4 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 2, 2, 4).fadeTo(new Color(0, 0, 0, 0), page.currentTime, 0.1, next);
						}
					};

					SpriteCallback done = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.5, next);
						}
					};
		            
					remove1.setArgs(page, remove2);
					remove2.setArgs(page, remove3);
					remove3.setArgs(page, remove4);
					remove4.setArgs(page, done);
					done.setArgs(page, next);
					
					Scene_Tutorial.getBlock(page.blocks, 1, 1, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 2, 2, 4).doNormal();
					
					remove1.callback(null, page.currentTime);
				}
	        };

	        SpriteCallback fallDown = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					
		            Block b = Scene_Tutorial.getBlock(page.blocks, 1, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 1, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);   
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
					
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback fallDown2 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);

					Block b = Scene_Tutorial.getBlock(page.blocks, 1, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 1, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);   
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() + 1, page.currentTime);
		            
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.2, next);
				}
	        };
	
	        SpriteCallback doBlink2 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
		            
					Scene_Tutorial.getBlock(page.blocks, 1, 0, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 1, 3, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 2, 0, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 2, 3, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 0, 2, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 0, 3, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 3, 2, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 3, 3, 4).doBlink();

					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.5, next);
				}
	        };
	
	        SpriteCallback fallUp = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
		            
					Scene_Tutorial.getBlock(page.blocks, 1, 0, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 1, 3, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 2, 0, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 2, 3, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 0, 2, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 0, 3, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 3, 2, 4).doNormal();
		            Scene_Tutorial.getBlock(page.blocks, 3, 3, 4).doNormal();
		        
					Block b = Scene_Tutorial.getBlock(page.blocks, 1, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 1, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);   
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            
		            page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
				}
	        };
	
	        SpriteCallback fallUp2 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
		            
					Block b = Scene_Tutorial.getBlock(page.blocks, 1, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 1, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);   
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 0, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            b = Scene_Tutorial.getBlock(page.blocks, 2, 1, 4);
		            b.moveToBoardCoord(b.getBoardX(), b.getBoardY() - 1, page.currentTime);
		            
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, next);
				}
	        };
	
	        SpriteCallback addBlocks = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);
					
					SpriteCallback add1 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 1, 1, 4).fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback add2 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback add3 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
						}
					};
		            
					SpriteCallback add4 = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							Page_Combos page = (Page_Combos) getArg(0);
							SpriteCallback next = (SpriteCallback) getArg(1);
							
							Scene_Tutorial.getBlock(page.blocks, 2, 2, 4).fadeTo(new Color(1, 1, 1, 1), page.currentTime, 0.1, next);
						}
					};

					add4.setArgs(page, add3);
					add3.setArgs(page, add2);
					add2.setArgs(page, add1);
					add1.setArgs(page, next);
					
					add4.callback(null, page.currentTime);
				}
	        };
	
	        SpriteCallback doBlink3 = new SpriteCallback() {
				public void callback(Sprite sprite, double currenttime) {
					Page_Combos page = (Page_Combos) getArg(0);
					SpriteCallback next = (SpriteCallback) getArg(1);

					Scene_Tutorial.getBlock(page.blocks, 1, 1, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 1, 2, 4).doBlink();
		            Scene_Tutorial.getBlock(page.blocks, 2, 1, 4).doBlink();           
		            Scene_Tutorial.getBlock(page.blocks, 2, 2, 4).doBlink();
		            
					page.marker.fadeTo(new Color(1, 1, 1, 1), page.currentTime, 1.0, next);
				}
	        };
	
	        turnRight.setArgs(this, turnLeft);
	        turnLeft.setArgs(this, doBlink1);
	        doBlink1.setArgs(this, removeBlocks);
	        removeBlocks.setArgs(this, fallDown);
	        fallDown.setArgs(this, fallDown2);
	        fallDown2.setArgs(this, doBlink2);
	        doBlink2.setArgs(this, fallUp);
	        fallUp.setArgs(this, fallUp2);
	        fallUp2.setArgs(this, addBlocks);
	        addBlocks.setArgs(this, doBlink3);
	        doBlink3.setArgs(this, turnRight);
	        
	        this.marker.fadeTo(new Color(1, 1, 1, 1), currentTime, 1.0, turnLeft);  
	    }
	    
	    public void hide() {
	        this.marker.clearColCallbacks();
	        for(Block b : this.blocks) {
	            this.removeSubSprite(b);
	        }
	        
	        this.removeSubSprite(this.marker);
	    }
	}

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
	private Page page_controls;
	private Page page_goal;
	private Page page_level;
	private Page_Timer page_timer;
	private Page page_advanced;
	private Page page_combos;
	
    private Scene_Tutorial() {
        this.updateBlocker = true;
        this.renderBlocker = false;
        this.titleFont = new Font("data/font_fat.png", 8, 8);
        this.textFont = new Font("data/font_clean.png", 8, 8);

        this.pageCount = 7;

        this.page_welcome = new Page_Welcome(0, 0, 1, this.pageCount);
        this.page_controls = new Page_Controls(0, 0, 2, this.pageCount);
        this.page_goal = new Page_Goal(160, 120, 3, this.pageCount);
        this.page_level = new Page_Level(160, 120, 4, this.pageCount);
        this.page_timer = new Page_Timer(160, 120, 5, this.pageCount);
        this.page_advanced = new Page_Advanced(160, 120, 6, this.pageCount);
        this.page_combos = new Page_Combos(160, 120, 7, this.pageCount);   
     
        this.pages = new LinkedList<Page>();
        this.pages.add(this.page_welcome);
        this.pages.add(this.page_controls);
        this.pages.add(this.page_goal);
        this.pages.add(this.page_level);
        this.pages.add(this.page_timer);
        this.pages.add(this.page_advanced);
        this.pages.add(this.page_combos);
        
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
					int current_page = (Integer) getArg(1);
					scene_tutorial.removeSprite(sprite);
	                sprite.clearColCallbacks();
	                scene_tutorial.pages.get(current_page).hide();

	                scene_tutorial.addSprite(scene_tutorial.pages.get(scene_tutorial.newPage));
	                scene_tutorial.pages.get(scene_tutorial.newPage).show(scene_tutorial.currentTime);
	                scene_tutorial.pages.get(scene_tutorial.newPage).fadeTo(new Color(1, 1, 1, 1), scene_tutorial.currentTime, 0.1, null);
	                scene_tutorial.pages.get(scene_tutorial.newPage).moveTo(new Point(0, 0), scene_tutorial.currentTime, 0.1, null);
	                scene_tutorial.state = State.NORMAL;
				}
            };
 
            switchPage.setArgs(this, this.currentPage);
            
            this.pages.get(this.newPage).setPos(new Point((100 * (this.newPage - this.currentPage)), 0));
            this.pages.get(this.currentPage).moveTo(new Point((100 * -(this.newPage - this.currentPage)), 0), this.currentTime, 0.1, null);
            this.pages.get(this.currentPage).fadeTo(new Color(1, 1, 1, 0), this.currentTime, 0.1, switchPage);
        }
        
        this.currentPage = this.newPage;
    }

    public void show() {
        this.state = State.NORMAL;
    }
        
    public void hide() {    	
    }
        
    public boolean handleEvent(Event event) {
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;

			if(keyevent.state == EventKeyState.State.DOWN) {
	            if(keyevent.key == Input.Keys.ESCAPE) {
	            	SpriteCallback killDialog = new SpriteCallback() {
						public void callback(Sprite sprite, double currenttime) {
							SceneHandler.getInstance().removeScene(Scene_DialogYesNo.getInstance());						}
	            	};
	            	
	            	Callback quit_game = new Callback() {
						public Object call(Object... params) {
							SceneHandler.getInstance().removeScene(Scene_DialogYesNo.getInstance());
							SceneHandler.getInstance().removeScene(Scene_MainGame.getInstance());
							SceneHandler.getInstance().removeScene(Scene_Tutorial.getInstance());
							return null;
						}
	            	};
	            	
	            	Callback do_nothing = new Callback(killDialog) {
						public Object call(Object... params) {
							Scene_DialogYesNo.getInstance().remove((SpriteCallback)args[0]);
							return null;
						}
	            	};
	            	
	                Scene_DialogYesNo.getInstance().setQuery("Do you want to exit to the menu?", quit_game, do_nothing);
	                SceneHandler.getInstance().pushScene(Scene_DialogYesNo.getInstance());
	            }
	                        
	            if(keyevent.key == Input.Keys.ENTER) {
	            	Scene_MainGame.getInstance().startGame();
	            	SceneHandler.getInstance().removeScene(this);
	            }
            
	            if (this.state == State.NORMAL) {
	            	if(keyevent.key == Input.Keys.LEFT) {
	                    this.turnToPage(this.currentPage - 1);
	            	}
	                    
	            	if(keyevent.key == Input.Keys.RIGHT) {
	                	this.turnToPage(this.currentPage + 1);
	            	}
	            }
			}
		}

        if(event.type == Event.Type.TIME_STATE_CHANGED) {
        	EventTimeState timeevent = (EventTimeState) event;  
            this.page_timer.setTimerState(timeevent.state);
        }
                
                
        return true;
	}

	public static List<Block> createBlockGroup(int posx, int posy, int[] types, int size) {
		List<Block> blocks = new LinkedList<Block>();
	    
	    for(int i=0; i<size; i++) {
	    	for(int j=0; j<size; j++) {
	            Block b = new Block(j, i, types[i*size + j], posx, posy);
	            blocks.add(b);
	    	}
	    }
	    
	    return blocks;
	}
                 
	public static Block getBlock(List<Block> blocks, int x, int y, int size) {
	    return blocks.get(y * size + x);
	}

	public static void setBlock(List<Block> blocks, int x, int y, int size, Block block) {
	    blocks.set(y * size + x, block);
	}
	
	public static void rotateBlocksInGroup(List<Block> blocks, int blockx, int blocky, int size, double currentTime, boolean left) {
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

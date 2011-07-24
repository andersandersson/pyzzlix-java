package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Levelboard extends Sprite {
	private Font font;

	private Sprite scorebg;
	private Sprite border;
	private Sprite glow;

	private Text leveltext;
	private Text blockcounttext;
	
	private Color _glow_color;
	private double _glow_duration;
	
	private int blockposx;
	private int blockposy;
	private Block block;

	private int currentblocks;
	private int goalblocks;

	private int level;
	
	private boolean newLevelEventSent;
	private boolean completed;
	
	public Levelboard() {        
		this.font = new Font("data/font_fat.png", 8, 8);

        this.leveltext = new Text(44, 12, this.font, "LEVEL 00");      
        this.leveltext.setAnchor(Text.Anchor.CENTER);
            
        this.blockposx = 20;
        this.blockposy = 30;
        this.block = null;

        this.currentblocks = 0;
        this.goalblocks = 0;

        this.blockcounttext = new Text(this.blockposx + 8, this.blockposy - 4, this.font, ":99/99");
   
        this.scorebg = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.scorebg.setPos(new Point(8, 8));
        this.scorebg.setScale(new Point(72, -32));
        this.scorebg.setCol(new Color(0, 0, 0, 0.5f));

		this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 208, 80, 88, 48);
		this.border.setPos(new Point(0, 0));
        
		this.glow = new Sprite(new Texture(Gdx.files.internal("data/windowglows.png")), 208, 80, 88, 48);
        this.glow.setPos(new Point(0,0));
        this.glow.setCol(new Color(0, 0, 0, 0));
        
        this.addSubSprite(this.scorebg);
        this.addSubSprite(this.blockcounttext);
        this.addSubSprite(this.leveltext);
        this.addSubSprite(this.border);
        this.addSubSprite(this.glow);
        
        this.level = 0;
        this.newLevelEventSent = false;

        this._glow_color = new Color(0, 0, 0, 0);
        this._glow_duration = 0;

        this.completed = false;        
	}

    public void doBlink() {
        SpriteCallback fade_to_done;;
        SpriteCallback fade_from_done;;
        
        fade_to_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Color color = (Color) getArg(0);
        		double duration = (Double) getArg(1);
        		SpriteCallback fade_from_done = (SpriteCallback) getArg(2);
        		sprite.fadeTo(color, currentTime, duration, fade_from_done);
        	}
        };

        fade_from_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Color color = (Color) getArg(0);
        		double duration = (Double) getArg(1);
        		SpriteCallback fade_to_done = (SpriteCallback) getArg(2);
        		sprite.fadeTo(color, currentTime, duration, fade_to_done);
        	}
        };

        fade_to_done.setArgs(new Color(1.0f,1.0f,1.0f,1.0f), 0.05, fade_from_done);
        fade_from_done.setArgs(new Color(0.0f,0.0f,0.0f,0.0f), 0.05, fade_to_done);
        fade_from_done.callback(this.blockcounttext, this.currentTime);
    }    	

    public void updateLevelboard(int currentblocks) {
        if (this.currentblocks != currentblocks) {
            this.block.doPulse();   

            if (!this.completed) {
                this.currentblocks = currentblocks;

                if (this.currentblocks >= this.goalblocks) {
                    this.currentblocks = this.goalblocks;
                    this.completed = true;
                    this.doBlink();

                    if(!this.newLevelEventSent) {
                        this.newLevelEventSent = true;
                        EventHandler.getInstance().post(new Event(Event.Type.LEVEL_UP));
                    }
                }
                
                this.blockcounttext.setText(":" + this.currentblocks +"/" + this.goalblocks);
            }
        }
    }
           
        
    public void setNewLevel(int level, int block, int goalblocks) {
        this.level = level;
        this.newLevelEventSent = false;
    
        this.leveltext.setText("LEVEL " + this.level);   
        
        this.goalblocks = goalblocks;
        this.currentblocks = 0;
            
        this.completed = false;
        this.blockcounttext.clearColCallbacks();
        this.blockcounttext.setCol(new Color(1.0f,1.0f,1.0f,1.0f)); 
        this.blockcounttext.setText(":" + this.currentblocks +"/" + this.goalblocks);
        
        Block oldblock = this.block;
        this.block = new Block(0, 0, block, 0, 0);
        this.block.setPos(new Point(this.blockposx, this.blockposy));
        this.block.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        this.block.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.3, null);
        this.addSubSprite(this.block);
        
        if(oldblock != null) { 
            SpriteCallback removeOldblock = new SpriteCallback() {
            	public void callback(Sprite sprite, double currenttime) {
            		Levelboard levelboard = (Levelboard) getArg(0);
            		levelboard.removeSubSprite(sprite);
            	}
            };
            
            removeOldblock.setArgs(this);
            oldblock.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.0f), this.currentTime, 0.3, removeOldblock);
        }
    }
             

    public void pulseBorder(Color col1, Color col2, double duration) { 
        this._glow_duration = duration;

        SpriteCallback fade_to_done;;
        SpriteCallback fade_from_done;;
        
        fade_to_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Levelboard levelboard = (Levelboard) getArg(0);
        		levelboard._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_from_done = (SpriteCallback) getArg(3);
        		levelboard.glow.fadeTo(levelboard._glow_color, currentTime, duration, fade_from_done);
        	}
        };

        fade_from_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Levelboard levelboard = (Levelboard) getArg(0);
        		levelboard._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_to_done = (SpriteCallback) getArg(3);
        		levelboard.glow.fadeTo(levelboard._glow_color, currentTime, duration, fade_to_done);
        	}
        };

        fade_to_done.setArgs(this, col1, duration, fade_from_done);
        fade_from_done.setArgs(this, col2, duration, fade_to_done);
        
        this.glow.clearColCallbacks();
        fade_from_done.callback(null, this.currentTime);
    }

        
    public void stopPulseBorder() {
	    Color from_col = new Color(this._glow_color);
	    from_col.a = 0.0f;

	    this.glow.clearColCallbacks();
	    this.glow.fadeTo(from_col, this.currentTime, this._glow_duration, null);
    }
}

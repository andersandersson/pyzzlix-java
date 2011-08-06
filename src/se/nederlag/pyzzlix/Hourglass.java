package se.nederlag.pyzzlix;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;
import se.nederlag.pyzzlix.events.EventTimeState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Hourglass extends Sprite {
	public enum State {
		LOW, NORMAL, HIGH, GAMEOVER;
	}
	
	private Font font;
	private Sprite background;
	private Sprite border;
	private Sprite bar;
	private Sprite pausebg;
	private Sprite glow;
	private Text stoptext;
	private Text pausetext;
	private boolean pauseVisible;
	
	private Color _glow_color;
	private double _glow_duration;
	
	private State state;
	
	private int max;
	private int value;
	private double pause;
	private int halted;
	
	public Hourglass() {
		this.font = new Font("data/font_fat.png", 8, 8);
		
		this.bar = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
		this.bar.setScale(new Point(72, 80));
		this.bar.setPos(new Point(8, 8));
		this.bar.setCol(new Color(0,1,0,1));
		
		this.max = 0;
		this.value = 0;
		this.pause = 0;
		this.halted = 0;
		
        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.setScale(new Point(72, 80));
        this.background.setPos(new Point(8, 8));
        this.background.setCol(new Color(0, 0, 0, 0.3f));
        
        this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 208, 136, 88, 96);
		this.border.setPos(new Point(0, 0));
        
        this.pausebg = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.pausebg.setScale(new Point(72, 80));
        this.pausebg.setPos(new Point(8, 8));
        this.pausebg.setCol(new Color(0, 0, 0, 0));

        this.stoptext = new Text(44, 64, this.font, "STOP");
        this.stoptext.setScale(new Point(2, 2));
        this.stoptext.setAnchor(Text.Anchor.CENTER);
        this.stoptext.setCol(new Color(0, 0, 0, 0));
                
        this.pausetext = new Text(44, 40, this.font, "0");
        this.pausetext.setScale(new Point(2, 2));
        this.pausetext.setAnchor(Text.Anchor.CENTER);
        this.pausetext.setCol(new Color(0, 0, 0, 0));

        this.glow = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 208, 136, 88, 96);
        this.glow.setPos(new Point(0,0));
        this.glow.setCol(new Color(0, 0, 0, 0));

        this.pauseVisible = false;
        
        this.addSubSprite(this.background);       
        this.addSubSprite(this.bar);
        this.addSubSprite(this.pausebg);
        this.addSubSprite(this.pausetext);
        this.addSubSprite(this.stoptext);
        this.addSubSprite(this.border);
        this.addSubSprite(this.glow);
        
        this._glow_color = new Color(0,0,0,0);
        this._glow_duration = 0.0;
        
        this.state = State.HIGH;
        this.reset(1000);
	 }
	
	public void pulseBorder(Color col1, Color col2, double duration) {
        this._glow_duration = duration;

        SpriteCallback fade_to_done;;
        SpriteCallback fade_from_done;;
        
        fade_to_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Hourglass hourglass = (Hourglass) getArg(0);
        		hourglass._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_from_done = (SpriteCallback) getArg(3);
        		hourglass.glow.fadeTo(hourglass._glow_color, currentTime, duration, fade_from_done);
        	}
        };

        fade_from_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Hourglass hourglass = (Hourglass) getArg(0);
        		hourglass._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_to_done = (SpriteCallback) getArg(3);
        		hourglass.glow.fadeTo(hourglass._glow_color, currentTime, duration, fade_to_done);
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

	public void setMax(int max) {
		this.max = max;
	}

	public int getMax() {
		return this.max;
	}

	public int getValue() {
		return this.value;
	}

	public void setValue(int value) {
	        this.value = value;

	        if(this.value > this.max) {
	            this.value = this.max;
	        }
	}

	public void addValue(int value) {
        this.setValue(this.value + value);
	}

    public void reset(int maxValue) {
        this.max = maxValue;
        this.value = maxValue;
        this.halted = 0;
        this.pause = 0;
        this.state = State.HIGH;
        this.hidePause();
    }

    public void scaleValue(double perc) {
        this.value *= perc;
        this.max *= perc;
    }

    public void updatePauseTimer(double time) {
        this.pause += time;

        if(this.pause > Config.MAX_PAUSE_TIME_SEC) {
            this.pause = Config.MAX_PAUSE_TIME_SEC;
        }
        
        if(this.pause > 0) {
            this.showPause();
        } else {
            this.hidePause();
        }
            
        if(this.pause > 0) {
            this.pausetext.setText(String.format("%.1f", this.pause));
        }
    }

    public void addPause(double time) {
        this.updatePauseTimer(time);
        this.pausetext.setScale(new Point(2.5, 2.5));
        this.pausetext.scaleTo(new Point(2.0, 2.0), this.currentTime, 0.3, null);
    }

    public void halt() {
        this.halted += 1;
    }
	        
    public void unhalt() {
        this.halted -= 1;
    }

    public void showPause() {
        if(this.pauseVisible) {
            return;
        }
        
        this.pauseVisible = true;
        this.pausebg.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.7f), this.currentTime, 0.2, null);
        this.pausetext.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.2, null);
        this.stoptext.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.2, null);
    }	        

    public void hidePause() {
        if(!this.pauseVisible) {
            return;
        }
        
        this.pauseVisible = false;
        this.pausebg.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.2, null);
        this.pausetext.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.2, null);
        this.stoptext.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.0f), this.currentTime, 0.2, null);
    }

    @Override
    public void update(double currentTime) {
	    super.update(currentTime);

	    if(this.state == State.GAMEOVER) {
            return;
        }

	    if(this.value <= 0) {
            EventHandler.getInstance().post(new Event(Event.Type.GAME_OVER));
            this.state = State.GAMEOVER;
            return;
	    }

        if(this.pause <= 0 && this.halted <= 0) {
	        this.value -= 1;
        } else if(this.halted <= 0) {
	        this.updatePauseTimer(-(this.currentTime - this.lastTime));
        }

        double p = (double) this.value / this.max;

        if(p > 1.0) {
            p = 1.0;
        }
	            
        p = (Math.exp(2.0*p) - 1.0)/(Math.exp(2.0) - 1.0);

        if(p < 0.2 && this.state != State.LOW) {
            this.state = State.LOW;
            EventHandler.getInstance().post(new EventTimeState(State.LOW));
        } else if(p >= 0.2 && p < 0.8 && this.state != State.NORMAL) {
            this.state = State.NORMAL;
            EventHandler.getInstance().post(new EventTimeState(State.NORMAL));
        } else if(p >= 0.8 && this.state != State.HIGH) {
            this.state = State.HIGH;
            EventHandler.getInstance().post(new EventTimeState(State.HIGH));
        }

        this.bar.scaleTo(new Point(72, p*80), currentTime, 0.1, null);
        this.bar.fadeTo(new Color((float)(1 - p), (float)p, 0.0f, 1.0f), currentTime, 0.1, null);
    }
}

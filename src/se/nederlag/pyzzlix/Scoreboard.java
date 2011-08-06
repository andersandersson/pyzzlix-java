package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scoreboard extends Sprite {
	private Font font;
	private Sprite border;
	private Sprite scorebg;
	private Sprite glow;

	private Color _glow_color;
	private double _glow_duration;

	private Text highscorelabeltext;
	private Text highscoretext;
	private Text scorelabeltext;
	private Text scoretext;

	private int level;
	private int score;
	private int highscore;
	
	public Scoreboard() {
		this.font = new Font("data/font_fat.png", 8, 8);

		this.highscorelabeltext = new Text(12, 44, this.font, "HISCORE:");        
        this.highscoretext = new Text(76, 34, this.font, "0");
        this.highscoretext.setAnchor(Text.Anchor.RIGHT);
        
        this.scorelabeltext = new Text(12, 24, this.font, "SCORE:");
        this.scoretext = new Text(76, 14, this.font, "0");
        this.scoretext.setAnchor(Text.Anchor.RIGHT);
		
        this.scorebg = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.scorebg.setPos(new Point(8, 8));
        this.scorebg.setScale(new Point(72, 48));
        this.scorebg.setCol(new Color(0, 0, 0, 0.3f));
		
		this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 208, 8, 88, 64);
		this.border.setPos(new Point(0.0, 0.0));

		this.glow = new Sprite(new Texture(Gdx.files.internal("data/windowglows.png")), 208, 8, 88, 64);
        this.glow.setPos(new Point(0,0));
        this.glow.setCol(new Color(0, 0, 0, 0));
		
		this.addSubSprite(this.scorebg);

		this.addSubSprite(this.scorelabeltext);
		this.addSubSprite(this.scoretext);
		this.addSubSprite(this.highscorelabeltext);
		this.addSubSprite(this.highscoretext);
		
		this.addSubSprite(this.border);
		this.addSubSprite(this.glow);
        
		this._glow_color = new Color(0,0,0,0);
        this._glow_duration = 0.0;	
        
        this.level = 0;
        this.score = 0;
        this.highscore = 0;
    }

	public void setHighscore(int highscore) {
        this.highscore = highscore;
        this.highscoretext.setText(""+this.highscore);
    }
                
    public void updateScoreboard(int score) {
        if (score != this.score) {
            this.score = score;
            this.scoretext.setText(""+this.score);
            if (score > this.highscore) {
                this.highscoretext.setText(""+this.score);
            }
        }
    }
    
    public void pulseBorder(Color col1, Color col2, double duration) { 
        this._glow_duration = duration;

        SpriteCallback fade_to_done;;
        SpriteCallback fade_from_done;;
        
        fade_to_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Scoreboard scoreboard = (Scoreboard) getArg(0);
        		scoreboard._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_from_done = (SpriteCallback) getArg(3);
        		scoreboard.glow.fadeTo(scoreboard._glow_color, currentTime, duration, fade_from_done);
        	}
        };

        fade_from_done = new SpriteCallback() {
        	public void callback(Sprite sprite, double currenttime) {
        		Scoreboard scoreboard = (Scoreboard) getArg(0);
        		scoreboard._glow_color = (Color) getArg(1);
        		double duration = (Double) getArg(2);
        		SpriteCallback fade_to_done = (SpriteCallback) getArg(3);
        		scoreboard.glow.fadeTo(scoreboard._glow_color, currentTime, duration, fade_to_done);
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

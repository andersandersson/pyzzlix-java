package se.nederlag.pyzzlix;

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

        this.blockcounttext = new Text(this.blockposx + 8, this.blockposy - 4, this.font, ":99/99");
   
        this.scorebg = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.scorebg.setPos(new Point(8, 8));
        this.scorebg.setScale(new Point(72, 32));
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
}

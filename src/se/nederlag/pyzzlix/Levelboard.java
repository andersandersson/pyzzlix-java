package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Levelboard extends Sprite {
	private Font font;
	private Sprite border;
	
	public Levelboard() {
		this.font = new Font("data/font_fat.png", 8, 8);
		this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 208, 80, 88, 48);
		this.border.setPos(new Point(0.0, 0.0));
		this.addSubSprite(this.border);
	}
}

package se.nederlag.pyzzlix;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Font {
	private int width;
	private int height;
	private Texture sheet;
	private List<TextureRegion> glyphs;
	
	public Sprite sprite;
	
	public Font(String fontfile, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.sheet = new Texture(Gdx.files.internal(fontfile));
		this.glyphs = Resources.loadImageSheet(this.sheet, width, height);
		this.sprite = new Sprite(this.sheet);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public Sprite getGlyph(char c) {
		int glyph = (int) c - 32;
		
		Sprite sprite = new Sprite(this.sheet);
		sprite.setRegion(this.glyphs.get(glyph));
		sprite.setSize(this.width, this.height);
		
		return sprite;
	}
}

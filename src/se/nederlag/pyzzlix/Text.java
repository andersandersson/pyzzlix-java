package se.nederlag.pyzzlix;

public class Text extends Sprite {
	public enum Anchor {
		LEFT, CENTER, RIGHT;
	}
	
	private Font font;
	private String text;
	private Anchor anchor;
	
	public Text(int x, int y, Font font, String text) {
		this.font = font;
		this.text = "";
		this.setPos(new Point(x, y));
		this.setScale(new Point(1.0, 1.0));
		this.anchor = Anchor.LEFT;
		this.setText(text);
	}
	
	public void setAnchor(Anchor anchor) {
		this.anchor = anchor;
		String text = this.text;
		this.text = "";
		this.setText(text);
	}
	
	public void setText(String newtext) {
        if(this.text == newtext) {
            return;
        }

        this.text = newtext;

        String[] splittext = newtext.split("\n");
        this.clearSubSprites();

        
        int drawposx = 0;
        int drawposy = 0;
        
        for(String text : splittext) {
             char[] chars = text.toCharArray();             
             int length = chars.length;
             int width = this.font.getWidth() * length;             
             int height = this.font.getHeight();

             if (this.anchor == Anchor.LEFT) {
                 drawposx = 0;
             } else if (this.anchor == Anchor.RIGHT) {
             	drawposx = -length * this.font.getWidth();
             } else if (this.anchor == Anchor.CENTER) {
                 drawposx = -(length * this.font.getWidth()) / 2;
             }
                 
             for(char c : chars) {
                 Sprite glyph = this.font.getGlyph(c);
                 glyph.setPos(new Point(drawposx, drawposy));
                 this.addSubSprite(glyph);
                 drawposx += this.font.getWidth();
             }
             
             drawposy += this.font.getHeight();	
        }
	}
	
	public String toString() {
		return this.text;
	}
}

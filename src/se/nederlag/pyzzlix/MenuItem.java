package se.nederlag.pyzzlix;

import com.badlogic.gdx.graphics.Color;

public class MenuItem extends Text {
	enum State { 
		NORMAL, BLINKING;
	};
	
	private Callback callfunc;
	
	public int timer;
	public int blinkcount;
	public State state;
	public boolean inFocus;
	public Point focusScale;
	public Color focusColor;
	public Point unfocusScale;
	public Color unfocusColor;

	
	public MenuItem(int x, int y, Font font, String label, Callback callfunc, Text.Anchor anchor) {
		super(x, y, font, label);
		
		this.callfunc = callfunc;
		this.setAnchor(anchor);

		this.timer = 0;
	    this.blinkcount = 0;
	    this.state = State.NORMAL;

	    this.inFocus = false;
	    this.focusScale = new Point(1.5, 1.5);
	    this.focusColor = new Color(1, 1, 1, 1);

	    this.unfocusScale = new Point(1.0, 1.0);
	    this.unfocusColor = new Color(0.6f, 0.6f, 0.6f, 1.0f);

	    this.setCol(this.unfocusColor);
	    this.setScale(this.unfocusScale);		
	}
	
	public void update(double currenttime) {
		super.update(currenttime);
		
		/* Skum kod
	    if (self.state == "normal"):
	        pass
	    elif (self.state == "blinking"):
	        self.timer
	    */
	}

	public void reset() {
		this.inFocus = false;
		this.setCol(this.unfocusColor);
		this.setScale(this.unfocusScale);
	}
	
	public void focus(double currenttime) {
		this.inFocus = true;
		this.fadeTo(this.focusColor, currenttime, 0.1, null);
		this.scaleTo(this.focusScale, currenttime, 0.05, null);
	}

	public void unfocus(double currenttime) {
		this.inFocus = true;
		this.fadeTo(this.unfocusColor, currenttime, 0.3, null);
		this.scaleTo(this.unfocusScale, currenttime, 0.1, null);
	}
	
	public void select() {
		this.timer = 0;
		this.state = State.BLINKING;
		
		if(this.callfunc != null) {
			this.callfunc.call();
		}
	}
}

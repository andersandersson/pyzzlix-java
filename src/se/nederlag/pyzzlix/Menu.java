package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

public class Menu extends Sprite {
	private List<MenuItem> items;
	private int focus;
	private int count;
	
	public Menu() {
		super();
		
		this.items = new LinkedList<MenuItem>();
		this.focus = 0;
		this.count = 0;
	}
	
	public void reset() {
        for(MenuItem item : this.items) {
            item.reset();
        }            
	}
	
	public MenuItem getItem(int idx) {
		return this.items.get(idx);
	}
	
	public int getFocus() {
		return this.focus;
	}
	
	public void add(MenuItem item) {
		this.addSubSprite(item);
		this.items.add(item);
		this.count = this.items.size();
	}

    public void setFocusCol(Color col) {
        for(MenuItem item : this.items) { 
            item.focusColor = col;
            if(item.inFocus) {
                item.focus(this.currentTime);
            }
        }
    }
        
    public void setUnfocusCol(Color col) {
    	for(MenuItem item : this.items) {
            item.unfocusColor = col;
            if(!item.inFocus) {
                item.unfocus(this.currentTime);
            }
    	}
    }
        
    public void setFocusScale(Point scale) {
    	for(MenuItem item : this.items) {
            item.focusScale = scale;
            if(item.inFocus) {
                item.focus(this.currentTime);
            }
    	}
    }
        
    public void setUnfocusScale(Point scale) {
    	for(MenuItem item : this.items) {
            item.unfocusScale = scale;
            if(!item.inFocus) {
                item.unfocus(this.currentTime);
            }
    	}
    }
        
    public void focusItem(int newfocus) {
        if(newfocus < 0) {
            newfocus = this.count - 1;
        }
            
        if (newfocus >= this.count) {
            newfocus = 0;
        }
    
        this.items.get(this.focus).unfocus(this.currentTime);
        this.items.get(newfocus).focus(this.currentTime);
        this.focus = newfocus;
    }
 	
    public void nextItem() {
        this.focusItem(this.focus + 1);
    }

    public void prevItem() {
        this.focusItem(this.focus - 1);
    }

    public void unfocusItem() {
        this.items.get(this.focus).unfocus(this.currentTime);
    }
        
    public void selectItem() {
        this.items.get(this.focus).select();
    }
}

package se.nederlag.pyzzlix.events;

public class EventMouseMove extends Event {
	
	public final int x;
	public final int y;
	
	public EventMouseMove(int x, int y)  {
		super(Event.Type.MOUSE);
		this.x =x;
		this.y = y;
	}
	
	public String toString() {
		return "Mousepos: (" + this.x + ", " + this.y + ")";
	}
}

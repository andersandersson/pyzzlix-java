package se.nederlag.pyzzlix.events;

public class Event {
	public enum Type {
		KEYBOARD, GAME_OVER;
	};
	
	public final Type type;
	
	public Event(Type type) {
		this.type = type;
	}
}
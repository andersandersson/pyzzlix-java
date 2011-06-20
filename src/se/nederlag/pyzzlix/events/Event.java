package se.nederlag.pyzzlix.events;

public class Event {
	public enum Type {
		KEYBOARD, GAME_OVER, CIRCLE_FOUND;
	};
	
	public final Type type;
	
	public Event(Type type) {
		this.type = type;
	}
}
package se.nederlag.pyzzlix.events;

public class Event {
	public enum Type {
		KEYBOARD, MOUSE, GAME_OVER, CIRCLE_FOUND, TIME_STATE_CHANGED, LEVEL_UP, EXIT;
	};
	
	public final Type type;
	
	public Event(Type type) {
		this.type = type;
	}
}
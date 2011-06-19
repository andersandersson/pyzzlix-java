package se.nederlag.pyzzlix.events;

public class EventKeyState extends Event {
	public enum State {
		UP, DOWN;
	}

	public final State state;
	public final int key;
	
	public EventKeyState(State state, int key) {
		this.state = state;
		this.key = key;
	}
	
	public String toString() {
		return "Key: " + this.key + ", state: " + this.state;
	}
}

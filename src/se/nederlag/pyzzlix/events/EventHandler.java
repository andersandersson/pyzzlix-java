package se.nederlag.pyzzlix.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class EventHandler {
	private static EventHandler instance = null;

	private Queue<Event> queue;
	
	private EventHandler() {
		this.queue = new LinkedList<Event>();
	}
	
	public void post(Event event) {
		this.queue.add(event);
	}
	
	public Event poll() {
		return this.queue.poll();
	}

	public Event peek() {
		return this.queue.peek();
	}
	
	public static EventHandler getInstance() {
		if(EventHandler.instance == null) {
			EventHandler.instance = new EventHandler();
		}
		
		return EventHandler.instance;
	}
}

package se.nederlag.pyzzlix.events;

import se.nederlag.pyzzlix.Hourglass;

public class EventTimeState extends Event {
	public final Hourglass.State state;
	
	public EventTimeState(Hourglass.State state) {
		super(Event.Type.TIME_STATE_CHANGED);
		
		this.state = state;
	}
}

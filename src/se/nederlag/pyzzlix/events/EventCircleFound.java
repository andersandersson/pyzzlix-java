package se.nederlag.pyzzlix.events;

import java.util.List;

import se.nederlag.pyzzlix.Block;

public class EventCircleFound extends Event {
	public final List<Block> fallBlocks;
	public final List<Block> rotationBlocks;
	
	public EventCircleFound(List<Block> fallBlocks, List<Block> rotationBlocks) {
		super(Event.Type.CIRCLE_FOUND);
		
		this.fallBlocks = fallBlocks;
		this.rotationBlocks = rotationBlocks;
	}
}

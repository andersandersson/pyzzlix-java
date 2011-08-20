package jsmug.audio;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CueSheet {
	public enum LoopMode {
		PINGPONG, NORMAL, NONE;
	}
	
	private class CueSheetEntry {
		double startTime;
		double endTime;
		Cue cue;
		
		public CueSheetEntry(double startTime, double endTime, Cue cue) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.cue = cue;
		}
	}
	
	private double currentTime = 0.0;
	private double timeDirection = 1.0;
	private double cueSheetEndTime = 0.0;
	
	private LoopMode loopMode = LoopMode.NONE;
	
	private Map<Object, Map<Method,List<CueSheetEntry>>> cues;
	
	public CueSheet() {
		this.cues = new HashMap<Object, Map<Method,List<CueSheetEntry>>>();
	}
	
	public boolean seek(double time) {
		this.currentTime = time;
		boolean active = false;
		double percentage = 0.0;

		if(this.loopMode != LoopMode.NONE) {
			active = true;
		}
		
		if(this.loopMode == LoopMode.NORMAL && this.currentTime > this.cueSheetEndTime) {
			this.currentTime = 0.0;
		}

		if(this.loopMode == LoopMode.PINGPONG && this.currentTime > this.cueSheetEndTime) {
			this.currentTime = this.cueSheetEndTime;
			this.timeDirection = -1;
		}
		
		if(this.loopMode == LoopMode.PINGPONG && this.currentTime < 0) {
			this.currentTime = 0;
			this.timeDirection = 1;
		}	
 		
		for(Map.Entry<Object, Map<Method,List<CueSheetEntry>>> targetCues : this.cues.entrySet()) {
			for(Map.Entry<Method,List<CueSheetEntry>> methodCues : targetCues.getValue().entrySet()) {
				for(CueSheetEntry cueSheetEntry : methodCues.getValue()) {
					if(cueSheetEntry.startTime <= this.currentTime && this.currentTime <= cueSheetEntry.endTime) {
						if(cueSheetEntry.endTime != cueSheetEntry.startTime) {
							percentage = (this.currentTime - cueSheetEntry.startTime) / (cueSheetEntry.endTime - cueSheetEntry.startTime);
						} else {
							percentage = 1.0;
						}
						
						active = true;
						cueSheetEntry.cue.eval(percentage);
					}
				}
			}
		}
		
		return !active;
	}
	
	public boolean update(double deltatime) {
		return this.seek(this.currentTime + deltatime*this.timeDirection);
	}
	
 	public<T> void addCue(double startTime, double endTime, Cue cue) {
 		// Make sure we have no bogus interval
 		if(startTime > endTime) {
 			throw new IllegalArgumentException("Starttime must be lower than endtime");
 		}
 		
 		// Make sure that the new cue does not overlap previous cues
		Map<Method,List<CueSheetEntry>> targetCues = this.cues.get(cue.getTarget());
 		List<CueSheetEntry> methodCues = null;

 		if(targetCues != null) {			
			methodCues = targetCues.get(cue.getMethod());
			
			if(methodCues != null) {
				for(CueSheetEntry cueSheetEntry : methodCues) {
					if( !(endTime <= cueSheetEntry.startTime || startTime >= cueSheetEntry.endTime))
					{
						throw new IllegalArgumentException("Cues of same type must not overlap");
					}
				}
			}
		}
 		
 		// Save endtime of entire cuesheet
 		if(endTime > this.cueSheetEndTime) {
 			this.cueSheetEndTime = endTime;
 		}
 		

 		// Add cue to map of cues (and create them if needed)
 		if(targetCues == null) {
 			targetCues = new HashMap<Method,List<CueSheetEntry>>();
 			this.cues.put(cue.getTarget(), targetCues);
 		}
 		
 		methodCues = targetCues.get(cue.getMethod());

 		if(methodCues == null) {
 			methodCues = new LinkedList<CueSheetEntry>();
 			targetCues.put(cue.getMethod(), methodCues);
 		}
 		
 		methodCues.add(new CueSheetEntry(startTime, endTime, cue));
 	}
 	
 	public void setLoopMode(LoopMode mode) {
 		this.loopMode = mode;
 	}

 	public LoopMode getLoopMode() {
 		return this.loopMode;
 	}
}

package jsmug.audio;

import java.util.LinkedList;
import java.util.List;

public class CueManager {
	private List<CueSheet> cueSheets;
	private List<CueSheet> cueSheetsToRemove;
	
	public CueManager() {
		this.cueSheets = new LinkedList<CueSheet>();
		this.cueSheetsToRemove = new LinkedList<CueSheet>();
	}
	
	public void addCueSheet(CueSheet cueSheet) {
		this.cueSheets.add(cueSheet);
	}
	
	public void update(double deltatime) {
		for(CueSheet cueSheet : this.cueSheets) {
			if(cueSheet.update(deltatime)) {
				this.cueSheetsToRemove.add(cueSheet);
			}
		}
		
		for(CueSheet cueSheet : this.cueSheetsToRemove) {
			this.cueSheets.remove(cueSheet);
		}
		
		this.cueSheetsToRemove.clear();
	}
}

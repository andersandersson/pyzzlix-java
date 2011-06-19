package se.nederlag.pyzzlix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

public class Scene_MainGame extends Scene {
	public enum State {
		RUNNING, GAMEOVER, IDLE;
	};
	
	private static Scene_MainGame instance = null;

	private State state;
	private boolean partsInPlace;
	private Board board;

	private SortedMap<Integer,Set<Integer>> levelBlocks;
	private List<Integer> activeBlocks;
	private int level;
	private int blocksToLevel;
	private int activeBlock;
	private int blockCount;
	private int score;
	private int initX;
	private int initY;
	private int initXDir;
	private int initYDir;
	private int initCounter;
	private int comboCounter;
	private int comboResetCounter;
	private Random randomGenerator;

	private Sprite blocks;
	
	private Scene_MainGame() {
		this.board = new Board(this, 10, 13);
		this.board.setPos(new Point(24, -300));
		this.state = State.IDLE;
		this.partsInPlace = false;
		this.blocks = new Sprite();
		
		this.levelBlocks = new TreeMap<Integer,Set<Integer>>();
		this.levelBlocks.put(1, new TreeSet<Integer>());
		this.levelBlocks.put(4, new TreeSet<Integer>());
		this.levelBlocks.put(9, new TreeSet<Integer>());
		this.levelBlocks.put(15, new TreeSet<Integer>());
		this.levelBlocks.put(22, new TreeSet<Integer>());

		int imax = 3;
		for(Map.Entry<Integer, Set<Integer>> entry : this.levelBlocks.entrySet()) {
			for(int i=0; i<imax; i++) {
				entry.getValue().add(i);
			}
			imax++;
		}
		
		this.activeBlocks = new LinkedList<Integer>();
		for(int i=0; i<4; i++) {
			for(int j=0; j<i+4; j++) {
				this.activeBlocks.add(j);
			}
		}
		
		this.randomGenerator = new Random(System.nanoTime());
		
		sprites.add(this.blocks);
		sprites.add(board);
	}
	
	public static Scene_MainGame getInstance()
	{
		if (instance == null)
			instance = new Scene_MainGame();
		return instance;
	}
	
	@Override 
	public void show() {
		this.resetGame();
		this.startGame();
	}
	
	public void startGame() {
		this.state = State.RUNNING;
		
		if(!this.partsInPlace) {
			this.moveInParts();
		}
	}
	
	public void pauseGame() {
		this.state = State.IDLE;
	}
	
	public void resetGame() {
		this.setLevel(1);
		this.blocks.clearSubSprites();
		this.board.stopPulseBorder();

		this.blockCount = 0;
		this.score = 0;
		this.board.reset();
		this.initX = 0;
		this.initY = this.board.getBoardHeight()*2-1;
		this.initXDir = 1;
		this.initYDir = -1;
		this.initCounter = this.board.getBoardWidth()*this.board.getBoardHeight();
		this.comboCounter = 0;
		this.comboResetCounter = 0;
		this.state = State.IDLE;
	}
	
	public void moveInParts() {
		this.partsInPlace = true;
		this.board.moveTo(new Point(24,0), this.currentTime, 0.5, null);
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	public void fillZigZag() {
		for(int i=0; i<4; i++) {
			if(this.initCounter > 0) {
				this.initCounter -= 1;
				this.addRandom(this.initX, this.initY);
				this.initX += this.initXDir;
				
				if(this.initX >= this.board.getBoardWidth()) {
					this.initXDir = -1;
					this.initX -= 1;
					this.initY -= 1;
				} 
				
				if(this.initX < 0) {
					this.initXDir = 1;
					this.initX += 1;
					this.initY -= 1;
				}
			}
		}
	}
	
	public void addRandom(int x, int y) {
		int type;
		
		if(y < this.board.getBoardHeight()*2 - 1) {
			type = this.getRandomBlockType();
			
			while(this.board.getBlockAt(x, y+1) != null && this.board.getBlockAt(x, y+1).getType() == type) {
				type = this.getRandomBlockType();
			}
		} else {
			type = this.getRandomBlockType();
		}
		
		this.createBlock(x, y, type);
	}
	
	public void createBlock(int x, int y, int type) {
		Block block = new Block(x, y, type, 8, -this.board.getBoardHeight()*16+8);
		this.board.add(x, y, block);
		block.animatePopup(this.currentTime);
	}
	
	public List<Integer> getUsableBlocks() {
		List<Integer> usableBlocks = new LinkedList<Integer>();
		int maxLevel = 0;
		
		for(Map.Entry<Integer, Set<Integer>> entry : this.levelBlocks.entrySet()) {
			if(this.level >= entry.getKey() && entry.getKey() >= maxLevel) {
				maxLevel = entry.getKey();
				usableBlocks.addAll(entry.getValue());
			}
		}
		
		
		return usableBlocks;
	}
	
	public int getRandomBlockType() {
		List<Integer> usableBlocks = this.getUsableBlocks();
		int type = usableBlocks.get(this.randomGenerator.nextInt(usableBlocks.size()));
		return type;
	}
	
	@Override
	public void tick() {
		if(this.state == State.RUNNING) {
			if(!this.board.full()) {
				if(this.initCounter > 0) {
					this.fillZigZag();
				} else {
					int boardWidth = this.board.getBoardWidth();
					int boardHeight = this.board.getBoardHeight();
					
					for(int x=boardWidth-1; x>=0; x--) {
						for(int y=boardHeight-1; y>=0; y--) {
							if(this.board.getBlockAt(x, y) == null) {
								this.addRandom(x, y);
							}
						}						
					}
				}
			}
			
			this.board.updateBoard();
		}
	}

	@Override
	public boolean handleEvent(Event event) {
		if(event.getClass() == EventKeyState.class) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				
				if(this.state == State.RUNNING) {
					switch(keyevent.key) {
						case Input.Keys.UP:
							Gdx.app.log("MAIN", "Pressed UP");
							break;
						case Input.Keys.DOWN:
							Gdx.app.log("MAIN", "Pressed DOWN");
							break;
						case Input.Keys.LEFT:
							Gdx.app.log("MAIN", "Pressed LEFT");
							break;
						case Input.Keys.RIGHT:
							Gdx.app.log("MAIN", "Pressed RIGHT");
							break;
						default:
							break;
					}
				}
			}
			
			return true;
		}
		
		return false;
	}
}

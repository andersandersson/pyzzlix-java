package se.nederlag.pyzzlix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import se.nederlag.pyzzlix.events.EventCircleFound;
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
	
	public void removeBlocks(List<Block> blocks) {
		List<Block> scale_blocks = new LinkedList<Block>(blocks);
		
		double delay = 0.7 / blocks.size();
		
		if(delay > 0.08) {
			delay = 0.08;
		}
		
		SpriteCallback block_scale_done = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				List<Block> scale_blocks = (List<Block>) getArg(0);
				List<Block> blocks = (List<Block>) getArg(1);
				double delay = (Double) getArg(2);
				
				if(scale_blocks.size() > 0) {
					Block next_block = scale_blocks.get(0);
					scale_blocks.remove(next_block);
					next_block.fadeTo(new Color(0.0f,0.0f,0.0f,0.0f), currenttime, delay, this);
					next_block.rotateTo(720.0, currenttime, delay, null);
					next_block.scaleTo(new Point(4.0,4.0), currenttime, delay, null);
				} else {
					for(Block block : blocks) {
						Scene_MainGame.getInstance().board.clear(block.getBoardX(), block.getBoardY());
					}
				}
			}
		};
		SpriteCallback block_wait_done = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				sprite.scaleTo(new Point(1.0, 1.0), currenttime, 0.5, (SpriteCallback) getArg(0));
			}
		};
		SpriteCallback block_wait_before_blink = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				List<Block> blocks = (List<Block>) getArg(0);
				
				int imax = blocks.size();
				for(int i=0; i<imax-1; i++) {
					Block block = blocks.get(i);
					block.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), currenttime, 0.1, null);
					block.doBlink();
				}

				Block block = blocks.get(imax-1);
				block.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), currenttime, 0.1, (SpriteCallback) getArg(1));
				block.doBlink();
			}
		};
		
		block_scale_done.setArgs(scale_blocks, blocks, delay);
		block_wait_done.setArgs(block_scale_done);
		block_wait_before_blink.setArgs(blocks, block_wait_done);
		
		blocks.get(blocks.size()-1).fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f), this.currentTime, 0.1, block_wait_before_blink);
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

	public void addCircleScore(List<Block> blocks, boolean falling) {
		
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
		if(event.type == Event.Type.CIRCLE_FOUND) {
			EventCircleFound circleevent = (EventCircleFound) event;
			
			if(circleevent.fallBlocks.size() > 0) {
				this.addCircleScore(circleevent.fallBlocks, true);
				this.removeBlocks(circleevent.fallBlocks);
			}

			if(circleevent.rotationBlocks.size() > 0) {
				this.addCircleScore(circleevent.fallBlocks, false);
				this.removeBlocks(circleevent.rotationBlocks);
			}
		}
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				
				if(this.state == State.RUNNING) {
					switch(keyevent.key) {
						case Input.Keys.UP:
							if(this.board.getMarker().getBoardY() > this.board.getBoardHeight()) {
								this.board.getMarker().move(0, -1, this.currentTime);
							}
							break;
						case Input.Keys.DOWN:
							if(this.board.getMarker().getBoardY() < this.board.getBoardHeight()*2-2) {
								this.board.getMarker().move(0, 1, this.currentTime);
							}
							break;
						case Input.Keys.LEFT:
							if(this.board.getMarker().getBoardX() > 0) {
								this.board.getMarker().move(-1, 0, this.currentTime);
							}
							break;
						case Input.Keys.RIGHT:
							if(this.board.getMarker().getBoardX() < this.board.getBoardWidth()-2) {
								this.board.getMarker().move(1, 0, this.currentTime);
							}
							break;
						case Input.Keys.Z:
							if(this.board.rotate(this.board.getMarker().getBoardX(), this.board.getMarker().getBoardY(), -1, 2)) {
								this.board.getMarker().turn();
							} else {
								this.board.getMarker().fail();
							}
							break;
						case Input.Keys.X:
							if(this.board.rotate(this.board.getMarker().getBoardX(), this.board.getMarker().getBoardY(), 1, 2)) {
								this.board.getMarker().turn();
							} else {
								this.board.getMarker().fail();
							}
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

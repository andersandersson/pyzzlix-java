package se.nederlag.pyzzlix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import se.nederlag.pyzzlix.audio.MusicInputStream;
import se.nederlag.pyzzlix.audio.MusicInputStreamMixer;
import se.nederlag.pyzzlix.audio.OggMusicInputStream;
import se.nederlag.pyzzlix.audio.OpenALMusicStream;
import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventCircleFound;
import se.nederlag.pyzzlix.events.EventKeyState;
import se.nederlag.pyzzlix.events.EventMouseMove;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.openal.OpenALAudio;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_MainGame extends Scene {
	public enum State {
		RUNNING, GAMEOVER, IDLE;
	};
	
	private static Scene_MainGame instance = null;

	private State state;
	private boolean partsInPlace;
	private Board board;
	private Hourglass hourglass; 
	private Levelboard levelboard;
	private Scoreboard scoreboard; 

	private SortedMap<Integer,Set<Integer>> levelBlocks;
	private List<Integer> activeBlocks;
	private int level;
	private int blocksToLevel;
	private int activeBlock;
	private int blockCount;
	private boolean doLevelUp;
	private int doLevelUpCounter;
	private int score;
	private int initX;
	private int initY;
	private int initXDir;
	private int initYDir;
	private int initCounter;
	private int comboCounter;
	private int comboResetCounter;
	private Random randomGenerator;

	private OpenALMusicStream music;
	private List<MusicInputStream> musics;
	private SortedMap<Integer,Set<Integer>> levelMusic;
	private Set<Integer> allMusic;
	
	
	private Sprite blocks;
	private Font font;
	
	private Sprite layerEffects;

	private Scene_MainGame() {
		this.font = new Font("data/font_fat.png", 8, 8);

		this.board = new Board(this, Config.BOARD_WIDTH, Config.BOARD_HEIGHT);
		this.board.setPos(new Point(24, -300));
		
		this.scoreboard = new Scoreboard();
		this.scoreboard.setPos(new Point(408.0, 80.0));

		this.levelboard = new Levelboard();
		this.levelboard.setPos(new Point(408.0, 80.0));

		this.hourglass = new Hourglass();
		this.hourglass.setPos(new Point(408.0, 136.0));
		
		this.state = State.IDLE;
		this.partsInPlace = false;
		this.blocks = new Sprite();

		this.doLevelUp = false;
		this.doLevelUpCounter = 0;
		this.comboCounter = 0;
		this.comboResetCounter = 0;
		this.blockCount = 0;
		this.score = 0;
		this.initCounter = 0;
		this.initX = 0;
		this.initY = 0;
		this.initXDir = 0;
		this.initYDir = 0;
		this.level = 1;

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
		
		this.layerEffects = new Sprite();
		
		this.addSprite(this.scoreboard);
		this.addSprite(this.levelboard);
		this.addSprite(this.hourglass);
		this.addSprite(this.board);
		this.addSprite(this.blocks);

		this.addSprite(this.layerEffects);

		
		this.levelMusic = new TreeMap<Integer,Set<Integer>>();
		this.levelMusic.put(1, new TreeSet<Integer>());
		this.levelMusic.get(1).addAll(Arrays.asList(new Integer[]{ 1,2 }));

		this.levelMusic.put(3, new TreeSet<Integer>());
		this.levelMusic.get(3).addAll(Arrays.asList(new Integer[]{ 0,1,2 }));

		this.levelMusic.put(6, new TreeSet<Integer>());
		this.levelMusic.get(6).addAll(Arrays.asList(new Integer[]{ 0,1,2,3 }));

		this.levelMusic.put(9, new TreeSet<Integer>());
		this.levelMusic.get(9).addAll(Arrays.asList(new Integer[]{ 1,2,3,5 }));

		this.levelMusic.put(12, new TreeSet<Integer>());
		this.levelMusic.get(12).addAll(Arrays.asList(new Integer[]{ 1,2,3,4,5 }));

		this.levelMusic.put(15, new TreeSet<Integer>());
		this.levelMusic.get(15).addAll(Arrays.asList(new Integer[]{ 0,1,2,3,4,5 }));

		this.levelMusic.put(17, new TreeSet<Integer>());
		this.levelMusic.get(17).addAll(Arrays.asList(new Integer[]{ 0,1,2,3,4,5,6 }));

		this.levelMusic.put(19, new TreeSet<Integer>());
		this.levelMusic.get(19).addAll(Arrays.asList(new Integer[]{ 0,1,2,3,4,5,7 }));

		this.musics = new ArrayList<MusicInputStream>();
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_chord.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_hh.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_bass.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_bass2.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_kick.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_lead.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_lead3.ogg")));
		this.musics.add(new OggMusicInputStream(Gdx.files.internal("data/music1_lead2.ogg")));

		this.allMusic = new TreeSet<Integer>();
		this.allMusic.addAll(Arrays.asList(new Integer[]{ 0,1,2,3,4,5,6,7 }));
		
		MusicInputStreamMixer mixer = new MusicInputStreamMixer(8, this.musics.get(0).getChannels(), this.musics.get(0).getSampleRate());

		for(int i=0; i<8; i++) {
			mixer.setStream(i, this.musics.get(i));
			this.musics.get(i).setVolume(0.0f);
		}
		
		this.music = new OpenALMusicStream((OpenALAudio) Gdx.audio, mixer);
		music.setLooping(true);
		playMusicForLevel();
		music.play();
	}
	
	public static Scene_MainGame getInstance()
	{
		if (instance == null)
			instance = new Scene_MainGame();
		return instance;
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
	
	@Override 
	public void show() {
		this.resetGame();
		this.startGame();
	}
	

	public void moveInParts() {
		this.partsInPlace = true;
		this.board.moveTo(new Point(24,0), this.currentTime, 0.5, null);
		this.scoreboard.moveTo(new Point(208, 8), this.currentTime, 0.5, null);
		this.levelboard.moveTo(new Point(208, 80), this.currentTime, 0.5, null);
		this.hourglass.moveTo(new Point(208, 136), this.currentTime, 0.5, null);
	}
	
	public void moveOutParts() {
	}
	
	public void showSplash() {		
	}
	
	public void hide() {		
	}
	
	public void resetGame() {
		this.setLevel(1);
		this.blocks.clearSubSprites();
		
		this.board.stopPulseBorder();
		this.levelboard.stopPulseBorder();
		this.scoreboard.stopPulseBorder();
		this.hourglass.stopPulseBorder();
		
		this.hourglass.reset(Config.HOURGLASS_DEFAULT);
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
		
		this.scoreboard.setHighscore(0);
		this.playMusicForLevel();
	}
	
	public void showGameOver() {
	}
	
	public void showEnterHighscore() {
	}

	public void playMusicForLevel() {
		Set<Integer> close = new TreeSet<Integer>(this.allMusic);
		Set<Integer> to_play = new TreeSet<Integer>();
		int max_level = 0;
		
		for(Map.Entry<Integer,Set<Integer>> entry : this.levelMusic.entrySet()) {
			if(this.level >= entry.getKey() && entry.getKey() >= max_level) {
				max_level = entry.getKey();
				to_play = entry.getValue();
			}
		}
		
		for(int i : to_play) {
			if(close.contains(i)) {
				close.remove(i);
				this.musics.get(i).setVolume(1.0f);
			}
		}

		for(int i : close) {
			this.musics.get(i).setVolume(0.0f);
		}
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
	
	public void refillUpperHalfBoard() {
		for(int x = 0; x < this.board.getBoardWidth(); x++) {
			for(int y = 0; y < this.board.getBoardHeight(); y++) {
				this.board.clear(x, y);
				this.addRandom(x, y);
			}
		}
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
			
			this.scoreboard.updateScoreboard(this.score);
			this.board.updateBoard();
			
			if(this.comboCounter > 0) {
				if(this.board.inactive()) {
					this.comboResetCounter += 1;
				} else {
					this.comboResetCounter = 0;
				}
				
				if(this.comboResetCounter > 3) {
					this.comboCounter = 0;
				}
			}

			if(this.doLevelUp) {
				if(this.board.inactive()) {
					this.doLevelUpCounter += 1;
				} else {
					this.doLevelUpCounter = 0;
				}
				
				if(this.doLevelUpCounter > 3) {
					this.newLevel();
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
	
	public void addBlockScore(Block block) {
        this.score += (this.level * Config.POINTS_PER_LEVEL_FOR_BLOCK_SCORE);

        if(block.getType() == this.activeBlock) {
            Block tmp_block = new Block(0, 0, block.getType(), 0, 0);
            int x = (int) (block.calcPos(0).getX() + this.board.calcPos(0).getX() + this.board.getBlocks().calcPos(0).getX());
            int y = (int) (block.calcPos(0).getY() + this.board.calcPos(0).getY() + this.board.getBlocks().calcPos(0).getY());

            tmp_block.setPos(new Point(x, y));
            
            this.layerEffects.addSubSprite(tmp_block);

            SpriteCallback remove_tmp_block = new SpriteCallback() {
				@Override
				public void callback(Sprite sprite, double currenttime) {
	            	Scene_MainGame maingame = (Scene_MainGame) getArg(0);
	            	maingame.blockCount += 1;
	            	maingame.layerEffects.removeSubSprite(sprite);
	            	maingame.levelboard.updateLevelboard(maingame.blockCount);
				}
            };

            remove_tmp_block.setArgs(this);
            
            tmp_block.moveTo(new Point(228, 110), this.currentTime, 0.5, remove_tmp_block);
            tmp_block.rotateTo(720.0, this.currentTime, 0.5, null);
            tmp_block.setCol(new Color(1.0f, 1.0f, 1.0f, 0.8f));
            tmp_block.fadeTo(new Color(1.0f, 1.0f, 1.0f, 0.5f), this.currentTime, 0.5, null);
        }
 	}
	
	public void addCircleScore(List<Block> blocks, boolean falling) {		
		int num_blocks = blocks.size();
	    int score = 0;
	    int cs = 0;

	    if(falling) {
	    	cs = this.comboCounter;
	        
	    	if (cs > 10) {
	    		cs = 10;
	    	}
	        
	    	//Mixer().playSound(self.combosound, volume=0.7)
	        this.comboCounter += 1;
	    }
	     
	    //Mixer().playSound(self.circlesound, volume=0.5)

	    double factor = this.comboCounter+1;
	        
	    int text_x = 0;
	    int text_y = 0;
	    int text_count = blocks.size();

	    if(num_blocks >= Config.MIN_BLOCKS_FOR_CIRCLE_SCORE || falling) {
	        score = num_blocks*Config.POINTS_PER_LEVEL_FOR_CIRCLE_SCORE*this.level;

	        if(factor > 0) {
	                score *= factor;
	        }
	            
	        this.score += score;
	    }
	    
	    double perc = (double)num_blocks*Config.PERCENTAGE_TIME_GIVEN_PER_BLOCK;
	        
	    this.hourglass.addValue((int)Math.floor(perc*this.hourglass.getMax()));
	    //self.background.boost(floor(num_blocks/4))
	       
	    if(score < 1) {
	    	return;
	    }

        for(Block block : blocks) {
            text_x += block.calcPos(0).getX();
            text_y += block.calcPos(0).getY();
        }

        text_x = (int) (text_x/text_count + this.board.calcPos(0).getX());// - this.font.width/2*len(str(len(blocks)))
        text_y = (int) (text_y/text_count + this.board.calcPos(0).getY());// - this.font.height/2
        
        Text text;
        if(factor >= 2.0) {
            text = new Text(text_x + 8, text_y + 8, this.font, String.valueOf((int)score/factor) + String.format("X%d", (int)factor));
        } else {
            text = new Text(text_x + 8, text_y + 8, this.font, String.valueOf((int)score));
        }

        text.setAnchor(Text.Anchor.CENTER);
        
        SpriteCallback text_fade_done = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				Sprite layer = (Sprite) getArg(0);
				layer.removeSubSprite(sprite);
			}
        };
        
        SpriteCallback text_scale_done = new SpriteCallback() {
			@Override
			public void callback(Sprite sprite, double currenttime) {
				SpriteCallback text_fade_done = (SpriteCallback) getArg(0);
				sprite.fadeTo(new Color(0, 0, 0, 0), currenttime, 0.3, text_fade_done);
			}
        };
        
        text_fade_done.setArgs(this.layerEffects);
        text_scale_done.setArgs(text_fade_done);
        
        text.setCenter(new Point(0, 4));    
        text.scaleTo(new Point(2, 2), this.currentTime, 0.7, text_scale_done);
        this.layerEffects.addSubSprite(text);
	}

	public List<Block> sortBlocksZigZag(List<Block> blocks) {
		final int start_y = blocks.get(0).getBoardY();
		
		Collections.sort(blocks, new Comparator<Block>() {
			public int compare(Block o1, Block o2) {
				if(o1.getBoardY() != o2.getBoardY()) {
					return o2.getBoardY() - o1.getBoardY();
				}

				if(o1.getBoardX() != o2.getBoardX()) {
					if( (o1.getBoardY() - start_y) % 2 == 0) {
						return o2.getBoardX() - o1.getBoardX();
					} else {
						return o1.getBoardX() - o2.getBoardX();
					}
				}
				
				return 0;
			}
		});
		return blocks;
	}
	
	public void removeBlocks(List<Block> blocks) {
		List<Block> scale_blocks = new LinkedList<Block>(blocks);
		scale_blocks = this.sortBlocksZigZag(scale_blocks);
		
		this.hourglass.addPause(blocks.size()*Config.PAUSE_TIME_PER_BLOCK);
		this.hourglass.halt();
		
		double delay = 0.7 / blocks.size();
		
		if(delay > 0.08) {
			delay = 0.08;
		}
		
		SpriteCallback block_scale_done = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				List<Block> scale_blocks = (List<Block>) getArg(0);
				List<Block> blocks = (List<Block>) getArg(1);
				double delay = (Double) getArg(2);
				Scene_MainGame maingame = (Scene_MainGame) getArg(3);
				
				if(scale_blocks.size() > 0) {
					Block next_block = scale_blocks.get(0);
					scale_blocks.remove(next_block);
					maingame.addBlockScore(next_block);
					next_block.fadeTo(new Color(0.0f,0.0f,0.0f,0.0f), currenttime, delay, this);
					next_block.rotateTo(720.0, currenttime, delay, null);
					next_block.scaleTo(new Point(4.0,4.0), currenttime, delay, null);
				} else {
					for(Block block : blocks) {
						Scene_MainGame.getInstance().board.clear(block.getBoardX(), block.getBoardY());
					}
					maingame.hourglass.unhalt();
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
		
		block_scale_done.setArgs(scale_blocks, blocks, delay, this);
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

	public int getBlocksToLevel() {
		return 20;
	}
	
	public int getActiveBlock() {
		int idx = (this.level-1) % this.activeBlocks.size();
	    return this.activeBlocks.get(idx);
	}
	
	public void setLevel(int level) {
		this.level = level;
		this.blocksToLevel = this.getBlocksToLevel();
		this.activeBlock = this.getActiveBlock();
        this.doLevelUp = false;
        this.levelboard.setNewLevel(this.level, this.activeBlock, this.blocksToLevel);
	}

	public void newLevel() {
		this.setLevel(this.level+1);
		this.blockCount = 0;
		this.hourglass.scaleValue(0.8);
		this.refillUpperHalfBoard();
		
		this.playMusicForLevel();
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
				this.addCircleScore(circleevent.rotationBlocks, false);
				this.removeBlocks(circleevent.rotationBlocks);
			}
		}
		
		if(event.type == Event.Type.GAME_OVER) {
			this.showGameOver();
		}
		
		if(event.type == Event.Type.LEVEL_UP) {
			this.doLevelUp = true;
		}

		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				
				if(this.state == State.RUNNING) {
					switch(keyevent.key) {
					    case Input.Keys.NUM_1:
					    	this.musics.get(0).setVolume(1.0f - this.musics.get(0).getVolume());
					    	break;
					    case Input.Keys.NUM_2:
					    	this.musics.get(1).setVolume(1.0f - this.musics.get(1).getVolume());
					    	break;
					    case Input.Keys.NUM_3:
					    	this.musics.get(2).setVolume(1.0f - this.musics.get(2).getVolume());
					    	break;
					    case Input.Keys.NUM_4:
					    	this.musics.get(3).setVolume(1.0f - this.musics.get(3).getVolume());
					    	break;
					    case Input.Keys.NUM_5:
					    	this.musics.get(4).setVolume(1.0f - this.musics.get(4).getVolume());
					    	break;
					    case Input.Keys.NUM_6:
					    	this.musics.get(5).setVolume(1.0f - this.musics.get(5).getVolume());
					    	break;
					    case Input.Keys.NUM_7:
					    	this.musics.get(6).setVolume(1.0f - this.musics.get(6).getVolume());
					    	break;
					    case Input.Keys.NUM_8:
					    	this.musics.get(7).setVolume(1.0f - this.musics.get(7).getVolume());
					    	break;
					    	
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

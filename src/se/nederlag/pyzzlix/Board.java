package se.nederlag.pyzzlix;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventCircleFound;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.sun.org.apache.bcel.internal.generic.ArithmeticInstruction;

public class Board extends Sprite {
	private int width;
	private int height;
	private Scene scene;
	
	private Sprite background;
	private Sprite blocks;
	private Sprite border;
	private Sprite glow;
	private Marker marker;
	private boolean blocksFalling;
	private boolean gameOver;
	
	private Color glowColor;
	private double glowDuration;
	
	private List<List<Block>> grid;
	private List<Pair<Integer,Integer>> lastRotated;
	
	public int triangleArea(Pair<Integer,Integer> a, Pair<Integer,Integer> b, Pair<Integer,Integer> c) {
		return (b.left - a.left) * (c.right - a.right) - (c.left - a.left)*(b.right - a.right);
	}

	public int polygonArea(List<Pair<Integer,Integer>> points) {
		Pair<Integer,Integer> start = points.get(0);
		Pair<Integer,Integer> prev = points.get(1);
		int area = 0;
		int length = points.size();
		
		for(int i=0; i<length; i++) {
			area += this.triangleArea(start, prev, points.get(i));
			prev = points.get(i);
		}
		
		return area;
	}
	
	public Board(Scene scene, int width, int height) {
		this.scene = scene;
		this.width = width;
		this.height = 2*height;
		this.glowColor = new Color(0,0,0,0);
		this.grid = new ArrayList<List<Block>>();
		this.lastRotated = new LinkedList<Pair<Integer,Integer>>();
		this.reset();
		
		this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")));
		this.background.setScale(new Point(160.0, 208.0));
		this.background.setCol(new Color(0.0f, 0.0f, 0.0f, 0.3f));
		this.background.setPos(new Point(8.0, 8.0));
		
		this.blocks = new Sprite();
		this.blocks.setPos(new Point(8.0, 8.0));
		
		this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 24, 0, 176, 232);
		this.border.setPos(new Point(0,0));

		this.glow = new Sprite(new Texture(Gdx.files.internal("data/windowglows.png")), 24, 0, 176, 232);
		this.glow.setPos(new Point(0,0));
		this.glow.setCol(new Color(0,0,0,0));
		
		this.marker = new Marker();
		this.marker.offsetX = this.blocks.calcPos(0.0).getX();
		this.marker.offsetY = this.blocks.calcPos(0.0).getY() + 2*height*16 - 2*16;
		this.marker.moveToBoardCoord(2, 14, this.currentTime);
		
		this.addSubSprite(this.background);
		this.addSubSprite(this.blocks);
		this.addSubSprite(this.border);
		this.addSubSprite(this.glow);
		this.addSubSprite(this.marker);
	}
	
	public int getBoardWidth() {
		return this.width;
	}

	public int getBoardHeight() {
		return this.height/2;
	}
	
	public Marker getMarker() {
		return this.marker;
	}

	public Sprite getBlocks() {
		return this.blocks;
	}

	public void pulseBorder(Color col1, Color col2, double duration) {
		this.glowDuration = duration;
		
		SpriteCallback fade_to_done;
		SpriteCallback fade_from_done;
		
		fade_to_done = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Color col = (Color) getArg(0);
				double duration = (Double) getArg(1);
				Color glowcol = (Color) getArg(2);
				glowcol.set(col);
				sprite.fadeTo(col, currenttime, duration, (SpriteCallback) getArg(3));
			}			
		};
		
		fade_from_done = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Color col = (Color) getArg(0);
				double duration = (Double) getArg(1);
				Color glowcol = (Color) getArg(2);
				glowcol.set(col);
				sprite.fadeTo(col, currenttime, duration, (SpriteCallback) getArg(3));
			}			
		};
		
		fade_to_done.setArgs(col1, (Double)duration, this.glowColor, fade_from_done);
		fade_from_done.setArgs(col2, (Double)duration, this.glowColor, fade_to_done);

		this.glow.clearColCallbacks();

		fade_to_done.callback(this.glow, this.currentTime);
	}

	public void stopPulseBorder() {
		Color from_col = new Color(this.glowColor);
		from_col.a = 0.0f;
		this.glow.fadeTo(from_col, this.currentTime, this.glowDuration, null);
	}
	
	public void reset() {
		int width = this.grid.size();
		for(int x=0; x<width; x++) {
			if(this.grid.get(x) != null) {
				int height = this.grid.get(x).size();
				for(int y=0; y<height; y++) {
					this.clear(x, y);
				}
			}
		}
		
		this.grid.clear();
		this.gameOver = false;
		this.lastRotated.clear();
		
		for(int x=0; x<this.width; x++) {
			ArrayList<Block> list = new ArrayList<Block>(); 
			this.grid.add(list);
			
			for(int y=0; y<this.height; y++) {
				list.add(null);
			}
		}		
	}
	
	public boolean full() {
		for(List<Block> list : this.grid) {
			if(list != null) {
				for(Block block : list) {
					if(block == null) {
						return false;
					}
				}
			} else {
				throw new IllegalStateException("Board is not initialized");
			}
		}
		
		return true;
	}
	
	public boolean inactive() {
		for(List<Block> list : this.grid) {
			if(list != null) {
				for(Block block : list) {
					if(block != null) {
						if(block.hasStatus(Block.Status.IN_CIRCLE) || block.hasStatus(Block.Status.MOVING) || block.getComboCounter() > 0) {
							return false;
						}
					}
				}
			} else {
				throw new IllegalStateException("Board is not initialized");
			}
		}
		
		return true;
	}
	
	public void add(int x, int y, Block block) {
		this.grid.get(x).set(y, block);
		
		if(y >= this.height/2) {
			this.blocks.addSubSprite(block);
		} else {
			block.addStatus(Block.Status.OFFSCREEN);
		}
	}
	
	public void clear(int x, int y) {
		Block block = this.grid.get(x).get(y);
		
		if(block != null && this.blocks.getSubSprites().contains(block)) {
			this.blocks.removeSubSprite(block);
		}
		
		this.grid.get(x).set(y, null);
	}
	
	public Block getBlockAt(int x, int y) {
		return this.grid.get(x).get(y);
	}
	
	public void updateGameOver() {
		for(List<Block> list : this.grid) {
			if(list != null) {
				for(Block block : list) {
					if(block == null) {
						return;
					}
				}
			}
		}
		
		EventHandler.getInstance().post(new Event(Event.Type.GAME_OVER));
	}
	
	public List<Block> handleCircle(List<Pair<Integer,Integer>> points) {
		if(this.polygonArea(points) < 0) {
			Collections.reverse(points);
		}

		LinkedList<Block> blocks = new LinkedList<Block>();
		Pair<Integer,Integer> last_point = null;
		
		for(Pair<Integer,Integer> point : points) {
			int x = point.left;
			int y = point.right;
			
			this.grid.get(x).get(y).addStatus(Block.Status.IN_CIRCLE);
			this.grid.get(x).get(y).incrementComboCounter();
			
			if(!blocks.contains(this.grid.get(x).get(y))) {
				blocks.add(this.grid.get(x).get(y));
			}
			
			if(last_point != null) {
				int x_dir = 0;
				int y_dir = 0;
				
				if(point.right - last_point.right == 1) {
					x_dir = -1; 
				}
				if(point.right - last_point.right == -1) {
					x_dir = 1; 
				}
				if(point.left - last_point.left == 1) {
					y_dir = 1; 
				}
				if(point.left - last_point.left == -1) {
					y_dir = -1; 
				}
				
				while(!points.contains(new Pair<Integer,Integer>(x+x_dir, y+y_dir))) {
					x = x+x_dir;
					y = y+y_dir;
					
					if(this.grid.get(x).get(y) != null && !this.grid.get(x).get(y).hasStatus(Block.Status.IN_CIRCLE)) {
						this.grid.get(x).get(y).addStatus(Block.Status.IN_CIRCLE);

						if(!blocks.contains(this.grid.get(x).get(y))) {
							blocks.add(this.grid.get(x).get(y));
							this.grid.get(x).get(y).incrementComboCounter();
						}
					}
				}
			}
			
			last_point = point;
		}
		
		return blocks;
	}
	
	public boolean findCircle(List<Pair<Integer,Integer>> rotationPoints, List<Pair<Integer,Integer>> fallPoints) {
		if(this.gameOver) {
			return false;
		}
		
		class Finder {
			private int width;
			private int height;
			private List<List<Block>> grid;
			
			public Finder(List<List<Block>> grid, int width, int height) {
				this.grid = grid;
				this.width = width;
				this.height = height;
			}
			
			public List<Pair<Integer,Integer>> find(int x, int y, LinkedList<Pair<Integer,Integer>> path, Pair<Integer,Integer> first_point, int type) {
				if(x < 0 || y < 0 || x >= this.width || y >= this.height) {
					return null;
				}
				if(this.grid.get(x).get(y) == null) {
					return null;
				}
				
				Block block = this.grid.get(x).get(y); 
				
				if(block.getType() != type) {
					return null;
				}
				if(block.hasStatus(Block.Status.MOVING) || block.hasStatus(Block.Status.IN_CIRCLE) || block.hasStatus(Block.Status.OFFSCREEN)) {
					return null;
				}
				if(path.size() > 0 && x == first_point.left && y == first_point.right) {
					return path;
				}
				if(path.contains(new Pair<Integer,Integer>(x,y))) {
					return null;
				}
				
				List<List<Pair<Integer,Integer>>> paths = new LinkedList<List<Pair<Integer,Integer>>>(); 
				
				path.add(new Pair<Integer,Integer>(x,y));
				paths.add(this.find(x, y+1, (LinkedList<Pair<Integer,Integer>>) path.clone(), first_point, type));
				paths.add(this.find(x+1, y, (LinkedList<Pair<Integer,Integer>>) path.clone(), first_point, type));
				paths.add(this.find(x-1, y, (LinkedList<Pair<Integer,Integer>>) path.clone(), first_point, type));
				paths.add(this.find(x, y-1, (LinkedList<Pair<Integer,Integer>>) path.clone(), first_point, type));
				
				List<Pair<Integer,Integer>> circle = new LinkedList<Pair<Integer,Integer>>();
				for(List<Pair<Integer,Integer>> pa : paths) {
					if(pa != null && pa.size() > circle.size()) {
						circle = pa;
					}
				}
				
				return circle;
			}
		};		
		Finder finder = new Finder(this.grid, this.width, this.height);
		
		List<Block> rotationCircles = new LinkedList<Block>();
		List<Block> fallCircles = new LinkedList<Block>();
		List<Pair<Integer,Integer>> circle;
		
		for(Pair<Integer,Integer> p : rotationPoints) {
			if(this.grid.get(p.left).get(p.right) != null) {
				circle = finder.find(p.left, p.right, new LinkedList<Pair<Integer,Integer>>(), p, this.grid.get(p.left).get(p.right).getType());
				
				if(circle != null && circle.size() >= 4) {
					rotationCircles.addAll(this.handleCircle(circle));
				}
			}
		}
		
		for(Pair<Integer,Integer> p : fallPoints) {
			if(this.grid.get(p.left).get(p.right) != null) {
				circle = finder.find(p.left, p.right, new LinkedList<Pair<Integer,Integer>>(), p, this.grid.get(p.left).get(p.right).getType());
				
				if(circle != null && circle.size() >= 4) {
					fallCircles.addAll(this.handleCircle(circle));
				}
			}
		}

		if(rotationCircles.size() > 0 || fallCircles.size() > 0) {
			EventHandler.getInstance().post(new EventCircleFound(fallCircles, rotationCircles));
		}
		
		return true;
	}
	
	public boolean rotate(int x, int y, int direction, int radius) {
		if(this.gameOver) {
			return false;
		}
		
		if(x > this.width-radius || x < 0) {
			return false;
		}

		if(y > this.height-radius || y < 0) {
			return false;
		}
		
		List<Integer> xs = new LinkedList<Integer>(); 
		
		for(int i=0; i<radius; i++) { xs.add(i); }
		for(int i=0; i<radius-1; i++) { xs.add(radius-1); }
		for(int i=radius-2; i>=0; i--) { xs.add(i); }
		for(int i=0; i<radius-1; i++) { xs.add(0); }
		
		List<Integer> ys = new LinkedList<Integer>(xs);
		
		if(direction == 1) {
			Collections.reverse(ys);
		} else {
			Collections.reverse(xs);
		}
		
		List<Pair<Integer,Integer>> points = new LinkedList<Pair<Integer,Integer>>();
		
		int imax=xs.size();
		for(int i=0; i<imax; i++) {
			points.add(new Pair<Integer,Integer>(xs.get(i), ys.get(i)));
		}
		
		Block tile = null;
		Block next_tile = null;
		
		for(Pair<Integer,Integer> point : points) {
			int xx = point.left+x;
			int yy = point.right+y;

			next_tile = this.grid.get(xx).get(yy);
			
			if(next_tile == null) {
				return false;
			}
			
			if(next_tile != null && (next_tile.hasStatus(Block.Status.IN_CIRCLE) || next_tile.hasStatus(Block.Status.IN_CIRCLE) || next_tile.hasStatus(Block.Status.OFFSCREEN))) {
				return false;
			}
		}
		
		tile = null;
		this.lastRotated.clear();
		
		for(Pair<Integer,Integer> point : points) {
			int xx = point.left+x;
			int yy = point.right+y;
			
			this.lastRotated.add(new Pair<Integer,Integer>(xx, yy));
			
			next_tile = this.grid.get(xx).get(yy);
			this.grid.get(xx).set(yy, tile); 
			
			if(tile != null) {
				this.moveBlock(tile, xx, yy);
			}
			
			tile = next_tile;
		}
		
		return true;
	}
	
	public void moveBlock(Block block, int x, int y) {
		block.moveToBoardCoord(x, y, this.currentTime);
		
		if(y < this.height/2) {
			block.addStatus(Block.Status.OFFSCREEN);
		} else {
			if(!this.blocks.getSubSprites().contains(block)) {
				this.blocks.addSubSprite(block);
			}
			
			block.removeStatus(Block.Status.OFFSCREEN);
		}
	}
	
	public void updateBoard() {
		if(this.gameOver) {
			return;
		}
		
		List<Pair<Integer,Integer>> points = new LinkedList<Pair<Integer,Integer>>();

		for(int y=this.height-2; y>=0; y--) {
			for(int x=0; x<this.width; x++) {
				Block tile_over = this.grid.get(x).get(y);
				Block tile_under = this.grid.get(x).get(y+1);
				
				if(tile_under != null && y+1 == this.height-1) {
					if(tile_under.getGravityDelay() == 0) {
						points.add(new Pair<Integer,Integer>(x,y+1));
					}
					
					tile_under.setGravityDelay(Config.DEFAULT_GRAVITY_DELAY);
					tile_under.removeStatus(Block.Status.MOVING);
					
					if(!tile_under.hasStatus(Block.Status.IN_CIRCLE)) {
						tile_under.resetComboCounter();
					}
				}
				
				if(tile_over != null && tile_under == null) {
					if(!tile_over.hasStatus(Block.Status.WEIGHTLESS) && !tile_over.hasStatus(Block.Status.IN_CIRCLE)) {
						if(tile_over.getGravityDelay() <= 0) {
							this.grid.get(x).set(y, null);
							this.grid.get(x).set(y+1, tile_over);
							tile_over.setGravityDelay(0);
							tile_over.addStatus(Block.Status.MOVING);
							this.moveBlock(tile_over, x, y+1);
						} else {
							tile_over.decrementGravityDelay();
						}
					}
				} else if(tile_over != null && tile_under != null) {
					if(tile_over.getGravityDelay() == 0 && tile_under.getGravityDelay() != 0) {
						points.add(new Pair<Integer,Integer>(x,y));
					}
					
					tile_over.setGravityDelay(tile_under.getGravityDelay());
					
					if(!tile_over.hasStatus(Block.Status.MOVING) && !tile_over.hasStatus(Block.Status.IN_CIRCLE)) {
						tile_over.setComboCounter(tile_under.getComboCounter());
					} else {
						int max_counter = Math.max(tile_over.getComboCounter(), tile_under.getComboCounter());
						
						tile_over.setComboCounter(max_counter);
						tile_under.setComboCounter(max_counter);
					}
					
					if(tile_under.hasStatus(Block.Status.MOVING)) {
						tile_over.addStatus(Block.Status.MOVING);
					} else {
						tile_over.removeStatus(Block.Status.MOVING);
					}					
				}
			}
		}
		
		if(points.size() > 0 || this.lastRotated.size() > 0) {
			this.findCircle(this.lastRotated, points);
		}
		
		this.lastRotated.clear();
	}
}

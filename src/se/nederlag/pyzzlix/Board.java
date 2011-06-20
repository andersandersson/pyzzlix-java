package se.nederlag.pyzzlix;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

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
	
	public static double triangleArea(Point a, Point b, Point c) {
		return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (c.getX() - a.getX())*(b.getY() - a.getY());
	}

	public static double polygonArea(Point[] points) {
		Point start = points[0];
		Point prev = points[1];
		double area = 0.0;
		int length = points.length;
		
		for(int i=0; i<length; i++) {
			area += Board.triangleArea(start, prev, points[i]);
			prev = points[i];
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
		this.background.setPos(new Point(8.0, 16.0));
		
		this.blocks = new Sprite();
		this.blocks.setPos(new Point(8.0, 16.0));
		
		this.border = new Sprite(new Texture(Gdx.files.internal("data/windowframes.png")), 24, 0, 176, 232);
		this.border.setPos(new Point(0,0));

		this.glow = new Sprite(new Texture(Gdx.files.internal("data/windowglows.png")), 24, 0, 176, 232);
		this.glow.setPos(new Point(0,0));
		this.glow.setCol(new Color(0,0,0,0));
		
		this.marker = new Marker();
		this.marker.offsetX = this.blocks.calcPos(0.0).getX();
		this.marker.offsetY = this.blocks.calcPos(0.0).getY() - height*16.0;
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
	
	public List<Block> handleCircle(Pair<Integer,Integer> points) {
		return null;
	}
	
	public boolean findCircle(Pair<Integer,Integer> rotation_points, Pair<Integer,Integer> fall_points) {
		return false;
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
		
	}
}

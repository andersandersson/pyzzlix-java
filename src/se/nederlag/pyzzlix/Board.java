package se.nederlag.pyzzlix;

import java.util.List;
import java.util.ArrayList;

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
	
	private Color glowColor;
	private double glowDuration;
	
	private List<List<Block>> grid;
	
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
		this.height = height;
		this.glowColor = new Color(0,0,0,0);
		
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
		
		this.grid = new ArrayList<List<Block>>();
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
		
	}
	
	public boolean full() {
		return true;
	}
	
	public boolean inactive() {
		return false;
	}
	
	public void add(int x, int y, Block block) {
		
	}
	
	public void clear(int x, int y) {
		
	}
	
	public void updateGameOver() {
		
	}
	
	public List<Block> handleCircle(Pair<Integer,Integer> points) {
		return null;
	}
	
	public boolean findCircle(Pair<Integer,Integer> rotation_points, Pair<Integer,Integer> fall_points) {
		return false;
	}
	
	public boolean rotate(int x, int y, int direction, int radius) {
		return false;
	}
	
	public void moveBlock(Block block, int x, int y) {
		
	}
	
	public void updateBoard() {
		
	}
}

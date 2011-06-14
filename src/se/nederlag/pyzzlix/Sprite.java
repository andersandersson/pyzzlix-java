package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite{
	private List<Sprite> subSprites = new LinkedList<Sprite>();
	private Animation currentAnimation = null;

	private double currentTime = 0.0;
	
	private Point pos = new Point(0,0);
	private double _pos_lasttime = 0.0;
	private Point _pos_ref = new Point(0,0);
	private double _pos_reftime = 0.0;
	private LinkedList<SpriteCallback> _pos_callbacks = new LinkedList<SpriteCallback>();
	
	private Point scale = new Point(0,0);
	private double _scale_lasttime = 0.0;
	private Point _scale_ref = new Point(0,0);
	private double _scale_reftime = 0.0;
	private LinkedList<SpriteCallback> _scale_callbacks = new LinkedList<SpriteCallback>();
	
	private double rot = 0.0;
	private double _rot_lasttime = 0.0;
	private double _rot_ref = 0.0;
	private double _rot_reftime = 0.0;
	private LinkedList<SpriteCallback> _rot_callbacks = new LinkedList<SpriteCallback>();
	
	private Color col = new Color(1,1,1,1);
	private float _col_lasttime = 0;
	private Color _col_ref = new Color(1,1,1,1);
	private float _col_reftime = 0;
	private LinkedList<SpriteCallback> _col_callbacks = new LinkedList<SpriteCallback>();
	
	public Sprite() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Sprite(Sprite sprite) {
		super(sprite);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(texture, srcX, srcY, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture, int srcWidth, int srcHeight) {
		super(texture, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(Texture texture) {
		super(texture);
		// TODO Auto-generated constructor stub
	}

	public Sprite(TextureRegion region, int srcX, int srcY, int srcWidth,
			int srcHeight) {
		super(region, srcX, srcY, srcWidth, srcHeight);
		// TODO Auto-generated constructor stub
	}

	public Sprite(TextureRegion region) {
		super(region);
		// TODO Auto-generated constructor stub
	}

	public Point calcPos(double currenttime)
	{
		if(_pos_reftime <= currenttime) {
			return _pos_ref;
		}
		else
		{
			double factorT = (_pos_reftime - currenttime) / (_pos_reftime - _pos_lasttime);
			return new Point(_pos_ref.x - (_pos_ref.x - pos.x) * factorT, _pos_ref.y - (_pos_ref.y - pos.y) * factorT);
		}
	}

	public void updatePos(double currenttime)
	{
		if(_pos_reftime <= currenttime)
		{
			if(_pos_callbacks.size() > 0)
			{
				LinkedList<SpriteCallback> callbacks = (LinkedList<SpriteCallback>) _pos_callbacks.clone();
				_pos_callbacks.clear();
				
				for(SpriteCallback callback : callbacks)
				{
					callback.callback(this, currenttime);
				}
			}
		}
		
		pos = calcPos(currenttime);
		_pos_lasttime = currenttime;
	}
	
	public Color calcCol(double currenttime)
	{
		if(_col_reftime <= currenttime) {
			return _col_ref;
		}
		else
		{
			float factorT = (_col_reftime - (float)currenttime) / (_col_reftime - _col_lasttime);
			return new Color(_col_ref.r - (_col_ref.r - col.r) * factorT, 
							 _col_ref.g - (_col_ref.g - col.g) * factorT,
							 _col_ref.b - (_col_ref.b - col.b) * factorT,
							 _col_ref.a - (_col_ref.a - col.a) * factorT);
		}
	}

	public void updateCol(double currenttime)
	{
		if(_col_reftime <= currenttime)
		{
			if(_col_callbacks.size() > 0)
			{
				LinkedList<SpriteCallback> callbacks = (LinkedList<SpriteCallback>) _col_callbacks.clone();
				_col_callbacks.clear();
				
				for(SpriteCallback callback : callbacks)
				{
					callback.callback(this, currenttime);
				}
			}
		}
		
		col = calcCol(currenttime);
		_col_lasttime = (float)currenttime;
	}
	
	public double calcRot(double currenttime)
	{
		if(_rot_reftime <= currenttime) {
			return _rot_ref;
		}
		else
		{
			double factorT = (_rot_reftime - currenttime) / (_rot_reftime - _rot_lasttime);
			return _rot_ref - (_rot_ref - rot) * factorT;
		}
	}

	public void updateRot(double currenttime)
	{
		if(_rot_reftime <= currenttime)
		{
			if(_rot_callbacks.size() > 0)
			{
				LinkedList<SpriteCallback> callbacks = (LinkedList<SpriteCallback>) _rot_callbacks.clone();
				_rot_callbacks.clear();
				
				for(SpriteCallback callback : callbacks)
				{
					callback.callback(this, currenttime);
				}
			}
		}
		
		rot = calcRot(currenttime);
		_rot_lasttime = currenttime;
	}
	
	public List<Sprite> getSubSprites() {
		return subSprites;
	}

	public void setAnimation(Animation animation)
	{
		currentAnimation = animation;
	}
	
	public void update(double currenttime)
	{
		currentTime = currenttime;
		
		updatePos(currentTime);
		updateCol(currentTime);
		updateRot(currentTime);
		
		if(currentAnimation != null) 
		{
			currentAnimation.updateFrame(currenttime);
		}
		
		for(Sprite sprite : subSprites)
		{
			sprite.update(currenttime);
		}
	}
	
	public void moveTo(Point pos, double currenttime, double duration, SpriteCallback callback)
	{
		updatePos(currenttime);
		
		this._pos_ref = pos;
		this._pos_reftime = currenttime + duration;
		
		if(callback != null)
		{
			this._pos_callbacks.add(callback);
		}
	}

	public void setPos(Point pos) {
		this.pos = pos;
		this._pos_ref = pos;
		this._pos_reftime = 0.0;
	}

	public void fadeTo(Color col, double currenttime, double duration, SpriteCallback callback)
	{
		updateCol(currenttime);
		
		this._col_ref = col;
		this._col_reftime = (float)currenttime + (float)duration;
		
		if(callback != null)
		{
			this._col_callbacks.add(callback);
		}
	}

	public void setCol(Color col) {
		this.col = col;
		this._col_ref = col;
		this._col_reftime = 0;
	}

	public void rotateTo(double rot, double currenttime, double duration, SpriteCallback callback)
	{
		updateRot(currenttime);
		
		this._rot_ref = rot;
		this._rot_reftime = (float)currenttime + (float)duration;
		
		if(callback != null)
		{
			this._rot_callbacks.add(callback);
		}
	}

	public void setRot(double rot) {
		this.rot = rot;
		this._rot_ref = rot;
		this._rot_reftime = 0;
	}
}

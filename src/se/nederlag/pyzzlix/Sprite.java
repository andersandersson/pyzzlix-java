package se.nederlag.pyzzlix;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Sprite extends com.badlogic.gdx.graphics.g2d.Sprite{
	private List<Sprite> subSprites = new LinkedList<Sprite>();
	private Animation currentAnimation = null;

	private double lastTime = 0.0;
	private double currentTime = 0.0;
	
	private Point pos = new Point(0,0);
	private double _pos_lasttime = 0.0;
	private Point _pos_ref = new Point(0,0);
	private double _pos_reftime = 0.0;
	private LinkedList<SpriteCallback> _pos_callbacks = new LinkedList<SpriteCallback>();
	
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

	public List<Sprite> getSubSprites() {
		return subSprites;
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
		this._pos_ref = pos;
		this._pos_reftime = 0.0;
	}

	public void setAnimation(Animation animation)
	{
		currentAnimation = animation;
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
	
	public void update(double currenttime)
	{
		lastTime = currenttime;
		currentTime = currenttime;
		
		updatePos(currentTime);
		
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
}

package se.nederlag.pyzzlix;

import com.badlogic.gdx.graphics.Color;

public class Scene_MainGame extends Scene {
	private static Scene_MainGame instance = null;
	
	private Scene_MainGame() {
		Font font = new Font("data/font_normal.png", 8, 8);
		Text text = new Text(50, 50, font, "ANDERS");
		Board board = new Board(this, 10, 13);
		board.setPos(new Point(24, 0));
		sprites.add(board);
		sprites.add(text);
		board.pulseBorder(new Color(1,0,0,1), new Color(0,1,0,1), 1.0);
	}
	
	public static Scene_MainGame getInstance()
	{
		if (instance == null)
			instance = new Scene_MainGame();
		return instance;
	}
	
	@Override
	public void tick() {
	}
	
}

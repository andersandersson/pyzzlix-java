package se.nederlag.pyzzlix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

public class Scene_GameOver extends Scene {
	private static Scene_GameOver instance = new Scene_GameOver();

	private Menu menu;
	private Font font;
	private Sprite background;

	private Scene_GameOver() {
		this.font = new Font("data/font_fat.png", 8, 8);
		
		this.updateBlocker = true;
		
        this.background = new Sprite(new Texture(Gdx.files.internal("data/pixel.png")), 1, 1);
        this.background.setScale(new Point(320, -240));
        this.background.setPos(new Point(0, -1));
        this.background.setCol(new Color(0, 0, 0, 0.7f));

        this.menu = new Menu();
        this.menu.setPos(new Point(160, 120));
        //this.menu.add(MenuItem(0, 0, this.font, "Play again", self.menu_playAgain));
        //this.menu.add(MenuItem(0, 24, this.font, "Exit to menu", self.menu_quit));
        this.menu.add(new MenuItem(0, 0, this.font, "Play again", null, Text.Anchor.CENTER));
        this.menu.add(new MenuItem(0, 24, this.font, "Exit to menu", null, Text.Anchor.CENTER));
        this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        
        this.addSprite(this.background);
        this.addSprite(this.menu);
	}
	
	public static Scene_GameOver getInstance() {
		return Scene_GameOver.instance;
	}
	
	public boolean handleEvent(Event event) {
		if(event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;
						
			if(keyevent.state == EventKeyState.State.DOWN) {
				switch(keyevent.key) {
					case Input.Keys.UP:
						this.menu.prevItem();
						break;
					case Input.Keys.DOWN:
						this.menu.nextItem();
						break;
					case Input.Keys.ENTER:
						this.menu.selectItem();
						break;
				}
			}
		}
		return true;
	}
	
	@Override
	public void tick() {
	}

	public void display(int level, int score) {
		SceneHandler.getInstance().pushScene(this);
	}	
}

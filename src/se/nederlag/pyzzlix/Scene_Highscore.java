package se.nederlag.pyzzlix;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.nederlag.pyzzlix.audio.Sound;
import se.nederlag.pyzzlix.audio.Audio;
import se.nederlag.pyzzlix.events.Event;
import se.nederlag.pyzzlix.events.EventKeyState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class Scene_Highscore extends Scene {
	private class HighscoreEntry {
		public String name;
		public int score;
		public int level;
		public int position;
		public Text text;

		public HighscoreEntry(String name, int score, int level, Text text,
				int position) {
			this.name = name;
			this.score = score;
			this.level = level;
			this.text = text;
			this.position = position;
		}

		@Override
		public HighscoreEntry clone() {
			return new HighscoreEntry(this.name, this.score, this.level,
					this.text.clone(), this.position);
		}
	};

	private static Scene_Highscore instance = null;

	public enum State {
		SHOWING, FADING;
	};

	private Menu menu;
	private Font font;
	private Sprite background;
	private State state;
	private boolean hasMenu;

	private Callback replayCallback;
	private Callback exitCallback;

	private Text titletext;

	private List<HighscoreEntry> highscores;

	private Sound menumove;
	private Sound selectsound;
	
	private Scene_Highscore() {
		this.font = new Font("data/font_fat.png", 8, 8);

		this.titletext = new Text(0, 74, this.font, "HIGHSCORES");
		this.titletext.setAnchor(Text.Anchor.CENTER);
		this.titletext.setScale(new Point(2.0, 2.0));

		this.background = new Sprite(new Texture(
				Gdx.files.internal("data/pixel.png")), 1, 1);
		this.background.setScale(new Point(320, 240));
		this.background.setPos(new Point(-160, -120));
		this.background.setCol(new Color(0, 0, 0, 0.7f));

		this.updateBlocker = false;

        Callback menuReplayCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_Highscore highscore = (Scene_Highscore) args[0];
				highscore.menu_playAgain();
				return null;
			}
        };
        
        Callback menuExitCallback = new Callback(this) {
			public Object call(Object... params) {
				Scene_Highscore highscore = (Scene_Highscore) args[0];
				highscore.menu_quit();
				return null;
			}
        };
        
        
        this.menu = new Menu();
		this.menu.setPos(new Point(0, -92));
		this.menu.add(new MenuItem(0, 24, this.font, "Play again", menuReplayCallback, Text.Anchor.CENTER));
		this.menu.add(new MenuItem(0, 0, this.font, "Exit to menu", menuExitCallback, Text.Anchor.CENTER));
		this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));

		this.addSprite(this.background);

		this.highscores = new LinkedList<HighscoreEntry>();

		for (int i = 0; i < 10; i++) {
			HighscoreEntry score = new HighscoreEntry("AAA", 0, 0, new Text(0, 52 - i * 10, this.font, "---"), i);
			this.highscores.add(score);
			this.updateHighscore(score, "AAA", 0, 0);
			this.addSprite(this.highscores.get(i).text);
		}

		this.state = State.SHOWING;
		this.hasMenu = false;

		this.addSprite(this.titletext);
		this.addSprite(this.menu);

		this.menumove = Resources.getSound("menumove");
		this.selectsound = Resources.getSound("menuselect");

		this.loadHighscores();

		this.replayCallback = null;
		this.exitCallback = null;
	}

	public static Scene_Highscore getInstance() {
		if (instance == null)
			instance = new Scene_Highscore();
		return instance;
	}

	public void display(Callback replayCallback, Callback exitCallback) {
		this.replayCallback = replayCallback;
		this.exitCallback = exitCallback;

		if (this.replayCallback == null && this.exitCallback == null) {
			this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 0.0f));
			this.hasMenu = false;
		} else {
			this.menu.setCol(new Color(1.0f, 1.0f, 1.0f, 1.0f));
			this.menu.focusItem(0);
			this.hasMenu = true;
		}

		SceneHandler.getInstance().pushScene(this);
		this.fadeIn(0.5);
	}

	public void loadHighscores() {
		Gdx.files.external(".pyzzlix").mkdirs();
		String data = Gdx.files.external(".pyzzlix/highscores.txt").readString();
		String[] items = data.split("\n");
		
		Pattern p = Pattern.compile("([^:]*):([^:]*):([^:]*)");
		for(String row : items) {
			Matcher m = p.matcher(row);
			if(m.matches()) {
				this.addNewHighscore(m.group(1), Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)));
			}
		}
	}

	public void resetHighscores() {
		for (HighscoreEntry entry : this.highscores) {
			this.updateHighscore(entry, "AAA", 0, 0);
		}
		
		this.saveHighscores();
	}

	public void saveHighscores() {
		Gdx.files.external(".pyzzlix").mkdirs();
		OutputStream out = Gdx.files.external(".pyzzlix/highscores.txt").write(false);

		for (HighscoreEntry entry : this.highscores) {
			try {
				out.write(entry.name.getBytes());
				out.write(":".getBytes());
				out.write(Integer.toString(entry.score).getBytes());
				out.write(":".getBytes());
				out.write(Integer.toString(entry.level).getBytes());
				out.write("\n".getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isNewHighscore(int score) {
		for (HighscoreEntry entry : this.highscores) {
			if (entry.score < score) {
				return true;
			}
		}

		return false;
	}

	public void updateHighscore(HighscoreEntry entry, String name, int score, int level) {
		entry.name = name;
		entry.score = score;
		entry.level = level;
		entry.text.setAnchor(Text.Anchor.CENTER);
		entry.text.setText(String.format("%2d. %3s: %10d LVL:%2d",
		entry.position+1, entry.name, entry.score, entry.level));
	}

	public void addNewHighscore(String name, int score, int level) {
		HighscoreEntry nextEntry = null;

		for (HighscoreEntry entry : this.highscores) {
			if (entry.score < score && nextEntry == null) {
				nextEntry = entry.clone();
				this.updateHighscore(entry, name, score, level);

			} else if (nextEntry != null) {
				HighscoreEntry tmp = entry.clone();

				this.updateHighscore(entry, nextEntry.name, nextEntry.score, nextEntry.level);

				nextEntry = tmp;
			}
		}

		this.saveHighscores();
	}

	public int getHighscore() {
		this.loadHighscores();

		return this.highscores.get(0).score;
	}

	public void tick() {
	}

	public void fadeIn(double duration) {
		this.state = State.FADING;
		this.updateBlocker = false;

		SpriteCallback fadeDone = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_Highscore highscore = (Scene_Highscore) getArg(0);
				highscore.state = State.SHOWING;
				highscore.updateBlocker = true;
			}
		};
		fadeDone.setArgs(this);

		this.background.fadeTo(new Color(0.0f, 0.0f, 0.0f, 0.7f),
				this.currentTime, duration, fadeDone);
		this.titletext.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f),
				this.currentTime, duration, null);

		for (HighscoreEntry entry : this.highscores) {
			entry.text.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f),
					this.currentTime, duration, null);
		}

		if (this.hasMenu) {
			this.menu.fadeTo(new Color(1.0f, 1.0f, 1.0f, 1.0f),
					this.currentTime, duration, null);
		}
	}

	public void fadeOutAndRemove() {
		SpriteCallback fadeDone = new SpriteCallback() {
			public void callback(Sprite sprite, double currenttime) {
				Scene_Highscore highscore = (Scene_Highscore) getArg(0);
				SceneHandler.getInstance().removeScene(highscore);
			}
		};
		fadeDone.setArgs(this);
		
		for (HighscoreEntry entry : this.highscores) {
			entry.text.fadeTo(new Color(0, 0, 0, 0), this.currentTime, 0.2,	fadeDone);
			fadeDone = null;
		}

		this.state = State.FADING;
		this.updateBlocker = false;
	}

	public void menu_playAgain() {
		this.fadeOutAndRemove();

		if (this.replayCallback != null) {
			this.replayCallback.call();
		}
	}

	public void menu_quit() {
		this.fadeOutAndRemove();

		if (this.exitCallback != null) {
			this.exitCallback.call();
		}
	}

	@Override
	public boolean handleEvent(Event event) {
		if (this.state == State.FADING) {
			return true;
		}

		if (event.type == Event.Type.KEYBOARD) {
			EventKeyState keyevent = (EventKeyState) event;

			if (keyevent.state == EventKeyState.State.DOWN) {
				if (!this.hasMenu) {
					if (keyevent.key == Input.Keys.ENTER
							|| keyevent.key == Input.Keys.ESCAPE) {
						SceneHandler.getInstance().removeScene(this);
					}
				} else {
					if (keyevent.key == Input.Keys.UP) {
						this.menu.prevItem();
					}
					Audio.playSound(this.menumove);

					if (keyevent.key == Input.Keys.DOWN) {
						this.menu.nextItem();
					}
					Audio.playSound(this.menumove);

					if (keyevent.key == Input.Keys.ENTER) {
						Audio.playSound(this.selectsound);
						this.menu.selectItem();
					}
				}
			}
		}

		return true;
	}
}

package com.udacity.game.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.udacity.game.gigagal.screens.GameplayScreen;
import com.udacity.game.gigagal.screens.HighScoresScreen;
import com.udacity.game.gigagal.screens.LevelSelectScreen;
import com.udacity.game.gigagal.screens.MenuScreen;
import com.udacity.game.gigagal.screens.OptionsScreen;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.SoundManager;

public class GigaGalGame extends Game {
	public static final String TAG = GigaGalGame.class.getName();

	private Assets assets;
	private SoundManager soundManager;
	private final String[] args;

	public boolean debug;
	public boolean debugMobile;

	public GigaGalGame(String[] args) {
		this.args = args;
		debug = false;
		debugMobile = false;
	}

	@Override
	public void create() {
		if (args != null) {
			for (String arg : args) {
				if (arg.equals("debug")) {
					debug = true;
				}
				if (arg.equals("debug_mobile")) {
					debugMobile = true;
				}
			}
		}
		Gdx.app.log(TAG, ("debug mode: " + debug));
		Gdx.app.log(TAG, ("debug mobile mode: " + debugMobile));
		assets = Assets.instance;
		assets.init();
		soundManager = SoundManager.get_instance();
		soundManager.playMusic(Constants.MUSIC_PATH);

		setScreen(new MenuScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
		soundManager.dispose();
		screen.dispose();
	}

	public void switchScreen(String screen_name) {
		switchScreen(screen_name, 1);
	}

	public void switchScreen(String screen_name, int level) {
		Gdx.input.setInputProcessor(null);

		if (screen_name.equals("gameplay")) {
			screen.dispose();
			setScreen(new GameplayScreen(this, level));
		}	else if (screen_name.equals("level select")) {
			screen.dispose();
			setScreen(new LevelSelectScreen(this));
		} else if (screen_name.equals("high_score")) {
			screen.dispose();
			setScreen(new HighScoresScreen(this));
		} else if (screen_name.equals("options")) {
			screen.dispose();
			setScreen(new OptionsScreen(this));
		} else {
			screen.dispose();
			setScreen(new MenuScreen(this));
		}
	}
}

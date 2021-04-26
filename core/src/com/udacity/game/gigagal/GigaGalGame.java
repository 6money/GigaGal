package com.udacity.game.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.udacity.game.gigagal.screens.GameplayScreen;
import com.udacity.game.gigagal.screens.HighScoresScreen;
import com.udacity.game.gigagal.screens.LevelSelectScreen;
import com.udacity.game.gigagal.screens.MenuScreen;
import com.udacity.game.gigagal.screens.OptionsScreen;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;

public class GigaGalGame extends Game {
	public static final String TAG = GigaGalGame.class.getName();

	private Assets assets;
	private final String[] args;
	private Music backgroundMusic;

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
		backgroundMusic = assets.musicAssets.backgroundMusic;
		backgroundMusic.setLooping(true);
		backgroundMusic.play();
		backgroundMusic.setVolume(0.6f);

		switchScreen("menu");
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}

	public void switchScreen(String screen_name) {
		switchScreen(screen_name, 1);
	}

	public void switchScreen(String screen_name, int level) {
		Gdx.input.setInputProcessor(null);

		if (screen_name.equals("gameplay")) {
			setScreen(new GameplayScreen(this, level));
		}	else if (screen_name.equals("level select")) {
			setScreen(new LevelSelectScreen(this));
		} else if (screen_name.equals("high_score")) {
			setScreen(new HighScoresScreen(this));
		} else if (screen_name.equals("options")) {
			setScreen(new OptionsScreen(this));
		} else {
			setScreen(new MenuScreen(this));
		}
	}
}

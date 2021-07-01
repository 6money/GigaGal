package com.sixmoney.gigagal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.sixmoney.gigagal.screens.GameplayScreen;
import com.sixmoney.gigagal.screens.HighScoresScreen;
import com.sixmoney.gigagal.screens.LevelSelectScreen;
import com.sixmoney.gigagal.screens.MenuScreen;
import com.sixmoney.gigagal.screens.OptionsScreen;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.SoundManager;

public class GigaGalGame extends Game {
	public static final String TAG = GigaGalGame.class.getName();

	private Assets assets;
	private SoundManager soundManager;
	private final String[] args;

	public boolean debug;
	public boolean mobileControls;

	public GigaGalGame(String[] args) {
		this.args = args;
		debug = false;
		mobileControls = false;
	}

	@Override
	public void create() {
		if (args != null) {
			for (String arg : args) {
				if (arg.equals("debug")) {
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
					debug = true;
				}
				if (arg.equals("mobile_browser")) {
					mobileControls = true;
				}
			}
		}
		Gdx.app.log(TAG, ("debug mode: " + debug));

		if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
			mobileControls = true;
		}

		Gdx.input.setCatchKey(Input.Keys.DOWN, true);
		Gdx.input.setCatchKey(Input.Keys.UP, true);
		Gdx.input.setCatchKey(Input.Keys.SPACE, true);

		assets = Assets.get_instance();
		soundManager = SoundManager.get_instance();
		soundManager.startBackgroundMusic();

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

		switch (screen_name) {
			case "gameplay":
				Gdx.app.log(TAG, "starting level" + level);
				setScreen(new GameplayScreen(this, level));
				break;
			case "level select":
				setScreen(new LevelSelectScreen(this));
				break;
			case "high_score":
				setScreen(new HighScoresScreen(this));
				break;
			case "options":
				setScreen(new OptionsScreen(this));
				break;
			default:
				setScreen(new MenuScreen(this));
				break;
		}
	}
}

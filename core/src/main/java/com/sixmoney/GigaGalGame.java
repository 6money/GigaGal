package com.sixmoney;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sixmoney.screens.GameplayScreen;
import com.sixmoney.screens.HighScoresScreen;
import com.sixmoney.screens.LevelSelectScreen;
import com.sixmoney.screens.MenuScreen;
import com.sixmoney.screens.OptionsScreen;
import com.sixmoney.utils.Assets;
import com.sixmoney.utils.Constants;
import com.sixmoney.utils.SoundManager;

public class GigaGalGame extends Game {
	public static final String TAG = GigaGalGame.class.getName();

	private Assets assets;
	private SoundManager soundManager;
	private final String[] args;
	private MenuScreen menuScreen;
	private GameplayScreen gameplayScreen;
	private HighScoresScreen highScoresScreen;
	private LevelSelectScreen levelSelectScreen;
	private OptionsScreen optionsScreen;

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

		switch (screen_name) {
			case "gameplay":
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
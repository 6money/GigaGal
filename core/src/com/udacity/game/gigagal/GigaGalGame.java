package com.udacity.game.gigagal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.udacity.game.gigagal.screens.GameplayScreen;
import com.udacity.game.gigagal.screens.HighScoresScreen;
import com.udacity.game.gigagal.screens.LevelSelectScreen;
import com.udacity.game.gigagal.screens.MenuScreen;
import com.udacity.game.gigagal.utils.Assets;

public class GigaGalGame extends Game {
	private Assets assets;

	@Override
	public void create() {
		assets = Assets.instance;
		assets.init();
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
		} else {
			setScreen(new MenuScreen(this));
		}
	}
}

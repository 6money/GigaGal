package com.udacity.game.gigagal.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.udacity.game.gigagal.GigaGalGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "GigaGal";
		config.height = 480;
		config.width = 900;
		new LwjglApplication(new GigaGalGame(arg), config);
	}
}

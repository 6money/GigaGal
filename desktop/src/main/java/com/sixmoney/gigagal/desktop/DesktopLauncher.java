package com.sixmoney.gigagal.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Constants;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
	public static void main(String[] args) {
		createApplication(args);
	}

	private static LwjglApplication createApplication(String[] args) {
		return new LwjglApplication(new GigaGalGame(args), getDefaultConfiguration());
	}

	private static LwjglApplicationConfiguration getDefaultConfiguration() {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "GigaGal";
		configuration.width = MathUtils.round(Constants.WINDOW_WIDTH);
		configuration.height = MathUtils.round(Constants.WINDOW_HEIGHT);
		configuration.foregroundFPS = 144;
		configuration.backgroundFPS = 30;
		//// This prevents a confusing error that would appear after exiting normally.
		configuration.forceExit = false;
		
		for (int size : new int[] { 128, 64, 32, 16 }) {
			configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
		}
		return configuration;
	}
}
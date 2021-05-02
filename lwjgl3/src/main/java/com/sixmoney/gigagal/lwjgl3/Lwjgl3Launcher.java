package com.sixmoney.gigagal.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Constants;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
	public static void main(String[] args) {
		createApplication(args);
	}

	private static Lwjgl3Application createApplication(String[] args) {
		return new Lwjgl3Application(new GigaGalGame(args), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		configuration.setTitle("GigaGal");
		configuration.setWindowedMode(MathUtils.round(Constants.WINDOW_WIDTH), MathUtils.round(Constants.WINDOW_HEIGHT));
		configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return configuration;
	}
}
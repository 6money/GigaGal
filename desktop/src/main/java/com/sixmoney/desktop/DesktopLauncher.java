package com.sixmoney.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.sixmoney.GigaGalGame;

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
		configuration.width = 640;
		configuration.height = 480;
		//// This prevents a confusing error that would appear after exiting normally.
		configuration.forceExit = false;
		
		for (int size : new int[] { 128, 64, 32, 16 }) {
			configuration.addIcon("libgdx" + size + ".png", FileType.Internal);
		}
		return configuration;
	}
}
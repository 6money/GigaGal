package com.sixmoney.gigagal.gwt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sixmoney.gigagal.GigaGalGame;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
		@Override
		public GwtApplicationConfiguration getConfig () {
			Gdx.app.setLogLevel(Application.LOG_INFO);
			// Resizable application, uses available space in browser
			return new GwtApplicationConfiguration(true);
			// Fixed size application:
//			return new GwtApplicationConfiguration(480, 320);
		}

		@Override
		public ApplicationListener createApplicationListener () { 
			return new GigaGalGame(null);
		}
}

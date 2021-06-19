package com.sixmoney.gigagal.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.GwtGraphics;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sixmoney.gigagal.GigaGalGame;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
		@Override
		public GwtApplicationConfiguration getConfig () {
			// Resizable application, uses available space in browser
			boolean usePhysicalPixels = GwtApplication.isMobileDevice();
			GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(usePhysicalPixels);
			cfg.fullscreenOrientation = GwtGraphics.OrientationLockType.LANDSCAPE;
			return cfg;
		}

		@Override
		public ApplicationListener createApplicationListener () { 
			return new GigaGalGame(null);
		}

		@Override
		public Preloader.PreloaderCallback getPreloaderCallback() {
			return this.createPreloaderPanel(GWT.getHostPageBaseURL() + "gigagal_70_batmfo.png");
		}

		@Override
		protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
            meterPanel.setStyleName("gdx-meter");
            meterPanel.addStyleName("nostripes");
            meterStyle.setProperty("backgroundColor", "#ffffff");
            meterStyle.setProperty("backgroundImage", "none");
            meterPanel.getElement().getStyle().setProperty("maxWidth", "473px");
            meterStyle.setProperty("maxWidth", "473px");
		}

		@Override
        protected Preloader.PreloaderCallback createPreloaderPanel(String logoUrl) {
            final Panel preloaderPanel = new VerticalPanel();
            preloaderPanel.setStyleName("gdx-preloader");
            final Image logo = new Image(logoUrl);
            logo.setStyleName("logo");
            preloaderPanel.add(logo);
            final Panel meterPanel = new SimplePanel();
            final InlineHTML meter = new InlineHTML();
            final Style meterStyle = meter.getElement().getStyle();
            meterStyle.setWidth(0, Style.Unit.PCT);
            this.adjustMeterPanel(meterPanel, meterStyle);
            meterPanel.add(meter);
            preloaderPanel.add(meterPanel);
            getRootPanel().add(preloaderPanel);
            return new Preloader.PreloaderCallback() {

                @Override
                public void error (String file) {
                    System.out.println("error: " + file);
                }

                @Override
                public void update (Preloader.PreloaderState state) {
                    meterStyle.setWidth(100f * state.getProgress(), Style.Unit.PCT);
                }

            };
        }
}

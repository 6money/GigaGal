package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.PreferenceManager;

public class GigaGalHUD {
    public Viewport viewport;

    private BitmapFont font;
    private boolean showFPS;

    public GigaGalHUD() {
        viewport = new ExtendViewport(Constants.HUD_VIEWPORT_SIZE, Constants.HUD_VIEWPORT_SIZE);
        font = new BitmapFont();
        showFPS = PreferenceManager.get_instance().getShowFPS();
    }

    public void render(SpriteBatch spriteBatch, int lives, int ammo_basic, int ammo_big, int ammo_rapid, int score) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        font.draw(spriteBatch, Constants.HUD_SCORE_LABEL + score, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - Constants.HUD_MARGIN);
        font.draw(spriteBatch, Constants.HUD_AMMO_LABEL + ammo_basic, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - (Constants.HUD_MARGIN * 2));
        font.draw(spriteBatch, Constants.HUD_AMMO_SPECIAL_LABEL + ammo_big, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - (Constants.HUD_MARGIN * 3));
        font.draw(spriteBatch, Constants.HUD_AMMO_RAPID_LABEL + ammo_rapid, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - (Constants.HUD_MARGIN * 4));
        if (showFPS) {
            font.draw(spriteBatch, String.valueOf(Gdx.graphics.getFramesPerSecond()), viewport.getCamera().viewportWidth - Constants.HUD_MARGIN * 2, viewport.getCamera().viewportHeight - Constants.GIGAGAL_HEIGHT - 14 - Constants.HUD_MARGIN);
        }

        float offset = Constants.HUD_MARGIN * 2;
        for (int i = 1; i <= lives; i++) {
            spriteBatch.draw(Assets.get_instance().gigaGalAssets.standing_right, viewport.getCamera().viewportWidth - Constants.HUD_MARGIN - offset, viewport.getCamera().viewportHeight - Constants.GIGAGAL_HEIGHT - 14 - Constants.HUD_MARGIN);
            offset += Constants.HUD_MARGIN * 2;
        }
        spriteBatch.end();

    }

    public void dispose() {
        font.dispose();
    }
}

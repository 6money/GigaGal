package com.udacity.game.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;

public class GigaGalHUD {
    public Viewport viewport;

    private BitmapFont font;

    public GigaGalHUD() {
        viewport = new ExtendViewport(Constants.HUD_VIEWPORT_SIZE, Constants.HUD_VIEWPORT_SIZE);
        font = new BitmapFont();
    }

    public void render(SpriteBatch spriteBatch, int lives, int ammo, int score) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        font.draw(spriteBatch, Constants.HUD_SCORE_LABEL + score, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - Constants.HUD_MARGIN);
        font.draw(spriteBatch, Constants.HUD_AMMO_LABEL + ammo, Constants.HUD_MARGIN, viewport.getCamera().viewportHeight - (Constants.HUD_MARGIN * 2));

        float offset = Constants.HUD_MARGIN * 2;
        for (int i = 1; i <= lives; i++) {
            spriteBatch.draw(Assets.instance.gigaGalAssets.standing_right, viewport.getCamera().viewportWidth - Constants.HUD_MARGIN - offset, viewport.getCamera().viewportHeight - Constants.GIGAGAL_HEIGHT - 14 - Constants.HUD_MARGIN);
            offset += Constants.HUD_MARGIN * 2;
        }
        spriteBatch.end();

    }
}

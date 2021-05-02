package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.utils.Constants;

public class GameOverOverlay {
    private final BitmapFont font;

    public final Viewport viewport;

    public GameOverOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(1);
    }

    public void render(SpriteBatch spriteBatch) {

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        font.draw(spriteBatch, Constants.GAME_OVER_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 2.5f, 0, Align.center, false);

        spriteBatch.end();

    }

    public void dispose() {
        font.dispose();
    }
}

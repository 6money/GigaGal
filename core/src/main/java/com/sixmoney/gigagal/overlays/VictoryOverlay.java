package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.entities.Explosion;
import com.sixmoney.gigagal.utils.Constants;

public class VictoryOverlay {
    public final static String TAG = VictoryOverlay.class.getName();

    private final BitmapFont font;
    private Array<Explosion> explosions;
    private boolean highScore;

    public final Viewport viewport;

    public VictoryOverlay() {
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(1);
    }

    public void init(boolean highScore) {
        this.highScore = highScore;
        explosions = new Array<>(Constants.EXPLOSION_COUNT);

        for (int i = 0; i < Constants.EXPLOSION_COUNT; i++) {
            Explosion explosion = new Explosion(new Vector2(MathUtils.random(0, viewport.getWorldWidth()), MathUtils.random(0, viewport.getWorldHeight())));
            explosion.offset = MathUtils.random(Constants.LEVEL_END_DURATION);
            explosions.add(explosion);
        }

    }

    public void render(SpriteBatch spriteBatch, int score) {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        for (Explosion explosion: explosions) {
            explosion.render(spriteBatch);
        }

        font.draw(spriteBatch, Constants.VICTORY_MESSAGE, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2f, 0, Align.center, false);
        font.draw(spriteBatch, Constants.VICTORY_SCORE + score, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 3f, 0, Align.center, false);
        if (highScore) {
            font.draw(spriteBatch, "HIGH SCORE!!", viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 6f, 0, Align.center, false);
        }

        spriteBatch.end();

    }

    public void dispose() {
        font.dispose();
    }
}
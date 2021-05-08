package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.entities.Explosion;
import com.sixmoney.gigagal.screens.GameplayScreen;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class VictoryOverlay extends InputAdapter {
    public final static String TAG = VictoryOverlay.class.getName();

    private final BitmapFont font;
    private Array<Explosion> explosions;
    private boolean highScore;
    private GameplayScreen gameplayScreen;
    private Rectangle continue_rect;
    private Rectangle quit_rect;

    public final Viewport viewport;

    public VictoryOverlay(GameplayScreen gameplayScreen) {
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.gameplayScreen = gameplayScreen;
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        continue_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        quit_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
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

    public void update_rect() {
        continue_rect.x = viewport.getWorldWidth() / 4;
        continue_rect.y = viewport.getWorldHeight() / 7;
        continue_rect.width = viewport.getWorldWidth() * 2 / 4;
        continue_rect.height = viewport.getWorldHeight() / 7;

        quit_rect.x = viewport.getWorldWidth() / 4;
        quit_rect.y = 0;
        quit_rect.width = viewport.getWorldWidth() * 2 / 4;
        quit_rect.height = viewport.getWorldHeight() / 7;
    }

    public void render(SpriteBatch spriteBatch, int score) {
        if (Utils.secondsSince(gameplayScreen.levelEndOverlayStartTime) > Constants.LEVEL_END_BLOCK) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                gameplayScreen.levelComplete();
            }
        }

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        if (gameplayScreen.level_num < Constants.MAX_LEVEL) {
            Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, continue_rect.x, continue_rect.y, continue_rect.width, continue_rect.height);
        }
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
        font.getData().setScale(0.5f);
        if (gameplayScreen.level_num < Constants.MAX_LEVEL) {
            font.draw(spriteBatch, Constants.NEXT_LEVEL_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 1.7f / 7, 0, Align.center, false);
        }
        font.draw(spriteBatch, Constants.QUIT_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 10, 0, Align.center, false);
        font.getData().setScale(0.8f);

        for (Explosion explosion: explosions) {
            explosion.render(spriteBatch);
        }

        font.draw(spriteBatch, Constants.VICTORY_MESSAGE, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * 5 / 6f, 0, Align.center, false);
        font.draw(spriteBatch, Constants.VICTORY_SCORE + score, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * 4 / 6f, 0, Align.center, false);
        if (highScore) {
            font.draw(spriteBatch, "HIGH SCORE!!", viewport.getWorldWidth() / 2, viewport.getWorldHeight() * 3 / 6f, 0, Align.center, false);
        }

        spriteBatch.end();

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (continue_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete();
        } else if (quit_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(true, false);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void dispose() {
        font.dispose();
    }
}

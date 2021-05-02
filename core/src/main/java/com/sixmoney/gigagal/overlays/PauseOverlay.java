package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.screens.GameplayScreen;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class PauseOverlay extends InputAdapter {
    public final static String TAG = PauseOverlay.class.getName();

    private GameplayScreen gameplayScreen;
    private final BitmapFont font;
    private Rectangle resume_rect;
    private Rectangle quit_rect;

    public final Viewport viewport;

    public PauseOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        resume_rect = new Rectangle(viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 7, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        quit_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
    }

    public void update_rect() {
        resume_rect.x = viewport.getWorldWidth() / 4;
        resume_rect.y = viewport.getWorldHeight() / 7;
        resume_rect.width = viewport.getWorldWidth() * 2 / 4;
        resume_rect.height = viewport.getWorldHeight() / 7;

        quit_rect.x = viewport.getWorldWidth() / 4;
        quit_rect.y = 0;
        quit_rect.width = viewport.getWorldWidth() * 2 / 4;
        quit_rect.height = viewport.getWorldHeight() / 7;
    }

    public void render(SpriteBatch spriteBatch) {

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, resume_rect.x, resume_rect.y, resume_rect.width, resume_rect.height);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);

        font.getData().setScale(1);
        font.draw(spriteBatch, Constants.PAUSED_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 2, 0, Align.center, false);
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, Constants.RESUME_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 1.7f / 7, 0, Align.center, false);
        font.draw(spriteBatch, Constants.QUIT_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 10, 0, Align.center, false);

        spriteBatch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (resume_rect.contains(viewportPosition)) {
            gameplayScreen.level.paused = false;
            if (gameplayScreen.onMobile() || gameplayScreen.debugMobile) {
                Gdx.input.setInputProcessor(gameplayScreen.onScreeenControls);
            } else {
                Gdx.input.setInputProcessor(null);
            }
        } else if (quit_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(true);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void dispose() {
        font.dispose();
    }
}

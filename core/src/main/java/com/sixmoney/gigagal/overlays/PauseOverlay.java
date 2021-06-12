package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private Rectangle restart_rect;
    private Rectangle text_rect;

    public final Viewport viewport;

    public PauseOverlay(GameplayScreen gameplayScreen) {
        this.gameplayScreen = gameplayScreen;
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        resume_rect = new Rectangle(viewport.getWorldWidth() / 4, viewport.getWorldHeight() / 7, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        restart_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        quit_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        text_rect = new Rectangle();

        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
    }

    public void update_rect() {
        resume_rect.x = viewport.getWorldWidth() / 4;
        resume_rect.y = viewport.getWorldHeight() * 2 / 7;
        resume_rect.width = viewport.getWorldWidth() * 2 / 4;
        resume_rect.height = viewport.getWorldHeight() / 7;

        restart_rect.x = viewport.getWorldWidth() / 4;
        restart_rect.y = viewport.getWorldHeight() / 7;
        restart_rect.width = viewport.getWorldWidth() * 2 / 4;
        restart_rect.height = viewport.getWorldHeight() / 7;

        quit_rect.x = viewport.getWorldWidth() / 4;
        quit_rect.y = 0;
        quit_rect.width = viewport.getWorldWidth() * 2 / 4;
        quit_rect.height = viewport.getWorldHeight() / 7;

        text_rect.x = viewport.getWorldWidth() / 8;
        text_rect.y = viewport.getWorldHeight() / 1.8f;
        text_rect.width = viewport.getWorldWidth() * 6 / 8;
        text_rect.height = viewport.getWorldHeight() / 4f;
    }

    public void render(SpriteBatch spriteBatch) {

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, restart_rect.x, restart_rect.y, restart_rect.width, restart_rect.height);
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, resume_rect.x, resume_rect.y, resume_rect.width, resume_rect.height);
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
        Assets.get_instance().platformAssets.ninePatch_platform_hard.draw(spriteBatch, text_rect.x, text_rect.y, text_rect.width, text_rect.height);

        font.getData().setScale(1);
        font.draw(spriteBatch, Constants.PAUSED_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 3 / 4, 0, Align.center, false);
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, Constants.RESUME_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 2.75f / 7, 0, Align.center, false);
        font.draw(spriteBatch, Constants.RESTART_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 1.7f / 7, 0, Align.center, false);
        font.draw(spriteBatch, Constants.QUIT_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 10, 0, Align.center, false);

        spriteBatch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (resume_rect.contains(viewportPosition)) {
            gameplayScreen.level.paused = false;
            Gdx.input.setInputProcessor(gameplayScreen.inputMultiplexer);
            return true;
        } else if (quit_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(true, false);
            return true;
        } else if (restart_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(false, true);
            return true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            gameplayScreen.level.paused = false;
            Gdx.input.setInputProcessor(gameplayScreen.inputMultiplexer);
            return true;
        }
        return false;
    }

    public void dispose() {
        font.dispose();
    }
}

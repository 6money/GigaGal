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
import com.sixmoney.gigagal.utils.Utils;

public class GameOverOverlay extends InputAdapter {
    private final BitmapFont font;
    private GameplayScreen gameplayScreen;
    private Rectangle restart_rect;
    private Rectangle quit_rect;
    private Rectangle text_rect;

    public final Viewport viewport;

    public GameOverOverlay(GameplayScreen gameplayScreen) {
        this.viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        this.gameplayScreen = gameplayScreen;
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        restart_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        quit_rect = new Rectangle(viewport.getWorldWidth() / 4, 0, viewport.getWorldWidth() * 3 / 4, viewport.getWorldHeight() / 7);
        text_rect = new Rectangle();
    }

    public void update_rect() {
        restart_rect.x = viewport.getWorldWidth() / 4;
        restart_rect.y = viewport.getWorldHeight() / 7;
        restart_rect.width = viewport.getWorldWidth() * 2 / 4;
        restart_rect.height = viewport.getWorldHeight() / 7;

        quit_rect.x = viewport.getWorldWidth() / 4;
        quit_rect.y = 0;
        quit_rect.width = viewport.getWorldWidth() * 2 / 4;
        quit_rect.height = viewport.getWorldHeight() / 7;

        text_rect.x = viewport.getWorldWidth() / 20;
        text_rect.y = viewport.getWorldHeight() / 2.1f;
        text_rect.width = viewport.getWorldWidth() * 18 / 20;
        text_rect.height = viewport.getWorldHeight() / 4f;
    }

    public void render(SpriteBatch spriteBatch) {
        if (Utils.secondsSince(gameplayScreen.levelEndOverlayStartTime) > Constants.LEVEL_END_BLOCK) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                gameplayScreen.levelComplete(false, true);
            }
        }

        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, restart_rect.x, restart_rect.y, restart_rect.width, restart_rect.height);
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, quit_rect.x, quit_rect.y, quit_rect.width, quit_rect.height);
        font.getData().setScale(0.5f);
        font.draw(spriteBatch, Constants.RESTART_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 1.7f / 7, 0, Align.center, false);
        font.draw(spriteBatch, Constants.QUIT_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight / 10, 0, Align.center, false);
        font.getData().setScale(0.8f);

        Assets.get_instance().platformAssets.ninePatch_platform_hard.draw(spriteBatch, text_rect.x, text_rect.y, text_rect.width, text_rect.height);
        font.draw(spriteBatch, Constants.GAME_OVER_MESSAGE, viewport.getCamera().viewportWidth / 2, viewport.getCamera().viewportHeight * 4 / 6f, 0, Align.center, false);

        spriteBatch.end();

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (restart_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(false, true);
            return true;
        } else if (quit_rect.contains(viewportPosition)) {
            gameplayScreen.levelComplete(true, false);
            return true;
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    public void dispose() {
        font.dispose();
    }
}

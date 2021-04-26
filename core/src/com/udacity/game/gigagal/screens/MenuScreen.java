package com.udacity.game.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.game.gigagal.GigaGalGame;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;

public class MenuScreen extends InputAdapter implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private BitmapFont bitmapFont;
    private Rectangle play_button;
    private Rectangle level_select_button;
    private Rectangle highScoreButton;
    private Rectangle optionsButton;
    private GigaGalGame gigaGalGame;

    public MenuScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
        extendViewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        bitmapFont = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        bitmapFont.getData().setScale(0.5f);
        play_button = new Rectangle();
        level_select_button = new Rectangle();
        highScoreButton = new Rectangle();
        optionsButton = new Rectangle();
    }


    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        play_button.x = extendViewport.getWorldWidth() / 8;
        play_button.y = extendViewport.getWorldHeight() * 3.6f / 8;
        play_button.width = extendViewport.getWorldWidth() * 6 / 8;
        play_button.height = extendViewport.getWorldHeight() / 6;

        level_select_button.x = extendViewport.getWorldWidth() / 8;
        level_select_button.y = extendViewport.getWorldHeight() * 2.3f / 8;
        level_select_button.width = extendViewport.getWorldWidth() * 6 / 8;
        level_select_button.height = extendViewport.getWorldHeight() / 6;

        highScoreButton.x = extendViewport.getWorldWidth() / 8;
        highScoreButton.y = extendViewport.getWorldHeight() / 8;
        highScoreButton.width = extendViewport.getWorldWidth() * 3 / 8;
        highScoreButton.height = extendViewport.getWorldHeight() / 6;

        optionsButton.x = extendViewport.getWorldWidth() / 2;
        optionsButton.y = extendViewport.getWorldHeight() / 8;
        optionsButton.width = extendViewport.getWorldWidth() * 3 / 8;
        optionsButton.height = extendViewport.getWorldHeight() / 6;
    }


    @Override
    public void render(float delta) {
        extendViewport.apply(true);

        Gdx.gl.glClearColor(
                Constants.BG_COLOR.r,
                Constants.BG_COLOR.g,
                Constants.BG_COLOR.b,
                Constants.BG_COLOR.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        spriteBatch.begin();
        bitmapFont.draw(spriteBatch, "WELCOME TO GIGAGAL", extendViewport.getWorldWidth() / 2, extendViewport.getWorldHeight() / 1.2f, 0, Align.center, false);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, play_button.x, play_button.y, play_button.width, play_button.height);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, level_select_button.x, level_select_button.y, level_select_button.width, level_select_button.height);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, highScoreButton.x, highScoreButton.y, highScoreButton.width, highScoreButton.height);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, optionsButton.x, optionsButton.y, optionsButton.width, optionsButton.height);
        bitmapFont.draw(spriteBatch, "PLAY", extendViewport.getWorldWidth() / 2, play_button.y + bitmapFont.getData().lineHeight, 0, Align.center, false);
        bitmapFont.draw(spriteBatch, "LEVEL SELECT", extendViewport.getWorldWidth() / 2, level_select_button.y + bitmapFont.getData().lineHeight, 0, Align.center, false);
        bitmapFont.draw(spriteBatch, "HIGH SCORES", extendViewport.getWorldWidth() / 4, highScoreButton.y + bitmapFont.getData().lineHeight, 0, Align.center, false);
        bitmapFont.draw(spriteBatch, "OPTIONS", extendViewport.getWorldWidth() * 3 / 4, optionsButton.y + bitmapFont.getData().lineHeight, 0, Align.center, false);

        spriteBatch.end();


    }


    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log(TAG, "clicked");
        Vector2 viewportPosition = extendViewport.unproject(new Vector2(screenX, screenY));

        if (play_button.contains(viewportPosition)) {
            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
        } else if (level_select_button.contains(viewportPosition)) {
            gigaGalGame.switchScreen("level select");
        } else if (highScoreButton.contains(viewportPosition)) {
            gigaGalGame.switchScreen("high_score");
        } else if (optionsButton.contains(viewportPosition)) {
            gigaGalGame.switchScreen("options");
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }
}

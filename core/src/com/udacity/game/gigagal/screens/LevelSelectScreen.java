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

public class LevelSelectScreen extends InputAdapter implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private BitmapFont bitmapFont;
    private Rectangle level1_button;
    private Rectangle level2_button;
    private GigaGalGame gigaGalGame;

    public LevelSelectScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        spriteBatch = new SpriteBatch();
        extendViewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        bitmapFont = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        bitmapFont.getData().setScale(0.8f);
        level1_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() * 3 / 8, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 4);
        level2_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() / 8, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 4);
    }


    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        level1_button.x = extendViewport.getWorldWidth() / 8;
        level1_button.y = extendViewport.getWorldHeight() * 3 / 8;
        level1_button.width = extendViewport.getWorldWidth() * 6 / 8;
        level1_button.height = extendViewport.getWorldHeight() / 4;

        level2_button.x = extendViewport.getWorldWidth() / 8;
        level2_button.y = extendViewport.getWorldHeight() / 8;
        level2_button.width = extendViewport.getWorldWidth() * 6 / 8;
        level2_button.height = extendViewport.getWorldHeight() / 4;
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
        bitmapFont.draw(spriteBatch, "SELECT A LEVEL", extendViewport.getWorldWidth() / 2, extendViewport.getWorldHeight() / 1.2f, 0, Align.center, false);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, level1_button.x, level1_button.y, level1_button.width, level1_button.height);
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, level2_button.x, level2_button.y, level2_button.width, level2_button.height);
        bitmapFont.draw(spriteBatch, "LEVEL 1", extendViewport.getWorldWidth() / 2, extendViewport.getWorldHeight() / 1.85f, 0, Align.center, false);
        bitmapFont.draw(spriteBatch, "LEVEL 2", extendViewport.getWorldWidth() / 2, extendViewport.getWorldHeight() / 3.5f, 0, Align.center, false);

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
        Vector2 viewportPosition = extendViewport.unproject(new Vector2(screenX, screenY));

        if (level1_button.contains(viewportPosition)) {
            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
        } else if (level2_button.contains(viewportPosition)) {
            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_2);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }
}

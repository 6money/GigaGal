package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class LevelSelectScreen extends InputAdapter implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private BitmapFont bitmapFont;
    private Rectangle level1_button;
    private Rectangle level2_button;
    private Rectangle level3_button;
    private Rectangle level4_button;
    private Rectangle back_button;
    private GigaGalGame gigaGalGame;
    private Stage stage;
    private Skin skin;

    public LevelSelectScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
//        Gdx.input.setInputProcessor(this);
//        spriteBatch = new SpriteBatch();
//        extendViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
//        bitmapFont = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
//        level1_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() * 4 / 8, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 6);
//        level2_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() / 8, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 6);
//        level3_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() / 12, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 6);
//        level4_button = new Rectangle(extendViewport.getWorldWidth() / 8, extendViewport.getWorldHeight() / 12, extendViewport.getWorldWidth() * 6 / 8, extendViewport.getWorldHeight() / 6);
//        back_button = new Rectangle(0, extendViewport.getWorldHeight() / 12 * 10, extendViewport.getWorldWidth() / 10, extendViewport.getWorldHeight() / 12);

        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH / 2, Constants.WINDOW_HEIGHT / 2));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH2));

        Table tableMenu = new Table(skin);
        tableMenu.setFillParent(true);
        tableMenu.pad(5);
        tableMenu.defaults().grow().space(5).padLeft(50).padRight(50);

        tableMenu.row();
        Label labelWelcome = new Label("SELECT A LEVEL", skin);
        labelWelcome.setAlignment(Align.center);
        tableMenu.add(labelWelcome).height(stage.getHeight() / 6);

        tableMenu.row().uniform().fill();

        tableMenu.row();
        TextButton buttonLvl1 = new TextButton("LEVEL 1", skin, "gigagal");
        buttonLvl1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
                dispose();
            }
        });
        tableMenu.add(buttonLvl1).height(stage.getHeight() / 6);

        tableMenu.row();
        TextButton buttonLvl2 = new TextButton("LEVEL 2", skin, "gigagal");
        buttonLvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_2);
                dispose();
            }
        });
        tableMenu.add(buttonLvl2).height(stage.getHeight() / 6);

        tableMenu.row();
        TextButton buttonLvl3 = new TextButton("LEVEL 3", skin, "gigagal");
        buttonLvl3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_3);
                dispose();
            }
        });
        tableMenu.add(buttonLvl3).height(stage.getHeight() / 6);

        tableMenu.row();
        TextButton buttonLvl4 = new TextButton("LEVEL 4", skin, "gigagal");
        buttonLvl4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_4);
                dispose();
            }
        });
        tableMenu.add(buttonLvl4).height(stage.getHeight() / 6);

        stage.addActor(tableMenu);

        TextButton buttonBack = new TextButton("Back", skin, "gigagal");
        buttonBack.setWidth(stage.getWidth() / 8);
        buttonBack.setHeight(stage.getHeight() / 10);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("menu");
                dispose();
            }
        });

        stage.addActor(buttonBack);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void resize(int width, int height) {
//        extendViewport.update(width, height, true);
//        level1_button.x = extendViewport.getWorldWidth() / 8;
//        level1_button.y = extendViewport.getWorldHeight() * 3 / 6;
//        level1_button.width = extendViewport.getWorldWidth() * 6 / 8;
//        level1_button.height = extendViewport.getWorldHeight() / 6;
//
//        level2_button.x = extendViewport.getWorldWidth() / 8;
//        level2_button.y = extendViewport.getWorldHeight() * 2 / 6;
//        level2_button.width = extendViewport.getWorldWidth() * 6 / 8;
//        level2_button.height = extendViewport.getWorldHeight() / 6;
//
//        level3_button.x = extendViewport.getWorldWidth() / 8;
//        level3_button.y = extendViewport.getWorldHeight() / 6;
//        level3_button.width = extendViewport.getWorldWidth() * 6 / 8;
//        level3_button.height = extendViewport.getWorldHeight() / 6;
//
//        level4_button.x = extendViewport.getWorldWidth() / 8;
//        level4_button.y = extendViewport.getWorldHeight() / 16;
//        level4_button.width = extendViewport.getWorldWidth() * 6 / 8;
//        level4_button.height = extendViewport.getWorldHeight() / 10;
//
//        back_button.y = extendViewport.getWorldHeight() / 10 * 9;
//        back_button.width = extendViewport.getWorldWidth() / 4;
//        back_button.height = extendViewport.getWorldHeight() / 10;

        stage.getViewport().update(width, height, true);
    }


    @Override
    public void render(float delta) {
//        extendViewport.apply(true);

        Gdx.gl.glClearColor(
                Constants.BG_COLOR.r,
                Constants.BG_COLOR.g,
                Constants.BG_COLOR.b,
                Constants.BG_COLOR.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
//        spriteBatch.begin();
//        bitmapFont.getData().setScale(0.5f);
//        bitmapFont.draw(spriteBatch, "SELECT A LEVEL", extendViewport.getWorldWidth() / 2, extendViewport.getWorldHeight() / 1.2f, 0, Align.center, false);
//        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, level1_button.x, level1_button.y, level1_button.width, level1_button.height);
//        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, level2_button.x, level2_button.y, level2_button.width, level2_button.height);
//        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, level3_button.x, level3_button.y, level3_button.width, level3_button.height);
//        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, level4_button.x, level4_button.y, level4_button.width, level4_button.height);
//        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, back_button.x, back_button.y, back_button.width, back_button.height);
//        bitmapFont.draw(spriteBatch, "LEVEL 1", level1_button.x + level1_button.width / 2, level1_button.y + level1_button.height / 1.3f, 0, Align.center, false);
//        bitmapFont.draw(spriteBatch, "LEVEL 2", level2_button.x + level2_button.width / 2, level2_button.y + level2_button.height / 1.3f, 0, Align.center, false);
//        bitmapFont.draw(spriteBatch, "LEVEL 3", level3_button.x + level3_button.width / 2, level3_button.y + level3_button.height / 1.3f, 0, Align.center, false);
//        bitmapFont.draw(spriteBatch, "LEVEL 4", level4_button.x + level4_button.width / 2, level4_button.y + level4_button.height / 1.3f, 0, Align.center, false);
//        bitmapFont.getData().setScale(0.3f);
//        bitmapFont.draw(spriteBatch, "BACK", back_button.x + back_button.width / 2, back_button.y + back_button.height / 1.5f, 0, Align.center, false);
//        spriteBatch.end();

        stage.act(delta);
        stage.draw();
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
//        spriteBatch.dispose();
        stage.dispose();
    }

//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        Vector2 viewportPosition = extendViewport.unproject(new Vector2(screenX, screenY));
//
//        if (level1_button.contains(viewportPosition)) {
//            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
//        } else if (level2_button.contains(viewportPosition)) {
//            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_2);
//        } else if (level3_button.contains(viewportPosition)) {
//            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_3);
//        } else if (level4_button.contains(viewportPosition)) {
//            gigaGalGame.switchScreen("gameplay", Constants.LEVEL_4);
//        } else if(back_button.contains(viewportPosition)) {
//            gigaGalGame.switchScreen("menu");
//            dispose();
//        }
//        return super.touchDown(screenX, screenY, pointer, button);
//    }
}

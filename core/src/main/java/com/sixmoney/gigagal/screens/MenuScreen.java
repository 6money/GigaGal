package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class MenuScreen implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private GigaGalGame gigaGalGame;
    private Stage stage;
    private Skin skin;
    private Table tableMenu;

    public MenuScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = Assets.get_instance().skinAssets.skin;

        tableMenu = new Table(skin);
        tableMenu.setFillParent(true);
        tableMenu.pad(5);
        tableMenu.defaults().space(5).padLeft(50).padRight(50).padTop(30);

        tableMenu.row();
        TextureRegion logoTexture = Assets.get_instance().logoAssets.logo_140;
        Image logo = new Image(logoTexture);
        logo.setAlign(Align.center);
        tableMenu.add(logo).height(114f);
        tableMenu.defaults().space(5).padLeft(50).padRight(50).padTop(30).grow();

        tableMenu.row().uniform().fill();

        tableMenu.row();
        TextButton buttonPlay = new TextButton("PLAY", skin, "gigagal");
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
                dispose();
            }
        });
        tableMenu.add(buttonPlay).minHeight(100f);

        tableMenu.row();
        TextButton levelSelectPlay = new TextButton("LEVEL SELECT", skin, "gigagal");
        levelSelectPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("level select");
                dispose();
            }
        });
        tableMenu.add(levelSelectPlay).minHeight(100f);

        tableMenu.row();
        Table tableSubmenu = new Table(skin);
        tableSubmenu.defaults().grow();
        tableSubmenu.row();
        TextButton buttonHighScores = new TextButton("HIGH SCORES", skin, "gigagal");
        buttonHighScores.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("high_score");
                dispose();
            }
        });
        tableSubmenu.add(buttonHighScores).uniform().spaceRight(5).minHeight(100f);
        TextButton buttonOptions = new TextButton("OPTIONS", skin, "gigagal");
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("options");
                dispose();
            }
        });
        tableSubmenu.add(buttonOptions).uniform().spaceLeft(5).minHeight(100f);
        tableMenu.add(tableSubmenu).minHeight(100f);

        TextureRegion gigagalTexture = Assets.get_instance().gigaGalAssets.jumping_right;
        Image gigagalImage = new Image(gigagalTexture);
        gigagalImage.setPosition(-500, -400);
        gigagalImage.scaleBy(40f);

        stage.addActor(gigagalImage);
        stage.addActor(tableMenu);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BG_COLOR.r,
                Constants.BG_COLOR.g,
                Constants.BG_COLOR.b,
                Constants.BG_COLOR.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        stage.dispose();
    }
}

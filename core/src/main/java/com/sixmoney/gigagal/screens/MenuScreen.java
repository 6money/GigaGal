package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

public class MenuScreen implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private GigaGalGame gigaGalGame;
    private Stage stage;
    private Skin skin;

    public MenuScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WINDOW_HEIGHT));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH2));

        Table tableMenu = new Table(skin);
        tableMenu.setFillParent(true);
        tableMenu.pad(5);
        tableMenu.defaults().grow().space(5).padLeft(50).padRight(50);

        tableMenu.row();
        Label labelWelcome = new Label("WELCOME TO GIGAGAL", skin);
        labelWelcome.setAlignment(Align.center);
        tableMenu.add(labelWelcome).height(stage.getHeight() / 5);

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
        tableMenu.add(buttonPlay).height(stage.getHeight() / 5);

        tableMenu.row();
        TextButton levelSelectPlay = new TextButton("LEVEL SELECT", skin, "gigagal");
        levelSelectPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("level select");
                dispose();
            }
        });
        tableMenu.add(levelSelectPlay).height(stage.getHeight() / 5);

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
        tableSubmenu.add(buttonHighScores).uniform().spaceRight(5);
        TextButton buttonOptions = new TextButton("OPTIONS", skin, "gigagal");
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("options");
                dispose();
            }
        });
        tableSubmenu.add(buttonOptions).uniform().spaceLeft(5);
        tableMenu.add(tableSubmenu).height(stage.getHeight() / 5);

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

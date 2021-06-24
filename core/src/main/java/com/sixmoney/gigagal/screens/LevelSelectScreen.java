package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class LevelSelectScreen extends InputAdapter implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private GigaGalGame gigaGalGame;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;

    public LevelSelectScreen(GigaGalGame game) {
        gigaGalGame = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = Assets.get_instance().skinAssets.skin;

        Table tableMenu = new Table(skin);
        tableMenu.setFillParent(true);
        tableMenu.pad(5);
        tableMenu.defaults().grow().space(5).padLeft(50).padRight(50);

        tableMenu.row();
        Label labelWelcome = new Label("SELECT A LEVEL", skin, "primary");
        labelWelcome.setAlignment(Align.center);
        tableMenu.add(labelWelcome).padTop(30).padBottom(30);

        Table tableLevels = new Table(skin);
        tableLevels.pad(5);
        tableLevels.defaults().space(5).growX().prefHeight(140f).maxHeight(200f);

        tableLevels.row();
        TextButton buttonLvl1 = new TextButton("LEVEL 1", skin, "gigagal");
        buttonLvl1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_1);
                dispose();
            }
        });
        tableLevels.add(buttonLvl1);

        tableLevels.row();
        TextButton buttonLvl2 = new TextButton("LEVEL 2", skin, "gigagal");
        buttonLvl2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_2);
                dispose();
            }
        });
        tableLevels.add(buttonLvl2);

        tableLevels.row();
        TextButton buttonLvl3 = new TextButton("LEVEL 3", skin, "gigagal");
        buttonLvl3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_3);
                dispose();
            }
        });
        tableLevels.add(buttonLvl3);

        tableLevels.row();
        TextButton buttonLvl4 = new TextButton("LEVEL 4", skin, "gigagal");
        buttonLvl4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_4);
                dispose();
            }
        });
        tableLevels.add(buttonLvl4);

        tableLevels.row();
        TextButton buttonLvl5 = new TextButton("LEVEL 5", skin, "gigagal");
        buttonLvl5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_5);
                dispose();
            }
        });
        tableLevels.add(buttonLvl5);

        tableLevels.row();
        TextButton buttonLvl6 = new TextButton("LEVEL 6", skin, "gigagal");
        buttonLvl6.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_6);
                dispose();
            }
        });
        tableLevels.add(buttonLvl6);

        tableLevels.row();
        TextButton buttonLvl7 = new TextButton("LEVEL 7", skin, "gigagal");
        buttonLvl7.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_7);
                dispose();
            }
        });
        tableLevels.add(buttonLvl7);

        tableLevels.row();
        TextButton buttonLvl8 = new TextButton("LEVEL 8", skin, "gigagal");
        buttonLvl8.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_8);
                dispose();
            }
        });
        tableLevels.add(buttonLvl8);

        tableLevels.row();
        TextButton buttonLvl9 = new TextButton("LEVEL 9", skin, "gigagal");
        buttonLvl9.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_9);
                dispose();
            }
        });
        tableLevels.add(buttonLvl9);

        tableLevels.row();
        TextButton buttonLvl10 = new TextButton("LEVEL 10", skin, "gigagal");
        buttonLvl10.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gigaGalGame.switchScreen("gameplay", Constants.LEVEL_10);
                dispose();
            }
        });
        tableLevels.add(buttonLvl10);

        ScrollPane scrollPane = new ScrollPane(tableLevels, skin);
        scrollPane.setFadeScrollBars(false);
        if (gigaGalGame.mobileControls) {
            scrollPane.setFlickScroll(true);
        } else {
            scrollPane.setFlickScroll(false);
        }

        scrollPane.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                stage.setScrollFocus(scrollPane);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
               stage.setScrollFocus(null);
            }
        });

        tableMenu.row();
        tableMenu.add(scrollPane);

        stage.addActor(tableMenu);

        buttonBack = new Button(skin, "gigagal");
        buttonBack.add(new Label("Back" ,skin));
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
        stage.getViewport().update(width, height, true);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
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

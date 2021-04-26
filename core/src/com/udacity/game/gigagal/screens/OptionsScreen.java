package com.udacity.game.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.udacity.game.gigagal.GigaGalGame;
import com.udacity.game.gigagal.utils.Constants;

public class OptionsScreen implements Screen {
    public static final String TAG = OptionsScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;
    private Table tableOptions;
    private Window window;

    public OptionsScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        buttonBack = new Button(skin);
        buttonBack.add(new Label("Back" ,skin));
        buttonBack.setWidth(stage.getWidth() / 8);
        buttonBack.setHeight(stage.getHeight() / 10);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchScreen("menu");
            }
        });

        tableOptions = new Table(skin);
        tableOptions.pad(5);
        tableOptions.setSize(stage.getWidth()/ 2, stage.getHeight());

        tableOptions.row();
        tableOptions.add(new Label("Level 1", skin));

        tableOptions.row();
        tableOptions.add(new Label("Level 1", skin));

        window = new Window("Options", skin);
        window.add(tableOptions);

        stage.addActor(window);
        stage.addActor(buttonBack);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

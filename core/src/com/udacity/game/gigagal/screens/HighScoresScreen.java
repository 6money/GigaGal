package com.udacity.game.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.udacity.game.gigagal.GigaGalGame;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.PreferenceManager;

public class HighScoresScreen implements Screen {
    public static final String TAG = MenuScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private List<Integer> listLevel1;
    private List<Integer> listLevel2;
    private List<Integer> listLevel3;
    private Label labelLevel1;
    private Label labelLevel2;
    private Label labelLevel3;
    private Label labelHighScore;
    private Button buttonBack;
    private SpriteBatch spriteBatch;

    public HighScoresScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        spriteBatch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("images/uiskin.json"));
        PreferenceManager preferenceManager = PreferenceManager.get_instance();
        Array<Integer> scoresLevel1 = preferenceManager.getScores("Level1");
        Array<Integer> scoresLevel2 = preferenceManager.getScores("Level2");
        Array<Integer> scoresLevel3 = preferenceManager.getScores("Level3");
        listLevel1 = new List<>(skin);
        listLevel1.setItems(scoresLevel1);
        listLevel1.setWidth(stage.getWidth() / 3);
        listLevel1.setHeight(200);
        listLevel1.setPosition(0, 0);

        listLevel2 = new List<>(skin);
        listLevel2.setItems(scoresLevel2);
        listLevel2.setWidth(stage.getWidth() / 3);
        listLevel2.setHeight(200);
        listLevel2.setPosition(stage.getWidth() / 3, 0);

        listLevel3 = new List<>(skin);
        listLevel3.setItems(scoresLevel3);
        listLevel3.setWidth(stage.getWidth() / 3);
        listLevel3.setHeight(200);
        listLevel3.setPosition(stage.getWidth() * 2 / 3, 0);

        labelLevel1 = new Label("Level 1", skin);
        labelLevel1.setPosition(0, 200);
        labelLevel2 = new Label("Level 2", skin);
        labelLevel2.setPosition(stage.getWidth() / 3, 200);
        labelLevel3 = new Label("Level 3", skin);
        labelLevel3.setPosition(stage.getWidth() * 2 / 3, 200);
        labelHighScore = new Label("HIGH SCORES", skin);
        labelHighScore.setPosition(stage.getWidth() / 2 - labelHighScore.getWidth() / 2, stage.getHeight() * 3 / 4);
        labelHighScore.setFontScale(2f);
        labelHighScore.setAlignment(Align.center);

        buttonBack = new Button(skin);
        buttonBack.add(new Label("Back" ,skin));
        buttonBack.setWidth(100);
        buttonBack.setHeight(40);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchScreen("menu");
            }
        });

        stage.addActor(listLevel1);
        stage.addActor(listLevel2);
        stage.addActor(listLevel3);
        stage.addActor(labelHighScore);
        stage.addActor(labelLevel1);
        stage.addActor(labelLevel2);
        stage.addActor(labelLevel3);
        stage.addActor(buttonBack);

        Gdx.input.setInputProcessor(stage);
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

        spriteBatch.begin();
        stage.draw();
        spriteBatch.end();
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
        spriteBatch.dispose();
    }
}

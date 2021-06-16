package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.PreferenceManager;

public class HighScoresScreen implements Screen {
    public static final String TAG = HighScoresScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private List<Integer> listLevel1;
    private List<Integer> listLevel2;
    private List<Integer> listLevel3;
    private List<Integer> listLevel4;
    private List<Integer> listLevel5;
    private List<Integer> listLevel6;
    private List<Integer> listLevel7;
    private List<Integer> listLevel8;
    private List<Integer> listLevel9;
    private List<Integer> listLevel10;
    private Label labelHighScore;
    private Button buttonBack;
    private Table tableHighscores;

    public HighScoresScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
        PreferenceManager preferenceManager = PreferenceManager.get_instance();
        Array<Integer> scoresLevel1 = preferenceManager.getScores("Level1");
        Array<Integer> scoresLevel2 = preferenceManager.getScores("Level2");
        Array<Integer> scoresLevel3 = preferenceManager.getScores("Level3");
        Array<Integer> scoresLevel4 = preferenceManager.getScores("Level4");
        Array<Integer> scoresLevel5 = preferenceManager.getScores("Level5");
        Array<Integer> scoresLevel6 = preferenceManager.getScores("Level6");
        Array<Integer> scoresLevel7 = preferenceManager.getScores("Level7");
        Array<Integer> scoresLevel8 = preferenceManager.getScores("Level8");
        Array<Integer> scoresLevel9 = preferenceManager.getScores("Level9");
        Array<Integer> scoresLevel10 = preferenceManager.getScores("Level10");

        listLevel1 = new List<>(skin, "gigagal");
        listLevel1.setItems(scoresLevel1);
        listLevel2 = new List<>(skin, "gigagal");
        listLevel2.setItems(scoresLevel2);
        listLevel3 = new List<>(skin, "gigagal");
        listLevel3.setItems(scoresLevel3);
        listLevel4 = new List<>(skin, "gigagal");
        listLevel4.setItems(scoresLevel4);
        listLevel5 = new List<>(skin, "gigagal");
        listLevel5.setItems(scoresLevel5);
        listLevel6 = new List<>(skin, "gigagal");
        listLevel6.setItems(scoresLevel6);
        listLevel7 = new List<>(skin, "gigagal");
        listLevel7.setItems(scoresLevel7);
        listLevel8 = new List<>(skin, "gigagal");
        listLevel8.setItems(scoresLevel8);
        listLevel9 = new List<>(skin, "gigagal");
        listLevel9.setItems(scoresLevel9);
        listLevel10 = new List<>(skin, "gigagal");
        listLevel10.setItems(scoresLevel10);

        Table tableScene = new Table(skin);
        tableScene.setFillParent(true);
        tableScene.pad(5);
        tableScene.defaults().grow();

        labelHighScore = new Label("HIGH SCORES", skin, "primary");
        labelHighScore.setPosition(stage.getWidth() / 2 - labelHighScore.getWidth() / 2, stage.getHeight() * 3 / 4);
        labelHighScore.setAlignment(Align.center);
        tableScene.add(labelHighScore).padTop(30).padBottom(30);

        buttonBack = new Button(skin, "gigagal");
        buttonBack.add(new Label("Back" ,skin));
        buttonBack.setWidth(stage.getWidth() / 8);
        buttonBack.setHeight(stage.getHeight() / 10);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.switchScreen("menu");
                dispose();
            }
        });

        tableHighscores = new Table(skin);
        tableHighscores.pad(5);
        tableHighscores.setPosition(0, 0);
        tableHighscores.defaults().grow();
        tableHighscores.setSize(stage.getWidth(), stage.getHeight() / 2);

        for (int i = 1; i <= 5; i++) {
            Label tempLabel = new Label("Level " + i, skin);
            tableHighscores.add(tempLabel);
        }


        tableHighscores.row();
        tableHighscores.add(listLevel1);
        tableHighscores.add(listLevel2);
        tableHighscores.add(listLevel3);
        tableHighscores.add(listLevel4);
        tableHighscores.add(listLevel5);

        tableHighscores.row();
        for (int i = 6; i <= Constants.MAX_LEVEL; i++) {
            Label tempLabel = new Label("Level " + i, skin);
            tableHighscores.add(tempLabel);
        }

        tableHighscores.row();
        tableHighscores.add(listLevel6);
        tableHighscores.add(listLevel7);
        tableHighscores.add(listLevel8);
        tableHighscores.add(listLevel9);
        tableHighscores.add(listLevel10);

        ScrollPane scrollPane = new ScrollPane(tableHighscores, skin);
        scrollPane.setFadeScrollBars(false);
        if (game.onMobile() || game.debugMobile) {
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

        tableScene.row();
        tableScene.add(scrollPane);

        stage.addActor(tableScene);
        stage.addActor(buttonBack);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        buttonBack.setPosition(0, stage.getHeight() - buttonBack.getHeight());
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
        skin.dispose();
        stage.dispose();
    }
}

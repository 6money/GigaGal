package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
    private Label labelHighScore;
    private Button buttonBack;
    private Table table;

    public HighScoresScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH2));
//        skin.getFont("font").getData().setScale(2);
//        skin.getFont("list").getData().setScale(2);
//        skin.getFont("subtitle").getData().setScale(2);
//        skin.getFont("window").getData().setScale(2);
        PreferenceManager preferenceManager = PreferenceManager.get_instance();
        Array<Integer> scoresLevel1 = preferenceManager.getScores("Level1");
        Array<Integer> scoresLevel2 = preferenceManager.getScores("Level2");
        Array<Integer> scoresLevel3 = preferenceManager.getScores("Level3");

        listLevel1 = new List<>(skin, "gigagal");
        listLevel1.setItems(scoresLevel1);
        listLevel2 = new List<>(skin, "gigagal");
        listLevel2.setItems(scoresLevel2);
        listLevel3 = new List<>(skin, "gigagal");
        listLevel3.setItems(scoresLevel3);

        labelHighScore = new Label("HIGH SCORES", skin);
        labelHighScore.setPosition(stage.getWidth() / 2 - labelHighScore.getWidth() / 2, stage.getHeight() * 3 / 4);
        labelHighScore.setFontScale(3f);
        labelHighScore.setAlignment(Align.center);

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

        table = new Table(skin);
        table.pad(5);
        table.setPosition(0, 0);
        table.defaults().grow();
        table.setSize(stage.getWidth(), stage.getHeight() / 2);

        table.add(new Label("Level 1", skin));
        table.add(new Label("Level 2", skin));
        table.add(new Label("Level 3", skin));

        table.row();
        table.add(listLevel1);
        table.add(listLevel2);
        table.add(listLevel3);

        stage.addActor(table);
        stage.addActor(labelHighScore);
        stage.addActor(buttonBack);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Constants.BG_COLOR.r,Constants.BG_COLOR.g,Constants.BG_COLOR.b,Constants.BG_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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

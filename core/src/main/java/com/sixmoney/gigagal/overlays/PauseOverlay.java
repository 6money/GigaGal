package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.entities.Lazer;
import com.sixmoney.gigagal.screens.GameplayScreen;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class PauseOverlay extends InputAdapter {
    public final static String TAG = PauseOverlay.class.getName();

    private GameplayScreen gameplayScreen;
    private Skin skin;
    private Table table;
    private Stage stage;

    public InputMultiplexer inputProcessor;

    public PauseOverlay(GameplayScreen gameplayScreen, SpriteBatch spriteBatch) {
        this.gameplayScreen = gameplayScreen;
        stage = new Stage(new ScreenViewport(), spriteBatch);
        inputProcessor = new InputMultiplexer(stage, this);
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
        table = new Table();
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().growX().center();

        Label labelPaused = new Label("PAUSED", skin, "gigagal-medium");
        labelPaused.setAlignment(Align.center);
        table.add(labelPaused).padTop(100f).padBottom(40f).padLeft(100f).padRight(100f).height(120f);
        table.row();
        TextButton buttonResume = new TextButton("RESUME", skin, "gigagal_32");
        buttonResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.level.paused = false;
                Gdx.input.setInputProcessor(gameplayScreen.inputMultiplexer);
            }
        });
        table.add(buttonResume).padLeft(150f).padRight(150f).height(100f);
        table.row();
        TextButton buttonRestart = new TextButton("RESTART", skin, "gigagal_32");
        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.levelComplete(false, true);
            }
        });
        table.add(buttonRestart).padLeft(150f).padRight(150f).height(100f);
        table.row();
        TextButton buttonQuit = new TextButton("QUIT", skin, "gigagal_32");
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.levelComplete(true, false);
            }
        });
        table.add(buttonQuit).padLeft(150f).padRight(150f).height(100f);
        table.pack();

        stage.addActor(table);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            gameplayScreen.level.paused = false;
            Gdx.input.setInputProcessor(gameplayScreen.inputMultiplexer);
            return true;
        }
        return false;
    }
}

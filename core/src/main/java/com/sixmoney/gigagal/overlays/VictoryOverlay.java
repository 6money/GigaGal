package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.entities.Explosion;
import com.sixmoney.gigagal.screens.GameplayScreen;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class VictoryOverlay extends InputAdapter {
    public final static String TAG = VictoryOverlay.class.getName();

    private GameplayScreen gameplayScreen;
    private Skin skin;
    private Stage stage;
    private Array<Explosion> explosions;

    public VictoryOverlay(GameplayScreen gameplayScreen, SpriteBatch spriteBatch) {
        this.gameplayScreen = gameplayScreen;
        stage = new Stage(new ScreenViewport(), spriteBatch);
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));
    }

    public void init(float score, boolean highScore) {
        explosions = new Array<>(Constants.EXPLOSION_COUNT);

        for (int i = 0; i < Constants.EXPLOSION_COUNT; i++) {
            Explosion explosion = new Explosion(new Vector2(MathUtils.random(0, stage.getWidth()), MathUtils.random(0, stage.getWidth())));
            explosion.offset = MathUtils.random(Constants.LEVEL_END_DURATION);
            explosions.add(explosion);
        }

        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().growX().center();

        String labelText = Constants.VICTORY_MESSAGE + "\n" + Constants.VICTORY_SCORE + score;
        float labelHeight = 180f;
        if (highScore) {
            labelText += "\nNEW HIGH SCORE!!";
            labelHeight = 270f;
        }
        Label labelVictory = new Label(labelText, skin, "gigagal-medium");
        labelVictory.setAlignment(Align.center);
        table.add(labelVictory).padTop(100f).padBottom(40f).padLeft(200f).padRight(200f).height(labelHeight);
        table.row();
        TextButton buttonContinue = new TextButton(Constants.NEXT_LEVEL_MESSAGE, skin, "gigagal_32");
        buttonContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.levelComplete();
            }
        });
        table.add(buttonContinue).padLeft(300f).padRight(300f).height(100f);
        table.row();
        TextButton buttonQuit = new TextButton(Constants.QUIT_MESSAGE, skin, "gigagal_32");
        buttonQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.levelComplete(true, false);
            }
        });
        table.add(buttonQuit).padLeft(300f).padRight(300f).height(100f);
        table.pack();

        stage.clear();
        stage.addActor(table);
    }

    public void render() {
        if (Utils.secondsSince(gameplayScreen.levelEndOverlayStartTime) > Constants.LEVEL_END_BLOCK) {
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                gameplayScreen.levelComplete();
            }
        }

        stage.act();
        stage.draw();

        stage.getBatch().begin();
        for (Explosion explosion: explosions) {
            explosion.render(stage.getBatch(), 4f);
        }
        stage.getBatch().end();
    }

    public void setInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
    }
}

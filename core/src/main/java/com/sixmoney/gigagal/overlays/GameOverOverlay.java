package com.sixmoney.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
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

public class GameOverOverlay {
    private GameplayScreen gameplayScreen;
    private Skin skin;
    private Stage stage;

    public GameOverOverlay(GameplayScreen gameplayScreen, SpriteBatch spriteBatch) {
        this.gameplayScreen = gameplayScreen;
        stage = new Stage(new ScreenViewport(), spriteBatch);
        skin = Assets.get_instance().skinAssets.skin;
    }

    public void init() {
        Table table = new Table();
        table.setFillParent(true);
        table.setPosition(0, 0);
        table.defaults().growX().center();

        Label labelGameOver = new Label(Constants.GAME_OVER_MESSAGE, skin, "gigagal-medium");
        labelGameOver.setAlignment(Align.center);
        table.add(labelGameOver).padTop(100f).padBottom(40f).padLeft(200f).padRight(200f).height(120f);
        table.row();
        TextButton buttonRestart = new TextButton(Constants.RESTART_MESSAGE, skin, "gigagal_32");
        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameplayScreen.levelComplete(false, true);
            }
        });
        table.add(buttonRestart).padLeft(300f).padRight(300f).height(100f);
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
                gameplayScreen.levelComplete(false, true);
            }
        }

        stage.act();
        stage.draw();
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

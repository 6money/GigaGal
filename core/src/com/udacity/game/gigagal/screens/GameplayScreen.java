package com.udacity.game.gigagal.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.game.gigagal.GigaGalGame;
import com.udacity.game.gigagal.Level;
import com.udacity.game.gigagal.overlays.GameOverOverlay;
import com.udacity.game.gigagal.overlays.GigaGalHUD;
import com.udacity.game.gigagal.overlays.OnScreeenControls;
import com.udacity.game.gigagal.overlays.PauseOverlay;
import com.udacity.game.gigagal.overlays.VictoryOverlay;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.ChaseCam;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.LevelLoader;
import com.udacity.game.gigagal.utils.PreferenceManager;
import com.udacity.game.gigagal.utils.Utils;

public class GameplayScreen extends ScreenAdapter {
    public static final String TAG = GameplayScreen.class.getName();

    public OnScreeenControls onScreeenControls;
    public PauseOverlay pauseOverlay;
    public boolean debug;
    public boolean debugMobile;

    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private ChaseCam chaseCam;
    private GigaGalHUD hud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    private long levelEndOverlayStartTime;
    private GigaGalGame gigaGalGame;
    private int level_num;
    private String level_name;
    private boolean old_paused;
    private ShapeRenderer shapeRenderer;

    public Level level;

    public GameplayScreen(GigaGalGame game, int level_num) {
        gigaGalGame = game;
        this.level_num = level_num;
        debug = game.debug;
        debugMobile = game.debugMobile;
    }

    public void setLevel_name() {
        this.level_name = "Level" + level_num;
    }

    @Override
    public void show() {
        setLevel_name();
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        extendViewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        level = LevelLoader.load(level_name, extendViewport);
        chaseCam = new ChaseCam(extendViewport.getCamera(), level.gigaGal);
        hud = new GigaGalHUD();
        victoryOverlay = new VictoryOverlay();
        gameOverOverlay = new GameOverOverlay();
        pauseOverlay = new PauseOverlay(this);
        onScreeenControls = new OnScreeenControls(this);
        onScreeenControls.gigaGal = level.gigaGal;
        levelEndOverlayStartTime = 0;

        if (onMobile() || debugMobile) {
            Gdx.input.setInputProcessor(onScreeenControls);
        }

    }

    public boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        hud.viewport.update(width, height, true);
        victoryOverlay.viewport.update(width, height, true);
        gameOverOverlay.viewport.update(width, height, true);
        pauseOverlay.viewport.update(width, height, true);
        pauseOverlay.update_rect();
        onScreeenControls.viewport.update(width, height, true);
        onScreeenControls.recalculateButtonPositions();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public void render(float delta) {
        old_paused = level.paused;
        level.update(delta);

        if (old_paused != level.paused && !level.paused) {
            if (onMobile() || debugMobile) {
                Gdx.input.setInputProcessor(onScreeenControls);
            } else {
                Gdx.input.setInputProcessor(null);
            }
        } else if (old_paused != level.paused && level.paused) {
            Gdx.input.setInputProcessor(pauseOverlay);
        }

        if (!level.victory && !level.gameOver) {
            chaseCam.update(delta);
            extendViewport.apply();
        }

        Gdx.gl.glClearColor(
                Constants.BG_COLOR.r,
                Constants.BG_COLOR.g,
                Constants.BG_COLOR.b,
                Constants.BG_COLOR.a
        );
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(extendViewport.getCamera().combined);

        spriteBatch.begin();
        level.render(spriteBatch);
        spriteBatch.end();

        if (onMobile() || debugMobile) {
            onScreeenControls.render(spriteBatch);
        }

        hud.render(spriteBatch, level.gigaGal.lives, level.gigaGal.ammmo_basic, level.gigaGal.ammmo_big, level.score);
        renderLevelEndOverlays(spriteBatch);

        if (level.paused) {
            pauseOverlay.render(spriteBatch);
        }

        if (debug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            level.debugRender(shapeRenderer);
            if (onMobile() || debugMobile) {
                onScreeenControls.debugRender(shapeRenderer);
            }
            shapeRenderer.end();
        }
    }

    private void renderLevelEndOverlays(SpriteBatch spriteBatch) {

        if (level.victory) {
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();

                Sound winEffect = Assets.instance.soundAssets.winEffect;
                winEffect.play();

                PreferenceManager preferenceManager = PreferenceManager.get_instance();
                Array<Integer> levelScores = preferenceManager.getScores(level_name);
                boolean highScore = false;

                if (levelScores.size == 0 || level.score > levelScores.get(0)) {
                    highScore = true;
                }

                preferenceManager.addScore(level_name, level.score);
                victoryOverlay.init(highScore);
            }

            victoryOverlay.render(spriteBatch, level.score);

            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                    levelComplete();
                }

                if (Gdx.input.isTouched()) {
                    levelComplete();
                }
            }
        } else if (level.gameOver) {
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();

                Sound loseEffect = Assets.instance.soundAssets.loseEffect;
                loseEffect.play();
            }

            gameOverOverlay.render(spriteBatch);

            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
                    levelComplete(true);
                }

                if (Gdx.input.isTouched()) {
                    levelComplete(true);
                }
            }
        }
    }

    private void startNewLevel() {
        setLevel_name();
        level = LevelLoader.load(level_name, extendViewport);
        chaseCam.chase_cam = level.viewport.getCamera();
        chaseCam.gigaGal = level.gigaGal;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        onScreeenControls.gigaGal = level.gigaGal;
    }

    public void levelComplete() {
        levelComplete(false);
    }

    public void levelComplete(boolean quit) {
        levelEndOverlayStartTime = 0;
        level_num++;

        if (level_num <= Constants.MAX_LEVEL && !level.gameOver && !quit) {
            startNewLevel();
        } else {
            gigaGalGame.switchScreen("menu");
        }
    }


    @Override
    public void hide() {
        super.hide();
        level.paused = true;
        Gdx.input.setInputProcessor(pauseOverlay);
    }

    @Override
    public void pause() {
        super.pause();
        level.paused = true;
        Gdx.input.setInputProcessor(pauseOverlay);
    }
}

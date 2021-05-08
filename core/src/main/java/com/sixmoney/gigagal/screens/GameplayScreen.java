package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.overlays.GameOverOverlay;
import com.sixmoney.gigagal.overlays.GigaGalHUD;
import com.sixmoney.gigagal.overlays.OnScreeenControls;
import com.sixmoney.gigagal.overlays.PauseOverlay;
import com.sixmoney.gigagal.overlays.VictoryOverlay;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.ChaseCam;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.LevelLoader;
import com.sixmoney.gigagal.utils.ParallaxCamera;
import com.sixmoney.gigagal.utils.PreferenceManager;
import com.sixmoney.gigagal.utils.SoundManager;
import com.sixmoney.gigagal.utils.Utils;

public class GameplayScreen extends ScreenAdapter {
    public static final String TAG = GameplayScreen.class.getName();

    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private ChaseCam chaseCam;
    private GigaGalHUD hud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    private GigaGalGame gigaGalGame;
    private String level_name;
    private boolean old_paused;
    private ShapeRenderer shapeRenderer;
    private ParallaxCamera parallaxCamera;
    private PreferenceManager preferenceManager;
    private float difficultly;

    public Level level;
    public int level_num;
    public long levelEndOverlayStartTime;
    public OnScreeenControls onScreeenControls;
    public PauseOverlay pauseOverlay;
    public boolean debug;
    public boolean debugMobile;

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
        parallaxCamera = new ParallaxCamera(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        extendViewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, parallaxCamera);
        preferenceManager = PreferenceManager.get_instance();
        difficultly = preferenceManager.getDifficulty();
        level = LevelLoader.load(level_name, difficultly, extendViewport, parallaxCamera);
        chaseCam = new ChaseCam(extendViewport.getCamera(), level.gigaGal);
        hud = new GigaGalHUD();
        victoryOverlay = new VictoryOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
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
        victoryOverlay.update_rect();
        gameOverOverlay.viewport.update(width, height, true);
        gameOverOverlay.update_rect();
        pauseOverlay.viewport.update(width, height, true);
        pauseOverlay.update_rect();
        onScreeenControls.viewport.update(width, height, true);
        onScreeenControls.recalculateButtonPositions();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        gameOverOverlay.dispose();
        hud.dispose();
        pauseOverlay.dispose();
        victoryOverlay.dispose();
        level.dispose();
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

        level.render(spriteBatch);

        if (onMobile() || debugMobile) {
            onScreeenControls.render(spriteBatch);
        }

        hud.render(spriteBatch, level.gigaGal.lives, level.gigaGal.ammmoBasic, level.gigaGal.ammmoBig, level.gigaGal.ammmoRapid, level.score);
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
            Gdx.input.setInputProcessor(victoryOverlay);
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();

                SoundManager.get_instance().playSound(Constants.WIN_EFFECT_PATH);
                Array<Integer> levelScores = preferenceManager.getScores(level_name);
                boolean highScore = false;

                if (levelScores.size == 0 || level.score > levelScores.get(0)) {
                    highScore = true;
                }

                preferenceManager.addScore(level_name, level.score);
                victoryOverlay.init(highScore);
            }
            victoryOverlay.render(spriteBatch, level.score);
        } else if (level.gameOver) {
            Gdx.input.setInputProcessor(gameOverOverlay);
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                SoundManager.get_instance().playSound(Constants.LOSE_EFFECT_PATH);
            }

            gameOverOverlay.render(spriteBatch);
        }
    }

    private void startNewLevel() {
        setLevel_name();
        level = LevelLoader.load(level_name, difficultly, extendViewport, parallaxCamera);
        chaseCam.chase_cam = level.viewport.getCamera();
        chaseCam.gigaGal = level.gigaGal;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        onScreeenControls.gigaGal = level.gigaGal;
        levelEndOverlayStartTime = 0;
        if (onMobile() || debugMobile) {
            Gdx.input.setInputProcessor(onScreeenControls);
        }
    }

    public void levelComplete() {
        levelComplete(false, false);
    }

    public void levelComplete(boolean quit, boolean restart) {
        if (!restart) {
            level_num++;
        }

        if (level_num <= Constants.MAX_LEVEL && !quit) {
            startNewLevel();
        } else {
            // This is here because we'd get crashes when the SpriteBatch was disposed
            Gdx.app.postRunnable(() -> {
                gigaGalGame.switchScreen("menu");
                dispose();
            });
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

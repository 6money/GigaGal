package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.sixmoney.gigagal.utils.ChaseCam;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.LevelLoader;
import com.sixmoney.gigagal.utils.ParallaxCamera;
import com.sixmoney.gigagal.utils.PreferenceManager;
import com.sixmoney.gigagal.utils.SoundManager;

public class GameplayScreen extends ScreenAdapter implements InputProcessor {
    public static final String TAG = GameplayScreen.class.getName();

    private SpriteBatch spriteBatch;
    private ChaseCam chaseCam;
    private GigaGalHUD hud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    private GigaGalGame gigaGalGame;
    private String level_name;
    private ShapeRenderer shapeRenderer;
    private ParallaxCamera parallaxCamera;
    private PreferenceManager preferenceManager;
    private float difficultly;
    private BitmapFont font;

    public ExtendViewport extendViewport;
    public Level level;
    public int level_num;
    public long levelEndOverlayStartTime;
    public OnScreeenControls onScreeenControls;
    public PauseOverlay pauseOverlay;
    public boolean debug;
    public boolean debugMobile;
    public InputMultiplexer inputMultiplexer;

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
        font = new BitmapFont(Gdx.files.internal("font/dialog.fnt"));
        font.getData().setScale(0.3f);
        preferenceManager = PreferenceManager.get_instance();
        difficultly = preferenceManager.getDifficulty();
        level = LevelLoader.load(level_name, difficultly, extendViewport, parallaxCamera);
        chaseCam = new ChaseCam(extendViewport.getCamera(), level.gigaGal);
        hud = new GigaGalHUD();
        victoryOverlay = new VictoryOverlay(this, spriteBatch);
        gameOverOverlay = new GameOverOverlay(this, spriteBatch);
        pauseOverlay = new PauseOverlay(this, spriteBatch);
        onScreeenControls = new OnScreeenControls(this);
        onScreeenControls.gigaGal = level.gigaGal;
        levelEndOverlayStartTime = 0;
        inputMultiplexer = new InputMultiplexer();

        if (onMobile() || debugMobile || preferenceManager.getMobile()) {
            inputMultiplexer.setProcessors(onScreeenControls, this);
        } else {
            inputMultiplexer.setProcessors(this);
        }
        Gdx.input.setInputProcessor(inputMultiplexer);

    }

    public boolean onMobile() {
        return gigaGalGame.onMobile();
    }

    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height, true);
        hud.viewport.update(width, height, true);
        victoryOverlay.resize(width, height);
        gameOverOverlay.resize(width, height);
        onScreeenControls.viewport.update(width, height, true);
        onScreeenControls.recalculateButtonPositions();
        pauseOverlay.resize(width, height);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        gameOverOverlay.dispose();
        hud.dispose();
        victoryOverlay.dispose();
        pauseOverlay.dispose();
        level.dispose();
    }

    @Override
    public void render(float delta) {
        level.update(delta);

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

        if (level_num == 1) {
            renderTutorial(spriteBatch);
        }

        if (onMobile() || debugMobile || preferenceManager.getMobile()) {
            onScreeenControls.render(spriteBatch);
        }

        hud.render(spriteBatch, level.gigaGal.lives, level.gigaGal.ammmoBasic, level.gigaGal.ammmoBig, level.gigaGal.ammmoRapid, level.score);
        renderLevelEndOverlays(spriteBatch);

        if (level.paused) {
            pauseOverlay.render();
        }

        if (debug) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            level.debugRender(shapeRenderer);
            if (onMobile() || debugMobile || preferenceManager.getMobile()) {
                onScreeenControls.debugRender(shapeRenderer);
            }
            shapeRenderer.end();
        }
    }

    private void renderLevelEndOverlays(SpriteBatch spriteBatch) {

        if (level.victory) {
            if (levelEndOverlayStartTime == 0) {
                victoryOverlay.setInputProcessor();
                levelEndOverlayStartTime = TimeUtils.nanoTime();

                SoundManager.get_instance().playSound(Constants.WIN_EFFECT_PATH);
                Array<Integer> levelScores = preferenceManager.getScores(level_name);
                boolean highScore = false;

                if (levelScores.size == 0 || level.score > levelScores.get(0)) {
                    highScore = true;
                }

                preferenceManager.addScore(level_name, level.score);
                victoryOverlay.init(level.score, highScore);
            }

            victoryOverlay.render();
        } else if (level.gameOver) {
            if (levelEndOverlayStartTime == 0) {
                gameOverOverlay.setInputProcessor();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                SoundManager.get_instance().playSound(Constants.LOSE_EFFECT_PATH);
                gameOverOverlay.init();
            }

            gameOverOverlay.render();
        }
    }

    private void renderTutorial(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        if (!onMobile()) {
            font.draw(spriteBatch, "LEFT: LEFT-ARROW", -20, 70);
            font.draw(spriteBatch, "RIGHT: RIGHT-ARROW", -20, 55);
            font.draw(spriteBatch, "DROP: DOWN-ARROW", -20, 40);
            font.draw(spriteBatch, "JUMP: Z", 90, 90);
            font.draw(spriteBatch, "SHOOT: X", 90, 75);
        }
        font.draw(spriteBatch, "COLLECT POWER-UPS TO GET", 150, 115);
        font.draw(spriteBatch, "MORE AMMO AND POINTS", 150, 105);
        font.draw(spriteBatch, "TRY TO MAKE IT TO THE END OF EACH", 240, 90);
        font.draw(spriteBatch, "LEVEL WITH AS HIGH A SCORE AS POSSIBLE", 240, 80);
        spriteBatch.end();
    }

    private void startNewLevel() {
        setLevel_name();
        level = LevelLoader.load(level_name, difficultly, extendViewport, parallaxCamera);
        chaseCam.chase_cam = level.viewport.getCamera();
        chaseCam.gigaGal = level.gigaGal;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        onScreeenControls.gigaGal = level.gigaGal;
        levelEndOverlayStartTime = 0;
        if (onMobile() || debugMobile || preferenceManager.getMobile()) {
            Gdx.input.setInputProcessor(onScreeenControls);
        }
    }

    public void levelComplete() {
        levelComplete(false, false);
    }

    public void levelComplete(boolean quit, boolean restart) {
        Gdx.input.setInputProcessor(inputMultiplexer);
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
        Gdx.input.setInputProcessor(pauseOverlay.inputProcessor);
    }

    @Override
    public void pause() {
        super.pause();
        level.paused = true;
        level.pauseRunningEffect();
        Gdx.input.setInputProcessor(pauseOverlay.inputProcessor);
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.X) {
            level.gigaGal.shootButtonPressed = true;
            return true;
        } else if (keycode == Input.Keys.ESCAPE) {
            level.paused = true;
            Gdx.input.setInputProcessor(pauseOverlay.inputProcessor);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.X) {
            level.gigaGal.shootButtonPressed = false;
            return true;
        }
        return false;
    }


    // All below this are currently unused, but required since we are implementing an interface
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

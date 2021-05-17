package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.PreferenceManager;
import com.sixmoney.gigagal.utils.SoundManager;

public class OptionsScreen implements Screen {
    public static final String TAG = OptionsScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private TextButton buttonBack;
    private Table tableOptions;
    private Window window;
    private CheckBox checkboxMusic;
    private CheckBox checkboxSounds;
    private CheckBox checkboxFPS;
    private CheckBox checkboxMobile;
    private Slider sliderMusic;
    private Slider sliderSounds;
    private Slider sliderDifficulty;
    private PreferenceManager preferenceManager;

    public OptionsScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH2));
        skin.getFont("font").getData().setScale(0.5f);

        buttonBack = new TextButton("Back", skin, "gigagal");
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

        Label labelVersion = new Label(Constants.VERSION, skin);
        labelVersion.setFontScale(0.5f);
        labelVersion.setAlignment(Align.left);
        labelVersion.setPosition(stage.getWidth() * 0.01f, stage.getHeight() * 0.01f);

        tableOptions = new Table(skin);
        tableOptions.pad(5);
        tableOptions.defaults().grow().pad(5);

        window = new Window("Options", skin, "gigagal");
        window.setSize(stage.getWidth() / 2, stage.getHeight() * 2 / 3);
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);
        window.defaults().grow();

        tableOptions.row();
        tableOptions.add(new Label("Music", skin));
        checkboxMusic = new CheckBox(null, skin);
        tableOptions.add(checkboxMusic);
        checkboxMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setMusic(checkboxMusic.isChecked());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Music Volume", skin));
        sliderMusic = new Slider(0, 100, 5, false, skin);
        tableOptions.add(sliderMusic);
        sliderMusic.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setMusicVolume(sliderMusic.getValue());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Sound Effects", skin));
        checkboxSounds = new CheckBox(null, skin);
        tableOptions.add(checkboxSounds);
        checkboxSounds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setSound(checkboxSounds.isChecked());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Effects Volume", skin));
        sliderSounds = new Slider(0, 100, 5, false, skin);
        tableOptions.add(sliderSounds);
        sliderSounds.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setSoundVolume(sliderSounds.getValue());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Difficulty", skin));
        sliderDifficulty = new Slider(0, 100, 50, false, skin);
        tableOptions.add(sliderDifficulty);
        sliderDifficulty.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setDifficulty(sliderDifficulty.getValue());
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Show FPS", skin));
        checkboxFPS = new CheckBox(null, skin);
        tableOptions.add(checkboxFPS);
        checkboxFPS.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setShowFPS(checkboxFPS.isChecked());
            }
        });

        tableOptions.row();
        tableOptions.add(new Label("Enable Mobile Controls", skin));
        checkboxMobile = new CheckBox(null, skin);
        tableOptions.add(checkboxMobile);
        checkboxMobile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setMobile(checkboxMobile.isChecked());
            }
        });

        tableOptions.row();
        TextButton buttonResetData = new TextButton("Reset all data", skin, "gigagal");
        tableOptions.add(buttonResetData);
        buttonResetData.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.removeData();
                init_saved_settings();
            }
        });
        tableOptions.add(new Label("This includes highsores", skin));

        window.add(tableOptions);

        stage.addActor(window);
        stage.addActor(buttonBack);
        stage.addActor(labelVersion);

        preferenceManager = PreferenceManager.get_instance();
        init_saved_settings();

        Gdx.input.setInputProcessor(stage);
    }

    private void init_saved_settings() {
        boolean musicEnabled = preferenceManager.getMusic();
        boolean soundEnabled = preferenceManager.getSound();
        float musicVolume = preferenceManager.getMusicVolume();
        float soundVolume = preferenceManager.getSoundVolume();
        float difficulty = preferenceManager.getDifficulty();
        boolean showFPS = preferenceManager.getShowFPS();
        boolean showMobile = preferenceManager.getMobile();

        checkboxMusic.setChecked(musicEnabled);
        sliderMusic.setValue(musicVolume);
        checkboxSounds.setChecked(soundEnabled);
        sliderSounds.setValue(soundVolume);
        sliderDifficulty.setValue(difficulty);
        checkboxFPS.setChecked(showFPS);
        checkboxMobile.setChecked(showMobile);

        SoundManager.get_instance().updateSoundPreferences();
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

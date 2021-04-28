package com.sixmoney.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.sixmoney.gigagal.GigaGalGame;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.PreferenceManager;
import com.sixmoney.gigagal.utils.SoundManager;

public class OptionsScreen implements Screen {
    public static final String TAG = OptionsScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;
    private Table tableOptions;
    private Window window;
    private CheckBox checkboxMusic;
    private CheckBox checkboxSounds;
    private Slider sliderMusic;
    private Slider sliderSounds;
    private PreferenceManager preferenceManager;

    public OptionsScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal(Constants.SKIN_PATH));

        buttonBack = new Button(skin);
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

        tableOptions = new Table(skin);
        tableOptions.pad(5);
        tableOptions.setSize(stage.getWidth()/ 2, stage.getHeight());

        tableOptions.row().pad(5);
        tableOptions.add(new Label("Music", skin)).pad(5);
        checkboxMusic = new CheckBox(null, skin);
        tableOptions.add(checkboxMusic).pad(5);
        checkboxMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setMusic(checkboxMusic.isChecked());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row().pad(5);
        tableOptions.add(new Label("Music Volume", skin)).pad(5);
        sliderMusic = new Slider(0, 100, 5, false, skin);
        tableOptions.add(sliderMusic).pad(5);
        sliderMusic.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setMusicVolume(sliderMusic.getValue());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row().pad(5);
        tableOptions.add(new Label("Sound Effects", skin)).pad(5);
        checkboxSounds = new CheckBox(null, skin);
        tableOptions.add(checkboxSounds).pad(5);
        checkboxSounds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.setSound(checkboxSounds.isChecked());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row().pad(5);
        tableOptions.add(new Label("Effects Volume", skin)).pad(5);
        sliderSounds = new Slider(0, 100, 5, false, skin);
        tableOptions.add(sliderSounds).pad(5);
        sliderSounds.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setSoundVolume(sliderSounds.getValue());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        tableOptions.row().pad(5);
        TextButton buttonResetData = new TextButton("Reset all data", skin);
        tableOptions.add(buttonResetData);
        buttonResetData.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                preferenceManager.removeData();
                init_saved_settings();
            }
        });
        tableOptions.add(new Label("This includes highsores", skin));

        window = new Window("Options", skin);
        window.add(tableOptions);
        window.pack();
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);

        stage.addActor(window);
        stage.addActor(buttonBack);

        preferenceManager = PreferenceManager.get_instance();
        init_saved_settings();

        Gdx.input.setInputProcessor(stage);
    }

    private void init_saved_settings() {
        boolean musicEnabled = preferenceManager.getMusic();
        boolean soundEnabled = preferenceManager.getSound();
        float musicVolume = preferenceManager.getMusicVolume();
        float soundVolume = preferenceManager.getSoundVolume();

        checkboxMusic.setChecked(musicEnabled);
        sliderMusic.setValue(musicVolume);
        checkboxSounds.setChecked(soundEnabled);
        sliderSounds.setValue(soundVolume);

        SoundManager.get_instance().updateSoundPreferences();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f,0f,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        skin.dispose();
        stage.dispose();
    }
}

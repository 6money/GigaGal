package com.udacity.game.gigagal.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.udacity.game.gigagal.GigaGalGame;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.PreferenceManager;
import com.udacity.game.gigagal.utils.SoundManager;

public class OptionsScreen implements Screen {
    public static final String TAG = OptionsScreen.class.getName();

    private GigaGalGame game;
    private Stage stage;
    private Skin skin;
    private Button buttonBack;
    private Table tableOptions;
    private Window window;

    public OptionsScreen(GigaGalGame gigaGalGame) {
        game = gigaGalGame;
    }

    @Override
    public void show() {
        final PreferenceManager preferenceManager = PreferenceManager.get_instance();
        boolean musicEnabled = preferenceManager.getMusic();
        boolean soundEnabled = preferenceManager.getSound();
        float musicVolume = preferenceManager.getMusicVolume();
        float soundVolume = preferenceManager.getSoundVolume();

        stage = new Stage(new ScreenViewport());
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
            }
        });

        tableOptions = new Table(skin);
        tableOptions.pad(5);
        tableOptions.setSize(stage.getWidth()/ 2, stage.getHeight());

        tableOptions.row().pad(5);
        tableOptions.add(new Label("Music", skin)).pad(5);
        final CheckBox checkboxMusic = new CheckBox(null, skin);
        checkboxMusic.setChecked(musicEnabled);
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
        final Slider sliderMusic = new Slider(0, 100, 5, false, skin);
        sliderMusic.setValue(musicVolume);
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
        final CheckBox checkboxSounds = new CheckBox(null, skin);
        checkboxSounds.setChecked(soundEnabled);
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
        final Slider sliderEffects = new Slider(0, 100, 5, false, skin);
        sliderEffects.setValue(soundVolume);
        tableOptions.add(sliderEffects).pad(5);
        sliderEffects.addListener(new DragListener() {
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                preferenceManager.setSoundVolume(sliderEffects.getValue());
                SoundManager.get_instance().updateSoundPreferences();
            }
        });

        window = new Window("Options", skin);
        window.add(tableOptions);
        window.pack();
        window.setPosition(stage.getWidth() / 2 - window.getWidth() / 2, stage.getHeight() / 2 - window.getHeight() / 2);

        stage.addActor(window);
        stage.addActor(buttonBack);

        Gdx.input.setInputProcessor(stage);
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

    }
}

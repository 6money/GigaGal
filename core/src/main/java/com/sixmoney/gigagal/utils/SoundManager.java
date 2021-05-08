package com.sixmoney.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

public class SoundManager implements Disposable, AssetErrorListener {
    private final static String TAG = SoundManager.class.getName();

    private static SoundManager soundManager;
    private AssetManager assetManager;
    private boolean musicEnabled;
    private boolean soundEnabled;
    private float musicVolume;
    private float soundVolume;
    private Music backgroundMusic;


    private SoundManager() {
        init();
        updateSoundPreferences();
    }


    public static SoundManager get_instance() {
        if (soundManager == null) {
            soundManager = new SoundManager();
        }

        return soundManager;
    }


    private void init() {
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        long startLoad = TimeUtils.nanoTime();
        assetManager.load(Constants.GUNSHOT1_PATH, Sound.class);
        assetManager.load(Constants.GUNSHOT2_PATH, Sound.class);
        assetManager.load(Constants.GUNSHOT3_PATH, Sound.class);
        assetManager.load(Constants.GUNSHOT4_PATH, Sound.class);
        assetManager.load(Constants.EXPLOSION1_PATH, Sound.class);
        assetManager.load(Constants.EXPLOSION2_PATH, Sound.class);
        assetManager.load(Constants.DEATH_SOUND_PATH, Sound.class);
        assetManager.load(Constants.JUMP_SOUND_PATH, Sound.class);
        assetManager.load(Constants.RUNNING_SOUND_PATH, Sound.class);
        assetManager.load(Constants.COLLECT_DIAMOND_PATH, Sound.class);
        assetManager.load(Constants.COLLECT_POWERUP_PATH, Sound.class);
        assetManager.load(Constants.WIN_EFFECT_PATH, Sound.class);
        assetManager.load(Constants.LOSE_EFFECT_PATH, Sound.class);
        assetManager.load(Constants.MUSIC_PATH, Music.class);
        assetManager.finishLoading();
        Gdx.app.log(TAG, "Sound assets Loaded in " + Utils.secondsSince(startLoad) + " seconds");
        Gdx.app.log(TAG, assetManager.getAssetNames().toString());

        backgroundMusic = assetManager.get(Constants.MUSIC_PATH);
    }


    public void updateSoundPreferences() {
        final PreferenceManager preferenceManager = PreferenceManager.get_instance();
        musicEnabled = preferenceManager.getMusic();
        soundEnabled = preferenceManager.getSound();
        musicVolume = preferenceManager.getMusicVolume();
        soundVolume = preferenceManager.getSoundVolume();

        if (musicEnabled) {
            backgroundMusic.setVolume(musicVolume / 100);
        } else {
            backgroundMusic.setVolume(0);
        }
    }


    public long playSound(String soundname) {
        return playSound(soundname, false);
    }


    public long playSound(String soundName, boolean looping) {
        float baseVolume = 1;
        Sound sound = assetManager.get(soundName);
        long soundid = -1;

        switch (soundName) {
            case Constants.EXPLOSION1_PATH:
            case Constants.EXPLOSION2_PATH:
                baseVolume = 0.4f;
                break;
            case Constants.DEATH_SOUND_PATH:
            case Constants.GUNSHOT1_PATH:
            case Constants.GUNSHOT2_PATH:
            case Constants.GUNSHOT3_PATH:
            case Constants.GUNSHOT4_PATH:
                baseVolume = 0.5f;
                break;
        }

        if (soundEnabled) {
            soundid = sound.play((soundVolume / 100) * baseVolume);
            sound.setLooping(soundid, looping);
        }
        return soundid;
    }


    public void pauseSound(String soundName, long soundId) {
        Sound sound = assetManager.get(soundName);
        sound.pause(soundId);
    }


    public void resumeSound(String soundName, long soundId) {
        Sound sound = assetManager.get(soundName);
        sound.resume(soundId);
    }


    public void stopSound(String soundName, long soundId) {
        Sound sound = assetManager.get(soundName);
        sound.stop(soundId);
    }


    public void playMusic(String soundName) {
        backgroundMusic = assetManager.get(soundName);
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        if (musicEnabled) {
            backgroundMusic.setVolume(musicVolume / 100);
        } else {
            backgroundMusic.setVolume(0);
        }
    }


    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }


    @Override
    public void dispose() {
        assetManager.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();
    }
}

package com.sixmoney.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;

public class PreferenceManager {
    private final static String TAG = PreferenceManager.class.getName();

    private static Preferences preferences;
    private static PreferenceManager preferenceManager;

    private PreferenceManager() {
        preferences = Gdx.app.getPreferences(Constants.PREFERENCES_NAME);
    }

    public static PreferenceManager get_instance() {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager();
        }

        return preferenceManager;
    }

    public Array<Integer> getScores(String levelName) {
        String scoresList = preferences.getString(levelName, "");
        Array<Integer> scores = new Array<>(true, 6);
        String[] scoresJavaArray = scoresList.split(",");
        Gdx.app.log(TAG, scoresList);

        if (!scoresJavaArray[0].equals("")) {
            for (String textscore : scoresJavaArray) {
                scores.add(Integer.parseInt(textscore));
            }
        }

        Sort.instance().sort(scores);
        scores.reverse();

        return scores;
    }

    public void addScore(String levelName, int score) {
        Array<Integer> scores = getScores(levelName);
        scores.add(score);
        Gdx.app.log(TAG, scores.toString());

        if (scores.size > 5) {
            Sort.instance().sort(scores);
            scores.reverse();
            scores.pop();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int savedScore: scores) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(",");
            }
            stringBuilder.append(savedScore);
        }

        Gdx.app.log(TAG, stringBuilder.toString());

        preferences.putString(levelName, stringBuilder.toString());
        preferences.flush();
    }

    public boolean getMusic() {
        return preferences.getBoolean("musicEnabled", true);
    }

    public void setMusic(boolean value) {
        preferences.putBoolean("musicEnabled", value);
        preferences.flush();
    }

    public float getMusicVolume() {
        return preferences.getFloat("musicVolume", 60f);
    }

    public void setMusicVolume(float value) {
        preferences.putFloat("musicVolume", value);
        preferences.flush();
    }

    public boolean getSound() {
        return preferences.getBoolean("soundEnabled", true);
    }

    public void setSound(boolean value) {
        preferences.putBoolean("soundEnabled", value);
        preferences.flush();
    }

    public float getSoundVolume() {
        return preferences.getFloat("soundVolume", 100f);
    }

    public void setSoundVolume(float value) {
        preferences.putFloat("soundVolume", value);
        preferences.flush();
    }

    public float getDifficulty() {
        return preferences.getFloat("difficulty", 0);
    }

    public void setDifficulty(float value) {
        preferences.putFloat("difficulty", value);
        preferences.flush();
    }

    public boolean getShowFPS() {
        return preferences.getBoolean("showFPS", false);
    }

    public void setShowFPS(boolean value) {
        preferences.putBoolean("showFPS", value);
        preferences.flush();
    }

    public boolean getMobile() {
        return preferences.getBoolean("showMobile", false);
    }

    public void setMobile(boolean value) {
        preferences.putBoolean("showMobile", value);
        preferences.flush();
    }

    public void removeData() {
        preferences.remove("musicEnabled");
        preferences.remove("musicVolume");
        preferences.remove("soundEnabled");
        preferences.remove("soundVolume");
        preferences.remove("difficulty");
        preferences.remove("showFPS");
        preferences.remove("showMobile");
        preferences.remove("Level1");
        preferences.remove("Level2");
        preferences.remove("Level3");
        preferences.remove("Level4");
        preferences.remove("Level5");
        preferences.remove("Level6");
        preferences.remove("Level7");
        preferences.remove("Level8");
        preferences.flush();
    }
}

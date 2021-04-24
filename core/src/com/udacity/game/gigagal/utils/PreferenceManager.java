package com.udacity.game.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

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

    public void addScore(String level_name, int score) {
        String scoresList = preferences.getString(level_name, "");
        Array<Integer> scores = new Array<>();
        String[] scoresJavaArray = scoresList.split(",");

        if (!scoresJavaArray[0].equals("")) {
            for (String textscore : scoresJavaArray) {
                scores.add(Integer.parseInt(textscore));
            }
        }

        scores.add(score);

        if (scores.size > 5) {
            scores.pop();
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int savedScore: scores) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(",");
            }
            stringBuilder.append(savedScore);
        }

        Gdx.app.log(TAG, scoresList);
        Gdx.app.log(TAG, stringBuilder.toString());

        preferences.putString(level_name, stringBuilder.toString());
        preferences.flush();
    }
}

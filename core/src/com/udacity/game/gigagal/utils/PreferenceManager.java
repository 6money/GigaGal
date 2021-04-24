package com.udacity.game.gigagal.utils;

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
}

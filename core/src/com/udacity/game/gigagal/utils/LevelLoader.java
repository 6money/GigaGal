package com.udacity.game.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.game.gigagal.Level;
import com.udacity.game.gigagal.entities.BigEnemy;
import com.udacity.game.gigagal.entities.BigPowerup;
import com.udacity.game.gigagal.entities.Enemy;
import com.udacity.game.gigagal.entities.ExitPortal;
import com.udacity.game.gigagal.entities.GigaGal;
import com.udacity.game.gigagal.entities.Platform;
import com.udacity.game.gigagal.entities.Powerup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();

    public static Level load(String levelName, Viewport viewport) {

        Level level = new Level(viewport);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;


        try {
            FileHandle fileHandle = Gdx.files.internal(path);
            JSONParser jsonParser = new JSONParser();

            JSONObject levelObj = (JSONObject) jsonParser.parse(fileHandle.reader());
            JSONObject compositeObj = (JSONObject) levelObj.get(Constants.LEVEL_COMPOSITE);

            JSONArray platformsArray = (JSONArray) compositeObj.get(Constants.LEVEL_9PATCHES);
            JSONObject platform = (JSONObject) platformsArray.get(0);
            loadPlatforms(platformsArray, level);

            JSONArray nonPlatformObjects = (JSONArray) compositeObj.get(Constants.LEVEL_IMAGES);
            loadNonPlatformEntities(level, nonPlatformObjects);
        } catch (Exception ex) {
            Gdx.app.error(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }

    private static float safeGetFloat(JSONObject object, String key){
        Number number = (Number) object.get(key);
        return (number == null) ? 0 : number.floatValue();
    }

    private static void loadPlatforms(JSONArray array, Level level) {

        Array<Platform> platformArray = new Array<>();

        for (Object object : array) {
            final JSONObject platformObject = (JSONObject) object;

            // Not that this is the BOTTOM of the platform, not the top
            // Also note that if the platform is at (0, 0), the x and y keys will be missing from the JSON
            final float x = safeGetFloat(platformObject, Constants.LEVEL_X_KEY);
            final float y = safeGetFloat(platformObject, Constants.LEVEL_Y_KEY);

            final float width = ((Number) platformObject.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
            final float height = ((Number) platformObject.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();

            Gdx.app.log(TAG, "Location: x:" + x + " y: " + y + " width: " + width + " height: " + height);

            Platform platform = new Platform(x, y + height, width, height);

            platformArray.add(platform);

            String platform_identifier = (String) platformObject.get(Constants.LEVEL_IDENTIFIER_KEY);

            if (platform_identifier != null && platform_identifier.equals(Constants.LEVEL_ENEMY_TAG)) {
                level.getEnemies().add(new Enemy(platform));
            } else if (platform_identifier != null && platform_identifier.equals(Constants.LEVEL_ENEMY_BIG_TAG)) {
                level.getEnemies().add(new BigEnemy(platform));
            }
        }

        platformArray.sort();
        platformArray.reverse();
        Gdx.app.log(TAG, platformArray.toString());

        level.setPlatforms(platformArray);

    }

    private static void loadNonPlatformEntities(Level level, JSONArray nonPlatformObjects) {
        for (Object o : nonPlatformObjects) {

            // First we need to cast the object to a JSONObject
            JSONObject item = (JSONObject) o;

            Vector2 lowerLeftCorner = new Vector2(safeGetFloat(item, Constants.LEVEL_X_KEY), safeGetFloat(item, Constants.LEVEL_Y_KEY));
            String item_identifier = (String) item.get(Constants.LEVEL_IDENTIFIER_KEY);

            if (item_identifier != null && item_identifier.equals(Constants.LEVEL_KILLPLANE_TAG)) {

                float killplane_height = lowerLeftCorner.y;
                level.setKillplane_height(killplane_height);
                continue;
            }

            // Check if this object is GigaGal
            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STANDING_RIGHT)) {

                // If so, add GigaGal's eye position to find her spawn position
                final Vector2 gigaGalPosition = lowerLeftCorner.add(Constants.GIGAGAL_EYE_POS);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);

                // Add our new GigaGal to the level
                level.setGigaGal(new GigaGal(gigaGalPosition, level));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.EXIT_PORTAL_SPRITE_1)) {
                Vector2 portal_position = new Vector2(safeGetFloat(item, Constants.LEVEL_X_KEY), safeGetFloat(item, Constants.LEVEL_Y_KEY));
                Gdx.app.log(TAG, "Loaded Portal at " + portal_position);
                level.setExitPortal(new ExitPortal(portal_position));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POWERUP2)) {
                Vector2 powerup_position = new Vector2(safeGetFloat(item, Constants.LEVEL_X_KEY), safeGetFloat(item, Constants.LEVEL_Y_KEY));
                Gdx.app.log(TAG, "Loaded powerup at " + powerup_position);
                level.getPowerups().add(new BigPowerup(powerup_position));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POWERUP)) {
                Vector2 powerup_position = new Vector2(safeGetFloat(item, Constants.LEVEL_X_KEY), safeGetFloat(item, Constants.LEVEL_Y_KEY));
                Gdx.app.log(TAG, "Loaded powerup at " + powerup_position);
                level.getPowerups().add(new Powerup(powerup_position));
                continue;
            }
        }
    }
}

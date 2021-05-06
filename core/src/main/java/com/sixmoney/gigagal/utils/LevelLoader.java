package com.sixmoney.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.entities.EnemyBig;
import com.sixmoney.gigagal.entities.EnemyRanged;
import com.sixmoney.gigagal.entities.PowerupBig;
import com.sixmoney.gigagal.entities.Diamond;
import com.sixmoney.gigagal.entities.Enemy;
import com.sixmoney.gigagal.entities.ExitPortal;
import com.sixmoney.gigagal.entities.GigaGal;
import com.sixmoney.gigagal.entities.Platform;
import com.sixmoney.gigagal.entities.PlatformHard;
import com.sixmoney.gigagal.entities.PlatformMedium;
import com.sixmoney.gigagal.entities.Powerup;
import com.sixmoney.gigagal.entities.PowerupRapid;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, Viewport viewport) {

        Level level = new Level(viewport);
        String path = Constants.LEVEL_DIR + File.separator + levelName + Constants.LEVEL_FILE_EXTENSION;


        try {
            FileHandle fileHandle = Gdx.files.internal(path);
            JsonValue jsonRoot = new JsonReader().parse(fileHandle);
            JsonValue compositeObj = jsonRoot.get(Constants.LEVEL_COMPOSITE);
            JsonValue platformsArray = compositeObj.get(Constants.LEVEL_9PATCHES);
            loadPlatforms(platformsArray, level);

            JsonValue nonPlatformObjects = compositeObj.get(Constants.LEVEL_IMAGES);
            loadNonPlatformEntities(level, nonPlatformObjects);
        } catch (Exception ex) {
            Gdx.app.error(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }


    private static void loadPlatforms(JsonValue array, Level level) {

        Array<Platform> platformArray = new Array<>();

        for (JsonValue platformObject : array) {
            // Not that this is the BOTTOM of the platform, not the top
            // Also note that if the platform is at (0, 0), the x and y keys will be missing from the JSON
            final float x = platformObject.getFloat(Constants.LEVEL_X_KEY, 0);
            final float y = platformObject.getFloat(Constants.LEVEL_Y_KEY, 0);
            final float width = platformObject.getFloat(Constants.LEVEL_WIDTH_KEY, 0);
            final float height = platformObject.getFloat(Constants.LEVEL_HEIGHT_KEY, 0);

            Gdx.app.log(TAG, "Location: x:" + x + " y: " + y + " width: " + width + " height: " + height);

            Platform platform;

            if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_HARD)) {
                platform = new PlatformHard(x, y + height, width, height);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_MEDIUM)) {
                platform = new PlatformMedium(x, y + height, width, height);
            } else {
                platform = new Platform(x, y + height, width, height);
            }

            platformArray.add(platform);

            String platform_identifier = platformObject.getString(Constants.LEVEL_IDENTIFIER_KEY, null);

            if (platform_identifier != null && platform_identifier.equals(Constants.LEVEL_ENEMY_TAG)) {
                level.getEnemies().add(new Enemy(platform, level));
            } else if (platform_identifier != null && platform_identifier.equals(Constants.LEVEL_ENEMY_BIG_TAG)) {
                level.getEnemies().add(new EnemyBig(platform, level));
            } else if (platform_identifier != null && platform_identifier.equals(Constants.LEVEL_ENEMY_RANGED_TAG)) {
                level.getEnemies().add(new EnemyRanged(platform, level));
            }
        }

        platformArray.sort();
        platformArray.reverse();

        level.setPlatforms(platformArray);

    }

    private static void loadNonPlatformEntities(Level level, JsonValue nonPlatformObjects) {
        for (JsonValue item : nonPlatformObjects) {

            Vector2 lowerLeftCorner = new Vector2(item.getFloat(Constants.LEVEL_X_KEY, 0), item.getFloat(Constants.LEVEL_Y_KEY, 0));
            String item_identifier = item.getString(Constants.LEVEL_IDENTIFIER_KEY, null);

            if (item_identifier != null && item_identifier.equals(Constants.LEVEL_KILLPLANE_TAG)) {

                float killplane_height = lowerLeftCorner.y;
                level.setKillplane_height(killplane_height);
                continue;
            }

            // Check if this object is GigaGal
            if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STANDING_RIGHT)) {

                // If so, add GigaGal's eye position to find her spawn position
                final Vector2 gigaGalPosition = lowerLeftCorner.add(Constants.GIGAGAL_EYE_POS);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);

                // Add our new GigaGal to the level
                level.setGigaGal(new GigaGal(gigaGalPosition, level));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.EXIT_PORTAL_SPRITE_1)) {
                Gdx.app.log(TAG, "Loaded Portal at " + lowerLeftCorner);
                level.setExitPortal(new ExitPortal(lowerLeftCorner));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.POWERUP)) {
                Gdx.app.log(TAG, "Loaded powerup at " + lowerLeftCorner);
                level.getPowerups().add(new Powerup(lowerLeftCorner));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.POWERUP2)) {
                Gdx.app.log(TAG, "Loaded big powerup at " + lowerLeftCorner);
                level.getPowerups().add(new PowerupBig(lowerLeftCorner));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.POWERUP3)) {
                Gdx.app.log(TAG, "Loaded rapid powerup at " + lowerLeftCorner);
                level.getPowerups().add(new PowerupRapid(lowerLeftCorner));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.DIAMOND)) {
                Gdx.app.log(TAG, "Loaded diamond at " + lowerLeftCorner);
                level.getDiamonds().add(new Diamond(lowerLeftCorner));
            }
        }
    }
}

package com.sixmoney.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.entities.Boss;
import com.sixmoney.gigagal.entities.Diamond;
import com.sixmoney.gigagal.entities.Enemy;
import com.sixmoney.gigagal.entities.EnemyBig;
import com.sixmoney.gigagal.entities.EnemyRanged;
import com.sixmoney.gigagal.entities.ExitPortal;
import com.sixmoney.gigagal.entities.GigaGal;
import com.sixmoney.gigagal.entities.Platform;
import com.sixmoney.gigagal.entities.PlatformBounce;
import com.sixmoney.gigagal.entities.PlatformHard;
import com.sixmoney.gigagal.entities.PlatformLight;
import com.sixmoney.gigagal.entities.PlatformMedium;
import com.sixmoney.gigagal.entities.Powerup;
import com.sixmoney.gigagal.entities.PowerupBig;
import com.sixmoney.gigagal.entities.PowerupNuke;
import com.sixmoney.gigagal.entities.PowerupRapid;

import java.io.File;

public class LevelLoader {
    public static final String TAG = LevelLoader.class.toString();


    public static Level load(String levelName, float difficultly, Viewport viewport, ParallaxCamera parallaxCamera) {

        Level level = new Level(viewport, difficultly, parallaxCamera);
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
            final int zIndex = platformObject.getInt(Constants.LEVEL_ZINDEX, 0);

            Platform platform;

            if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_HARD)) {
                platform = new PlatformHard(x, y + height, width, height, zIndex);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_MEDIUM)) {
                platform = new PlatformMedium(x, y + height, width, height, zIndex);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_BOUNCE)) {
                platform = new PlatformBounce(x, y + height, width, height, zIndex);
            } else if (platformObject.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_LIGHT)) {
                platform = new PlatformLight(x, y + height, width, height, zIndex);
            } else {
                platform = new Platform(x, y + height, width, height, zIndex);
            }

            Gdx.app.log(TAG, platform.toString());
            platformArray.add(platform);

            String[] platform_vars = platformObject.getString(Constants.LEVEL_CUSTOM_VARS, "").split(";");
            for (String var: platform_vars) {
                Gdx.app.log(TAG, var);
            }

            if (!platform_vars[0].equals("")) {
                for (String var: platform_vars) {
                    String[] platform_var_pair = var.split(":");
                    String platform_identifier = platform_var_pair[0];
                    int numEnemies = Integer.parseInt(platform_var_pair[1]);

                    switch (platform_identifier) {
                        case Constants.LEVEL_ENEMY_TAG:
                            for (int i = 0; i < numEnemies; i++) {
                                level.getEnemies().add(new Enemy(platform, level));
                            }
                            break;
                        case Constants.LEVEL_ENEMY_BIG_TAG:
                            for (int i = 0; i < numEnemies; i++) {
                                level.getEnemies().add(new EnemyBig(platform, level));
                            }
                            break;
                        case Constants.LEVEL_ENEMY_RANGED_TAG:
                            for (int i = 0; i < numEnemies; i++) {
                                level.getEnemies().add(new EnemyRanged(platform, level));
                            }
                            break;
                        case Constants.LEVEL_BOSS_TAG:
                            for (int i = 0; i < numEnemies; i++) {
                                level.getBosses().add(new Boss(platform, level));
                            }
                            break;
                    }
                }
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

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.POWERUP4)) {
                Gdx.app.log(TAG, "Loaded nuke powerup at " + lowerLeftCorner);
                level.getPowerups().add(new PowerupNuke(lowerLeftCorner));
                continue;
            }

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).asString().equals(Constants.DIAMOND)) {
                Gdx.app.log(TAG, "Loaded diamond at " + lowerLeftCorner);
                level.getDiamonds().add(new Diamond(lowerLeftCorner));
            }
        }
    }
}

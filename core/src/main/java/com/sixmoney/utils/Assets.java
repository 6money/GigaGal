package com.sixmoney.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    private AssetManager assetManager;

    public GigaGalAssets gigaGalAssets;
    public PlatformAssets platformAssets;
    public EnemyAssets enemyAssets;
    public BulletAssets bulletAssets;
    public ExplosionAssets explosionAssets;
    public PowerupAssets powerupAssets;
    public ExitPortalAssets exitPortalAssets;
    public OnscreenControlsAssets onscreenControlsAssets;
    public DiamondAssets diamondAssets;

    private Assets() {
    }

    public void init() {
        this.assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS);
        gigaGalAssets = new GigaGalAssets(atlas);
        platformAssets = new PlatformAssets(atlas);
        enemyAssets = new EnemyAssets(atlas);
        bulletAssets = new BulletAssets(atlas);
        explosionAssets = new ExplosionAssets(atlas);
        powerupAssets = new PowerupAssets(atlas);
        exitPortalAssets = new ExitPortalAssets(atlas);
        onscreenControlsAssets = new OnscreenControlsAssets(atlas);
        diamondAssets = new DiamondAssets(atlas);
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }


    public class GigaGalAssets {

        public TextureAtlas.AtlasRegion standing_right;
        public TextureAtlas.AtlasRegion standing_left;
        public TextureAtlas.AtlasRegion jumping_right;
        public TextureAtlas.AtlasRegion jumping_left;
        public TextureAtlas.AtlasRegion walk_1_right;
        public TextureAtlas.AtlasRegion walk_1_left;
        public TextureAtlas.AtlasRegion walk_2_right;
        public TextureAtlas.AtlasRegion walk_2_left;
        public TextureAtlas.AtlasRegion walk_3_right;
        public TextureAtlas.AtlasRegion walk_3_left;

        public Animation walkLeftLoop;
        public Animation walkRightLoop;

        public GigaGalAssets(TextureAtlas atlas) {
            standing_right = atlas.findRegion(Constants.STANDING_RIGHT);
            standing_left = atlas.findRegion(Constants.STANDING_LEFT);
            jumping_right = atlas.findRegion(Constants.JUMPING_RIGHT);
            jumping_left = atlas.findRegion(Constants.JUMPING_LEFT);
            walk_1_right = atlas.findRegion(Constants.WALK_1_RIGHT);
            walk_1_left = atlas.findRegion(Constants.WALK_1_LEFT);
            walk_2_right = atlas.findRegion(Constants.WALK_2_RIGHT);
            walk_2_left = atlas.findRegion(Constants.WALK_2_LEFT);
            walk_3_right = atlas.findRegion(Constants.WALK_3_RIGHT);
            walk_3_left = atlas.findRegion(Constants.WALK_3_LEFT);

            Array<TextureAtlas.AtlasRegion> walkLeftTextures = new Array<>();
            walkLeftTextures.add(walk_1_left);
            walkLeftTextures.add(walk_2_left);
            walkLeftTextures.add(walk_3_left);

            Array<TextureAtlas.AtlasRegion> walkRightTextures = new Array<>();
            walkRightTextures.add(walk_1_right);
            walkRightTextures.add(walk_2_right);
            walkRightTextures.add(walk_3_right);

            walkLeftLoop = new Animation(Constants.WALK_DURATION, walkLeftTextures, Animation.PlayMode.LOOP_PINGPONG);
            walkRightLoop = new Animation(Constants.WALK_DURATION, walkRightTextures, Animation.PlayMode.LOOP_PINGPONG);
        }
    }


    public class PlatformAssets {

        public TextureAtlas.AtlasRegion platform;
        public TextureAtlas.AtlasRegion platform_hard;
        public TextureAtlas.AtlasRegion platform_medium;
        public NinePatch ninePatch_platform;
        public NinePatch ninePatch_platform_hard;
        public NinePatch ninePatch_platform_medium;


        public PlatformAssets(TextureAtlas atlas) {
            platform = atlas.findRegion(Constants.PLATFORM);
            platform_hard = atlas.findRegion(Constants.PLATFORM_HARD);
            platform_medium = atlas.findRegion(Constants.PLATFORM_MEDIUM);
            ninePatch_platform = new NinePatch(
                    platform,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH
            );
            ninePatch_platform_hard = new NinePatch(
                    platform_hard,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH
            );
            ninePatch_platform_medium = new NinePatch(
                    platform_medium,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH,
                    Constants.PLATFORM_EDGE_LENGTH
            );
        }
    }


    public class EnemyAssets {
        public TextureAtlas.AtlasRegion enemy;
        public TextureAtlas.AtlasRegion enemy2;

        public EnemyAssets(TextureAtlas atlas) {
            enemy = atlas.findRegion(Constants.ENEMY);
            enemy2 = atlas.findRegion(Constants.ENEMY2);
        }
    }


    public class BulletAssets {
        public TextureAtlas.AtlasRegion bullet;
        public TextureAtlas.AtlasRegion bullet2;

        public BulletAssets(TextureAtlas atlas) {
            bullet = atlas.findRegion(Constants.BULLET);
            bullet2 = atlas.findRegion(Constants.BULLET2);
        }
    }


    public class ExplosionAssets {
        private TextureAtlas.AtlasRegion explosion_large;
        private TextureAtlas.AtlasRegion explosion_larger;
        private TextureAtlas.AtlasRegion explosion_largest;
        private TextureAtlas.AtlasRegion explosion_medium;
        private TextureAtlas.AtlasRegion explosion_small;

        public final Animation explosion_animation;
        public final Animation explosion_big_animation;

        public ExplosionAssets(TextureAtlas atlas) {
            explosion_large = atlas.findRegion(Constants.EXPLOSION_LARGE);
            explosion_larger = atlas.findRegion(Constants.EXPLOSION_LARGER);
            explosion_largest = atlas.findRegion(Constants.EXPLOSION_LARGEST);
            explosion_medium = atlas.findRegion(Constants.EXPLOSION_MEDIUM);
            explosion_small = atlas.findRegion(Constants.EXPLOSION_SMALL);

            Array<TextureAtlas.AtlasRegion> explosionTextures = new Array<>();
            explosionTextures.add(explosion_large);
            explosionTextures.add(explosion_medium);
            explosionTextures.add(explosion_small);

            Array<TextureAtlas.AtlasRegion> explosionBigTextures = new Array<>();
            explosionBigTextures.add(explosion_largest);
            explosionBigTextures.add(explosion_larger);
            explosionBigTextures.add(explosion_large);
            explosionBigTextures.add(explosion_medium);
            explosionBigTextures.add(explosion_small);

            explosion_animation = new Animation(Constants.EXPLOSION_DURATION, explosionTextures, Animation.PlayMode.NORMAL);
            explosion_big_animation = new Animation(Constants.EXPLOSION_BIG_DURATION, explosionBigTextures, Animation.PlayMode.NORMAL);
        }
    }


    public class PowerupAssets {
        public TextureAtlas.AtlasRegion powerup;
        public TextureAtlas.AtlasRegion powerup2;

        public PowerupAssets(TextureAtlas atlas) {
            powerup = atlas.findRegion(Constants.POWERUP);
            powerup2 = atlas.findRegion(Constants.POWERUP2);
        }

    }


    public class ExitPortalAssets {

        public Animation exit_portal_animation;


        public ExitPortalAssets(TextureAtlas atlas) {
            final TextureAtlas.AtlasRegion exitPortal1 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_1);
            final TextureAtlas.AtlasRegion exitPortal2 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_2);
            final TextureAtlas.AtlasRegion exitPortal3 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_3);
            final TextureAtlas.AtlasRegion exitPortal4 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_4);
            final TextureAtlas.AtlasRegion exitPortal5 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_5);
            final TextureAtlas.AtlasRegion exitPortal6 = atlas.findRegion(Constants.EXIT_PORTAL_SPRITE_6);

            Array<TextureAtlas.AtlasRegion> exitPortalFrames = new Array<>();
            exitPortalFrames.add(exitPortal1);
            exitPortalFrames.add(exitPortal2);
            exitPortalFrames.add(exitPortal3);
            exitPortalFrames.add(exitPortal4);
            exitPortalFrames.add(exitPortal5);
            exitPortalFrames.add(exitPortal6);

            exit_portal_animation = new Animation(Constants.EXIT_PORTAL_DURATION, exitPortalFrames, Animation.PlayMode.LOOP_PINGPONG);
        }
    }


    public class OnscreenControlsAssets {
        public TextureAtlas.AtlasRegion move_left;
        public TextureAtlas.AtlasRegion move_right;
        public TextureAtlas.AtlasRegion jump;
        public TextureAtlas.AtlasRegion shoot;
        public TextureAtlas.AtlasRegion drop;

        public OnscreenControlsAssets(TextureAtlas atlas) {
            move_left = atlas.findRegion(Constants.MOVE_LEFT_BUTTON);
            move_right = atlas.findRegion(Constants.MOVE_RIGHT_BUTTON);
            jump = atlas.findRegion(Constants.JUMP_BUTTON);
            shoot = atlas.findRegion(Constants.SHOOT_BUTTON);
            drop = atlas.findRegion(Constants.DROP_BUTTON);
        }
    }


    public class DiamondAssets {
        public TextureAtlas.AtlasRegion diamond;

        public DiamondAssets(TextureAtlas atlas) {
            diamond = atlas.findRegion(Constants.DIAMOND);
        }
    }
}
package com.sixmoney.gigagal.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {

    // world
    public static final Color BG_COLOR = Color.SKY;
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 160;
    public static final float WINDOW_WIDTH = 900;
    public static final float WINDOW_HEIGHT = 480;
    public static final String TEXTURE_ATLAS = "images/packaged/gigagal.pack.atlas";
    public static final float GRAVITY = 1000f;
    public static final int CHASE_CAM_SPEED = 200;
    public static final String PREFERENCES_NAME = "gigagal";
    public static final String SKIN_PATH = "images/ParticleParkUI.json";
    public static final String SKIN_PATH2 = "images/gigaGalSkin/gigagal_custom_data.json";
    public static final String FONT_FILE = "font/dialog.fnt";
    public static final String VERSION = "0.1.4-alpha";

    // sprites
    public static final String STANDING_RIGHT = "standing-right";
    public static final String STANDING_LEFT = "standing-left";
    public static final String JUMPING_RIGHT = "jumping-right";
    public static final String JUMPING_LEFT = "jumping-left";
    public static final String WALK_1_RIGHT = "walk-1-right";
    public static final String WALK_1_LEFT = "walk-1-left";
    public static final String WALK_2_RIGHT = "walk-2-right";
    public static final String WALK_2_LEFT = "walk-2-left";
    public static final String WALK_3_RIGHT = "walk-3-right";
    public static final String WALK_3_LEFT = "walk-3-left";
    public static final String PLATFORM = "platform";
    public static final String PLATFORM_HARD = "platform-hard";
    public static final String PLATFORM_MEDIUM = "platform-medium";
    public static final String PLATFORM_BOUNCE = "platform-bounce";
    public static final String PLATFORM_LIGHT = "platform-light";
    public static final String ENEMY = "enemy";
    public static final String ENEMY2 = "enemy2";
    public static final String ENEMY3 = "enemy3";
    public static final String BOSS = "boss";
    public static final String BULLET = "bullet";
    public static final String BULLET2 = "bullet2";
    public static final String BULLET3 = "bullet3";
    public static final String BULLET4 = "bullet4";
    public static final String LAZER = "lazer";
    public static final String EXPLOSION_LARGE = "explosion-large";
    public static final String EXPLOSION_LARGER = "explosion-larger";
    public static final String EXPLOSION_LARGEST = "explosion-largest";
    public static final String EXPLOSION_MEDIUM = "explosion-medium";
    public static final String EXPLOSION_SMALL = "explosion-small";
    public static final String POWERUP = "powerup";
    public static final String POWERUP2 = "powerup2";
    public static final String POWERUP3 = "powerup3";
    public static final String POWERUP4 = "powerup4";
    public static final String EXIT_PORTAL_SPRITE_1 = "exit-portal-1";
    public static final String EXIT_PORTAL_SPRITE_2 = "exit-portal-2";
    public static final String EXIT_PORTAL_SPRITE_3 = "exit-portal-3";
    public static final String EXIT_PORTAL_SPRITE_4 = "exit-portal-4";
    public static final String EXIT_PORTAL_SPRITE_5 = "exit-portal-5";
    public static final String EXIT_PORTAL_SPRITE_6 = "exit-portal-6";
    public static final String DIAMOND = "diamond";
    public static final String PARTICLE = "particle";
    public static final String CLOUDS = "images/backgroundSprites/clouds.png";
    public static final String CLOUDS2 = "images/backgroundSprites/clouds2.png";
    public static final String CLOUDS3 = "images/backgroundSprites/clouds3.png";


    // gigagal
    public static final Vector2 GIGAGAL_EYE_POS = new Vector2(16, 24);
    public static final float GIGAGAL_EYE_HEIGHT = 16f;
    public static final float GIGAGAL_HEIGHT = 23f;
    public static final float GIGAGAL_MOVEMENT_SPEED = 100f;
    public static final float GIGAGAL_JUMP_SPEED = 250f;
    public static final float GIGAGAL_BOUNCE_JUMP_SPEED = 500f;
    public static final Vector2 GIGAGAL_KNOCKBACK_SPEED = new Vector2(250, 250);
    public static final float GIGAGAL_JUMP_DURATION = 0.15f;
    public static final float GIGAGAL_BOUNCE_JUMP_DURATION = 0.30f;
    public static final float GIGAGAL_STANCE_WIDTH = 8f;
    public static final float WALK_DURATION = 0.15f;
    public static final Vector2 GIGAGAL_BARREL_POS = new Vector2(12, -7);
    public static final int GIGAGAL_INIT_AMMO = 20;
    public static final int GIGAGAL_INIT_LIVES = 3;
    public static final float GIGAGAL_PARTICLE_DELAY = 0.2f;
    public static final float GIGAGAL_DEATH_DELAY = 0.5f;


    // ememy
    public static final Vector2 ENEMY_CENTER_POS = new Vector2(14, 22);
    public static final float ENEMY_SPEED = 20f;
    public static final float ENEMY_SPEED_CHARGE = 80f;
    public static final float ENEMY_BOB_APLITUDE = 2f;
    public static final float ENEMY_BOB_PERIOD = 3f;
    public static final float ENEMY_COLLISION_RADIUS = 15f;
    public static final float ENEMY_HIT_COLLISION_RADIUS = 17f;
    public static final int ENEMY_HEALTH = 5;
    public static final int ENEMY_BIG_HEALTH = 15;

    // boss
    public static final Vector2 BOSS_CENTER_POS = new Vector2(44, 44);
    public static final float BOSS_SPEED = 25f;
    public static final float BOSS_SPEED_CHARGE = 60f;
    public static final float BOSS_COLLISION_RADIUS = 28f;
    public static final float BOSS_HIT_COLLISION_RADIUS = 28f;
    public static final int BOSS_HEALTH = 300;
    public static final float BOSS_PHASE_TIME_PERIOD = 4;

    // platform
    public static final int PLATFORM_EDGE_LENGTH = 8;

    // bullet
    public static final Vector2 BULLET_CENTER = new Vector2(3, 2);
    public static final float BULLET_SPEED = 150f;
    public static final float BULLET_RAPID_FIRE_DELAY = 0.025f;
    public static final float LAZER_SHOOT_DELAY = 0.6f;
    public static final float LAZER_SHOOT_DELAY_BOSS = 0.1f;

    // explosions
    public static final Vector2 EXPLOSION_CENTER = new Vector2(14, 14);
    public static final float EXPLOSION_DURATION = 0.15f;
    public static final float EXPLOSION_BIG_DURATION = 0.15f;

    // powerup
    public static final Vector2 POWERUP_CENTER = new Vector2(7, 5);
    public static final int POWERUP_AMOUNT = 10;
    public static final int POWERUP_AMOUNT_RAPID = 50;
    public static final int POWERUP_AMOUNT_NUKE = 1;

    // diamond
    public static final Vector2 DIAMOND_SIZE = new Vector2(20, 15);

    //portal
    public static final Vector2 EXIT_PORTAL_CENTER = new Vector2(31, 31);
    public static float EXIT_PORTAL_DURATION = 0.1f;
    public static Vector2 EXIT_PORTAL_POSITION = new Vector2(20, 20);

    //level loading
    public static final String LEVEL_DIR = "levels";
    public static final String LEVEL_FILE_EXTENSION = ".dt";
    public static final String LEVEL_COMPOSITE = "composite";
    public static final String LEVEL_9PATCHES = "sImage9patchs";
    public static final String LEVEL_IMAGES = "sImages";
    public static final String LEVEL_ZINDEX = "zIndex";
    public static final String LEVEL_ERROR_MESSAGE = "There was a problem loading the level.";
    public static final String LEVEL_IMAGENAME_KEY = "imageName";
    public static final String LEVEL_X_KEY = "x";
    public static final String LEVEL_Y_KEY = "y";
    public static final String LEVEL_WIDTH_KEY = "width";
    public static final String LEVEL_HEIGHT_KEY = "height";
    public static final String LEVEL_IDENTIFIER_KEY = "itemIdentifier";
    public static final String LEVEL_CUSTOM_VARS = "customVars";
    public static final String LEVEL_ENEMY_TAG = "Enemy";
    public static final String LEVEL_ENEMY_BIG_TAG = "BigEnemy";
    public static final String LEVEL_ENEMY_RANGED_TAG = "RangedEnemy";
    public static final String LEVEL_BOSS_TAG = "Boss";
    public static final String LEVEL_KILLPLANE_TAG = "Killplane";
    public static final int LEVEL_1 = 1;
    public static final int LEVEL_2 = 2;
    public static final int LEVEL_3 = 3;
    public static final int LEVEL_4 = 4;
    public static final int LEVEL_5 = 5;
    public static final int LEVEL_6 = 6;
    public static final int LEVEL_7 = 7;
    public static final int LEVEL_8 = 8;
    public static final int LEVEL_9 = 9;
    public static final int LEVEL_10 = 10;
    public static final int MAX_LEVEL = 10;

    //HUD
    public static final float HUD_VIEWPORT_SIZE = 480;
    public static final float HUD_MARGIN = 20;
    public static final String HUD_AMMO_LABEL = "Ammo: ";
    public static final String HUD_AMMO_SPECIAL_LABEL = "Special: ";
    public static final String HUD_AMMO_RAPID_LABEL = "Rapid: ";
    public static final String HUD_SCORE_LABEL = "Score: ";

    // Scoring
    public static final int ENEMY_KILL_SCORE = 50;
    public static final int ENEMY_HIT_SCORE = 10;
    public static final int POWERUP_SCORE = 100;
    public static final int AMMO_SCORE = 25;
    public static final int AMMO_SPECIAL_SCORE = 50;
    public static final int AMMO_RAPID_SCORE = 10;
    public static final int AMMO_NUKE_SCORE = 1000;
    public static final int DIAMOND_SCORE = 1000;

    // Victory/Game Over/Pause screens
    public static final float LEVEL_END_DURATION = 2;
    public static final float LEVEL_END_BLOCK = 1;
    public static final String VICTORY_MESSAGE = "You are the Winner!";
    public static final String VICTORY_SCORE = "Score: ";
    public static final String GAME_OVER_MESSAGE = "Game Over, Gal";
    public static final int EXPLOSION_COUNT = 200;
    public static final String PAUSED_MESSAGE = "PAUSED";
    public static final String RESUME_MESSAGE = "RESUME";
    public static final String QUIT_MESSAGE = "QUIT";
    public static final String RESTART_MESSAGE = "RESTART";
    public static final String NEXT_LEVEL_MESSAGE = "NEXT LEVEL";

    // Onscreen Controls
    public static final float ONSCREEN_CONTROLS_VIEWPORT_SIZE = 200;
    public static final String MOVE_LEFT_BUTTON = "button-move-left";
    public static final String MOVE_RIGHT_BUTTON = "button-move-right";
    public static final String SHOOT_BUTTON = "button-shoot";
    public static final String JUMP_BUTTON = "button-jump";
    public static final String DROP_BUTTON = "button-drop";
    public static final Vector2 BUTTON_CENTER = new Vector2(15, 15);
    public static final float BUTTON_RADIUS = 24;

    // Music and sounds
    public static final String MUSIC_PATH = "music/music_zapsplat_game_music_arcade_electro_repeating_retro_arp_electro_drums_serious_012.mp3";
    public static final String GUNSHOT1_PATH = "sounds/zapsplat_science_fiction_blaster_gun_single_shot_001_62523.mp3";
    public static final String GUNSHOT2_PATH = "sounds/zapsplat_science_fiction_blaster_gun_single_shot_002_62524.mp3";
    public static final String GUNSHOT3_PATH = "sounds/zapsplat_science_fiction_blaster_gun_single_shot_003_62525.mp3";
    public static final String GUNSHOT4_PATH = "sounds/zapsplat_science_fiction_blaster_gun_single_shot_004_62526.mp3";
    public static final String LAZER_PATH = "sounds/science_fiction_laser_002.mp3";
    public static final String EXPLOSION1_PATH = "sounds/zapsplat_explosions_large_explosion_003_63093.mp3";
    public static final String EXPLOSION2_PATH = "sounds/zapsplat_explosions_large_explosion_005_63095.mp3";
    public static final String DEATH_SOUND_PATH = "sounds/esm_8bit_splat_explosion_bomb_boom_blast_cannon_retro_old_school_classic_cartoon.mp3";
    public static final String JUMP_SOUND_PATH = "sounds/zapsplat_multimedia_game_sound_classic_jump_003_41724.mp3";
    public static final String RUNNING_SOUND_PATH = "sounds/zapsplat_lego_person_running_001_25160.mp3";
    public static final String COLLECT_DIAMOND_PATH = "sounds/zapsplat_multimedia_game_sound_coins_collect_several_at_once_001_40812.mp3";
    public static final String COLLECT_POWERUP_PATH = "sounds/zapsplat_multimedia_game_sound_coin_collect_clink_002_54060.mp3";
    public static final String WIN_EFFECT_PATH = "sounds/little_robot_sound_factory_Jingle_Win_00.mp3";
    public static final String LOSE_EFFECT_PATH = "sounds/multimedia_game_sound_retro_lose_tone_002_52984.mp3";
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums;
import com.sixmoney.gigagal.utils.SoundManager;
import com.sixmoney.gigagal.utils.Utils;

import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Boss {
    private final Level level;
    private final long start_time;
    private Enums.Direction direction;
    private final boolean ranged;
    private final float random_phase;
    private boolean charging;
    private final float shootDelayTime;
    private long shootStartTime;
    private final Platform platform;
    private float speed;
    private final float speedCharge;

    public float health;
    public Vector2 position;
    public Rectangle enemy_bounding_box;
    public boolean onScreen;

    public Boss(Platform platform, Level level) {
        this.platform = platform;
        this.level = level;
        position = new Vector2(MathUtils.random(platform.left, platform.left + platform.width), platform.top + Constants.BOSS_CENTER_POS.y);
        if (MathUtils.randomBoolean()) {
            direction = Enums.Direction.LEFT;
        } else {
            direction = Enums.Direction.RIGHT;
        }
        start_time = TimeUtils.nanoTime();
        this.health = Constants.BOSS_HEALTH;
        ranged = true;
        random_phase = MathUtils.random();
        charging = false;
        onScreen = false;
        if (level.difficultly == 0f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS;
            speed = Constants.BOSS_SPEED;
            speedCharge = Constants.ENEMY_SPEED_CHARGE;
        } else if (level.difficultly == 50f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS * 0.8f;
            speed = Constants.BOSS_SPEED * 1.3f;
            speedCharge = Constants.ENEMY_SPEED_CHARGE * 1.3f;
        } else {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS * 0.5f;
            speed = Constants.BOSS_SPEED * 2f;
            speedCharge = Constants.ENEMY_SPEED_CHARGE * 2f;
        }
        speed = MathUtils.random(speed, speed * 1.5f);

        shootStartTime = TimeUtils.nanoTime();
        enemy_bounding_box = new Rectangle(
                position.x - Constants.BOSS_COLLISION_RADIUS,
                position.y - Constants.BOSS_COLLISION_RADIUS,
                Constants.BOSS_COLLISION_RADIUS * 2,
                Constants.BOSS_COLLISION_RADIUS * 2
        );
    }

    public void update(float delta) {
        if (platform.hasPlayer) {
            if (onScreen) {
                charging = false;
            } else {
                charging = false;
                if (platform.playerPosition < position.x - platform.left) {
                    direction = Enums.Direction.LEFT;
                } else {
                    direction = Enums.Direction.RIGHT;
                }
            }
            if (Utils.secondsSince(shootStartTime) > Constants.LAZER_SHOOT_DELAY_BOSS) {
                shootStartTime = TimeUtils.nanoTime();
                shoot();
            }
        } else {
            charging = false;
        }
        move(delta);

        float elapsed_time = Utils.secondsSince(start_time);
        float bob_multiplier = 1 + sin(PI2 * elapsed_time / Constants.ENEMY_BOB_PERIOD) + random_phase;
        position.y = platform.top  + Constants.ENEMY_CENTER_POS.y + Constants.ENEMY_BOB_APLITUDE * bob_multiplier;

        enemy_bounding_box.x = position.x - Constants.BOSS_COLLISION_RADIUS;
        enemy_bounding_box.y = position.y - Constants.BOSS_COLLISION_RADIUS;
    }


    private void move(float delta) {
        float move_distance;
        if (charging) {
            move_distance = delta * speedCharge;
        } else {
            move_distance = delta * speed;
        }
        if (direction == Enums.Direction.LEFT) {
            position.x -= move_distance;
        } else {
            position.x += move_distance;
        }

        if (position.x < platform.left) {
            direction = Enums.Direction.RIGHT;
            position.x = platform.left;
        } else if (position.x > platform.left + platform.width) {
            direction = Enums.Direction.LEFT;
            position.x = platform.left + platform.width;
        }
    }

    public void shoot() {
        SoundManager.get_instance().playSound(Constants.LAZER_PATH);
        Enums.Direction shootDirection;
        if (platform.playerPosition < position.x - platform.left) {
            shootDirection = Enums.Direction.LEFT;
        } else {
            shootDirection = Enums.Direction.RIGHT;
        }
        Lazer lazer = new Lazer(level, new Vector2(position.x, position.y - Constants.ENEMY_CENTER_POS.y / 2), shootDirection);
        lazer.travelPastScreen = true;
        level.getBullets().add(lazer);
    }

    public void render(SpriteBatch spriteBatch) {
        if (charging && direction == Enums.Direction.LEFT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.boss, position.x - Constants.BOSS_CENTER_POS.x, position.y - Constants.BOSS_CENTER_POS.y, 30);
        } else if (charging && direction == Enums.Direction.RIGHT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.boss, position.x - Constants.BOSS_CENTER_POS.x, position.y - Constants.BOSS_CENTER_POS.y, -30);
        } else {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.boss, position.x - Constants.BOSS_CENTER_POS.x, position.y - Constants.BOSS_CENTER_POS.y);
        }
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                enemy_bounding_box.x,
                enemy_bounding_box.y,
                enemy_bounding_box.width,
                enemy_bounding_box.height
        );
    }
}

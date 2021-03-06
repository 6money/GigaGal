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
import com.sixmoney.gigagal.utils.Enums.Direction;
import com.sixmoney.gigagal.utils.Utils;

import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Enemy {
    private final float random_phase;

    protected Platform platform;
    protected Direction direction;
    protected float health;
    protected long start_time;
    protected float speed;
    protected float speedCharge;
    protected boolean charging;
    protected boolean ranged;
    protected Level level;
    protected long shootStartTime;
    protected float shootDelayTime;

    public Vector2 position;
    public Rectangle enemy_bounding_box;
    public boolean onScreen;


    public Enemy(Platform platform, Level level) {
        this.platform = platform;
        this.level = level;
        position = new Vector2(MathUtils.random(platform.left, platform.left + platform.width), platform.top + Constants.ENEMY_CENTER_POS.y);
        if (MathUtils.randomBoolean()) {
            direction = Direction.LEFT;
        } else {
            direction = Direction.RIGHT;
        }
        start_time = TimeUtils.nanoTime();
        this.health = Constants.ENEMY_HEALTH;
        ranged = false;
        random_phase = MathUtils.random();
        charging = false;
        onScreen = false;
        if (level.difficultly == 0f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY;
            speed = Constants.ENEMY_SPEED;
            speedCharge = Constants.ENEMY_SPEED_CHARGE;
        } else if (level.difficultly == 50f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY * 0.8f;
            speed = Constants.ENEMY_SPEED * 1.3f;
            speedCharge = Constants.ENEMY_SPEED_CHARGE * 1.3f;
        } else {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY * 0.5f;
            speed = Constants.ENEMY_SPEED * 2f;
            speedCharge = Constants.ENEMY_SPEED_CHARGE * 2f;
        }
        speed = MathUtils.random(speed, speed * 1.5f);

        shootStartTime = TimeUtils.nanoTime();
        enemy_bounding_box = new Rectangle(
                position.x - Constants.ENEMY_COLLISION_RADIUS,
                position.y - Constants.ENEMY_COLLISION_RADIUS,
                Constants.ENEMY_COLLISION_RADIUS * 2,
                Constants.ENEMY_COLLISION_RADIUS * 2
        );
    }


    public float getHealth() {
        return health;
    }


    public void setHealth(float health) {
        this.health = health;
    }


    public void update(float delta) {
        if (platform.hasPlayer) {
            if (ranged && onScreen) {
                charging = false;
                if (Utils.secondsSince(shootStartTime) > Constants.LAZER_SHOOT_DELAY) {
                    shootStartTime = TimeUtils.nanoTime();
                    shoot();
                }
            } else if (!ranged) {
                charging = true;
                if (platform.playerPosition < position.x - platform.left) {
                    direction = Direction.LEFT;
                } else {
                    direction = Direction.RIGHT;
                }
            }
        } else {
            charging = false;
        }
        move(delta);

        float elapsed_time = Utils.secondsSince(start_time);
        float bob_multiplier = 1 + sin(PI2 * elapsed_time / Constants.ENEMY_BOB_PERIOD) + random_phase;
        position.y = platform.top  + Constants.ENEMY_CENTER_POS.y + Constants.ENEMY_BOB_APLITUDE * bob_multiplier;

        enemy_bounding_box.x = position.x - Constants.ENEMY_COLLISION_RADIUS;
        enemy_bounding_box.y = position.y - Constants.ENEMY_COLLISION_RADIUS;
    }


    private void move(float delta) {
        float move_distance;
        if (charging) {
            move_distance = delta * speedCharge;
        } else {
            move_distance = delta * speed;
        }
        if (direction == Direction.LEFT) {
            position.x -= move_distance;
        } else {
            position.x += move_distance;
        }

        if (position.x < platform.left) {
            direction = Direction.RIGHT;
            position.x = platform.left;
        } else if (position.x > platform.left + platform.width) {
            direction = Direction.LEFT;
            position.x = platform.left + platform.width;
        }
    }


    public void render(SpriteBatch spriteBatch) {
        if (charging && direction == Direction.LEFT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, 30);
        } else if (charging && direction == Direction.RIGHT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, -30);
        } else {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y);
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


    public void shoot() {
    }
}

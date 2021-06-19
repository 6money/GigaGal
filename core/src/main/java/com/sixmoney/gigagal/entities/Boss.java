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
import com.sixmoney.gigagal.utils.SoundManager;
import com.sixmoney.gigagal.utils.Utils;

public class Boss {
    private final Level level;
    private Vector2 directionVector;
    private final float random_phase;
    private boolean charging;
    private final float shootDelayTime;
    private long shootStartTime;
    private long attackPhaseStartTime;
    private float speed;
    private final float speedCharge;

    public float health;
    public Vector2 position;
    public Rectangle enemy_bounding_box;
    public boolean onScreen;
    private float rotation;

    public Boss(Platform platform, Level level) {
        this.level = level;
        position = new Vector2(MathUtils.random(platform.left, platform.left + platform.width), platform.top + Constants.BOSS_CENTER_POS.y);
        this.health = Constants.BOSS_HEALTH;
        random_phase = MathUtils.random();
        charging = false;
        onScreen = false;
        directionVector = new Vector2();
        attackPhaseStartTime = TimeUtils.nanoTime();
        rotation = 0;
        if (level.difficultly == 0f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS;
            speed = Constants.BOSS_SPEED;
            speedCharge = Constants.BOSS_SPEED_CHARGE;
        } else if (level.difficultly == 50f) {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS * 0.8f;
            speed = Constants.BOSS_SPEED * 1.3f;
            speedCharge = Constants.BOSS_SPEED_CHARGE * 1.3f;
        } else {
            shootDelayTime = Constants.LAZER_SHOOT_DELAY_BOSS * 0.5f;
            speed = Constants.BOSS_SPEED * 2f;
            speedCharge = Constants.BOSS_SPEED_CHARGE * 2f;
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
        float move_distance;

        if (Utils.secondsSince(attackPhaseStartTime) > Constants.BOSS_PHASE_TIME_PERIOD) {
            attackPhaseStartTime = TimeUtils.nanoTime();
            charging = !charging;
        }

        if (!charging) {
            rotation = 0;
            move_distance = delta * speed;
            if (Utils.secondsSince(shootStartTime) > Constants.LAZER_SHOOT_DELAY_BOSS) {
                shootStartTime = TimeUtils.nanoTime();
                shoot();
            }
        } else {
            rotation += 2880 * delta;
            move_distance = delta * speedCharge;
        }

        Vector2 gigagalPos = new Vector2(level.gigaGal.position);
        directionVector = gigagalPos.sub(position).nor();
        Vector2 movementVector = new Vector2(directionVector.x * move_distance, directionVector.y * move_distance);
        position.add(movementVector);

//        float elapsed_time = Utils.secondsSince(start_time);
//        float bob_multiplier = 1 + sin(PI2 * elapsed_time / Constants.ENEMY_BOB_PERIOD) + random_phase;
//        position.y = position.y + (Constants.ENEMY_BOB_APLITUDE * bob_multiplier);

        enemy_bounding_box.x = position.x - Constants.BOSS_COLLISION_RADIUS;
        enemy_bounding_box.y = position.y - Constants.BOSS_COLLISION_RADIUS;
    }

    public void shoot() {
        SoundManager.get_instance().playSound(Constants.LAZER_PATH);
        Lazer lazer = new Lazer(level, new Vector2(position.x, position.y), directionVector);
        lazer.travelPastScreen = true;
        level.getBullets().add(lazer);
    }

    public void render(SpriteBatch spriteBatch) {
        if (charging) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.boss, position.x - Constants.BOSS_CENTER_POS.x, position.y - Constants.BOSS_CENTER_POS.y, rotation);
        } else {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.boss, position.x - Constants.BOSS_CENTER_POS.x, position.y - Constants.BOSS_CENTER_POS.y, 0);
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

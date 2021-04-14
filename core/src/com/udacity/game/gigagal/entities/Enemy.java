package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Utils;

import static com.badlogic.gdx.math.MathUtils.PI2;
import static com.badlogic.gdx.math.MathUtils.sin;

public class Enemy {
    private final float random_phase;

    private Platform platform;
    private Direction direction;
    private int health;
    private long start_time;

    public Vector2 position;

    public Enemy(Platform platform, int health) {
        this.platform = platform;
        position = new Vector2(platform.left, platform.top + Constants.ENEMY_CENTER_POS.y);
        direction = Direction.RIGHT;
        start_time = TimeUtils.nanoTime();
        this.health = health;
        random_phase = MathUtils.random();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void update(float delta) {
        float move_distance = delta * Constants.ENEMY_SPEED;
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

        float elapsed_time = Utils.secondsSince(start_time);
        float bob_multiplier = 1 + sin(PI2 * elapsed_time / Constants.ENEMY_BOB_PERIOD) + random_phase;
        position.y = platform.top  + Constants.ENEMY_CENTER_POS.y + Constants.ENEMY_BOB_APLITUDE * bob_multiplier;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.enemyAssets.enemy, position.x  - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y);
    }

    private enum Direction{LEFT, RIGHT}
}

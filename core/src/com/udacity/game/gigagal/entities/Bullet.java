package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.udacity.game.gigagal.Level;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Enums.Direction;

public class Bullet {
    protected Direction direction;
    protected Vector2 position;
    protected Level level;
    protected int damage;

    public boolean active;

    public Bullet(Level level, Vector2 position, Direction direction) {
        this.level = level;
        this.position = position;
        this.direction = direction;
        active = true;
        damage = 1;
    }

    public void update(float delta) {
        float movement_amount = delta * Constants.BULLET_SPEED;

        if (direction == Direction.LEFT) {
            position.x -= movement_amount;
        } else {
            position.x += movement_amount;
        }

        for (Enemy enemy: level.getEnemies()) {
            Vector2 bullet_center = new Vector2(position.x + Constants.BULLET_CENTER.x, position.y + Constants.BULLET_CENTER.y);
            if (bullet_center.dst(enemy.position) < Constants.ENEMY_HIT_COLLISION_RADIUS) {
                playExplosion();
                add_explosion(bullet_center);
                active = false;
                enemy.setHealth(enemy.getHealth() - damage);
                level.score += Constants.ENEMY_HIT_SCORE;
            }
        }

        float viewport_width = level.getViewport().getWorldWidth();
        Vector3 viewport_position = level.getViewport().getCamera().position;

        if (position.x < viewport_position.x - viewport_width / 2 || position.x > viewport_position.x + viewport_width / 2) {
            active = false;
        }
    }

    public void playExplosion() {
        Sound explosion;
        int explosionid = MathUtils.random(1, 2);
        switch (explosionid) {
            case 1:
                explosion = Assets.instance.soundAssets.explosion1;
                break;
            case 2:
                explosion = Assets.instance.soundAssets.explosion2;
                break;
            default:
                explosion = Assets.instance.soundAssets.explosion1;
                break;
        }
        long effectid = explosion.play();
        explosion.setVolume(effectid, 0.4f);
    }

    public void add_explosion(Vector2 bullet_center) {
        level.getExplosions().add(new Explosion(bullet_center));
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.bulletAssets.bullet, position.x, position.y - Constants.BULLET_CENTER.y);
    }


}

package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.udacity.game.gigagal.Level;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Enums;

public class BigBullet extends Bullet {
    public BigBullet(Level level, Vector2 position, Enums.Direction direction) {
        super(level, position, direction);
        damage = 2;
    }

    @Override
    public void add_explosion(Vector2 bullet_center) {
        level.getExplosions().add(new ExplosionBig(bullet_center));
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.bulletAssets.bullet2, position.x, position.y - Constants.BULLET_CENTER.y);
    }
}

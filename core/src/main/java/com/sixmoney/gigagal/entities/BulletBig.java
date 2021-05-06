package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums.*;

public class BulletBig extends Bullet {
    public BulletBig(Level level, Vector2 position, Direction direction) {
        super(level, position, direction);
        damage = 2;
    }

    @Override
    public void add_explosion(Vector2 bullet_center) {
        level.getExplosions().add(new ExplosionBig(bullet_center));
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.get_instance().bulletAssets.bullet2, position.x, position.y - Constants.BULLET_CENTER.y);
        particleBulletTrail.draw(spriteBatch);
    }
}

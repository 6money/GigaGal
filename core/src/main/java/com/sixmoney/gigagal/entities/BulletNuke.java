package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums;

public class BulletNuke extends Bullet {

    public BulletNuke(Level level, Vector2 position, Enums.Direction direction) {
        super(level, position, direction);
        damage = 100;
    }

    @Override
    public void add_explosion(Vector2 bullet_center) {
        ExplosionBig explosionBig = new ExplosionBig(bullet_center);
        explosionBig.setScale(5);
        level.getExplosions().add(explosionBig);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.get_instance().bulletAssets.bullet4, position.x, position.y - Constants.BULLET_CENTER.y);
        renderParticleTrail(spriteBatch);
    }
}

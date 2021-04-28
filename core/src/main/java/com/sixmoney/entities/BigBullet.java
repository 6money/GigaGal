package com.sixmoney.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.Level;
import com.sixmoney.utils.Assets;
import com.sixmoney.utils.Constants;
import com.sixmoney.utils.Enums.*;

public class BigBullet extends Bullet {
    public BigBullet(Level level, Vector2 position, Direction direction) {
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

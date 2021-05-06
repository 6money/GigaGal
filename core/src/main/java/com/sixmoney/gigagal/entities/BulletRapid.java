package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums;

public class BulletRapid extends Bullet {
    public BulletRapid(Level level, Vector2 position, Enums.Direction direction) {
        super(level, position, direction);
        damage = 0.25f;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.get_instance().bulletAssets.bullet3, position.x, position.y - Constants.BULLET_CENTER.y);
        particleBulletTrail.draw(spriteBatch);
    }
}

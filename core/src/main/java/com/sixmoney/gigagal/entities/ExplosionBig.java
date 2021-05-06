package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class ExplosionBig extends Explosion {

    public ExplosionBig(Vector2 position) {
        super(position);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (!isFinished() && !yetToStart()) {
            TextureRegion key_frame = (TextureRegion) Assets.get_instance().explosionAssets.explosion_big_animation.getKeyFrame(Utils.secondsSince(start_time) - offset);
            Utils.drawTextureRegion(spriteBatch, key_frame, position.x - Constants.EXPLOSION_CENTER.x, position.y - Constants.EXPLOSION_CENTER.y);
        }
    }

    @Override
    public boolean isFinished() {
        return Assets.get_instance().explosionAssets.explosion_big_animation.isAnimationFinished(Utils.secondsSince(start_time) - offset);
    }
}

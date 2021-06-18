package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    public void render(Batch spriteBatch) {
        if (!isFinished() && !yetToStart()) {
            TextureRegion key_frame = (TextureRegion) Assets.get_instance().explosionAssets.explosion_big_animation.getKeyFrame(Utils.secondsSince(start_time) - offset);
            spriteBatch.draw(
                    key_frame.getTexture(),
                    position.x - Constants.EXPLOSION_CENTER.x,
                    position.y - Constants.EXPLOSION_CENTER.y,
                    Constants.EXPLOSION_CENTER.x,
                    Constants.EXPLOSION_CENTER.y,
                    key_frame.getRegionWidth(),
                    key_frame.getRegionHeight(),
                    scale,
                    scale,
                    0,
                    key_frame.getRegionX(),
                    key_frame.getRegionY(),
                    key_frame.getRegionWidth(),
                    key_frame.getRegionHeight(),
                    false,
                    false);
        }
    }

    @Override
    public boolean isFinished() {
        return Assets.get_instance().explosionAssets.explosion_big_animation.isAnimationFinished(Utils.secondsSince(start_time) - offset);
    }
}

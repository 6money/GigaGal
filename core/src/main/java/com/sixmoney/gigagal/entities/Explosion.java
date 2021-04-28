package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class Explosion {
    protected Vector2 position;
    protected long start_time;

    public float offset;

    public Explosion(Vector2 position) {
        this.position = position;
        start_time = TimeUtils.nanoTime();
        this.offset = 0;
    }

    public void render(SpriteBatch spriteBatch) {
        if (!isFinished() && !yetToStart()) {
            TextureRegion key_frame = (TextureRegion) Assets.instance.explosionAssets.explosion_animation.getKeyFrame(Utils.secondsSince(start_time) - offset);
            Utils.drawTextureRegion(spriteBatch, key_frame, position.x - Constants.EXPLOSION_CENTER.x, position.y - Constants.EXPLOSION_CENTER.y);
        }
    }

    public boolean yetToStart(){
        return Utils.secondsSince(start_time) - offset < 0;
    }

    public boolean isFinished() {
        return Assets.instance.explosionAssets.explosion_animation.isAnimationFinished(Utils.secondsSince(start_time) - offset);
    }
}

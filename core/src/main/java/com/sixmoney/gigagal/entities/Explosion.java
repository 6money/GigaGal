package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class Explosion {
    protected Vector2 position;
    protected long start_time;
    protected float scale;

    public float offset;

    public Explosion(Vector2 position) {
        this.position = position;
        start_time = TimeUtils.nanoTime();
        this.offset = 0;
        scale = 1;
    }

    public void render(Batch spriteBatch) {
        this.render(spriteBatch, 1f);
    }

    public void render(Batch spriteBatch, float scale) {
        if (!isFinished() && !yetToStart()) {
            TextureRegion key_frame = (TextureRegion) Assets.get_instance().explosionAssets.explosion_animation.getKeyFrame(Utils.secondsSince(start_time) - offset);
            Utils.drawTextureRegion(spriteBatch, key_frame, position.x - Constants.EXPLOSION_CENTER.x, position.y - Constants.EXPLOSION_CENTER.y, 0f, scale);
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean yetToStart(){
        return Utils.secondsSince(start_time) - offset < 0;
    }

    public boolean isFinished() {
        return Assets.get_instance().explosionAssets.explosion_animation.isAnimationFinished(Utils.secondsSince(start_time) - offset);
    }
}

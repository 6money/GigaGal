package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.gigagal.utils.Assets;

public class PlatformBounce extends Platform {

    public PlatformBounce(float left, float top, float width, float height) {
        super(left, top, width, height);
        solid = false;
        droppable = false;
        bounce = true;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.get_instance().platformAssets.ninePatch_platform_bounce.draw(spriteBatch, left, bottom, width, height);
    }
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.gigagal.utils.Assets;

public class PlatformMedium extends Platform {
    public PlatformMedium(float left, float top, float width, float height) {
        super(left, top, width, height);
        droppable = false;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.get_instance().platformAssets.ninePatch_platform_medium.draw(spriteBatch, left, bottom, width, height);
    }
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.gigagal.utils.Assets;

public class PlatformHard extends Platform {

    public PlatformHard(float left, float top, float width, float height) {
        super(left, top, width, height);
        solid = true;
        droppable = false;
    }

    public PlatformHard(float left, float top, float width, float height, int zIndex) {
        super(left, top, width, height, zIndex);
        solid = true;
        droppable = false;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.get_instance().platformAssets.ninePatch_platform_hard.draw(spriteBatch, left, bottom, width, height);
    }
}

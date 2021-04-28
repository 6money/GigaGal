package com.sixmoney.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.utils.Assets;

public class PlatformHard extends Platform {

    public PlatformHard(float left, float top, float width, float height) {
        super(left, top, width, height);
        solid = true;
        droppable = false;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.instance.platformAssets.ninePatch_platform_hard.draw(spriteBatch, left, bottom, width, height);
    }
}

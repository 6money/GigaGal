package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.udacity.game.gigagal.utils.Assets;

public class PlatformHard extends Platform {

    public PlatformHard(float left, float top, float width, float height) {
        super(left, top, width, height);
        solid = true;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.instance.platformAssets.ninePatch_platform_hard.draw(spriteBatch, left, bottom, width, height);
    }
}

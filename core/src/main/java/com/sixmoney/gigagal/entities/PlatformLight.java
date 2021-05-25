package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlatformLight extends Platform {

    public PlatformLight(float left, float top, float width, float height) {
        super(left, top, width, height);
        invisible = true;
    }

    public PlatformLight(float left, float top, float width, float height, int zIndex) {
        super(left, top, width, height, zIndex);
        invisible = true;
    }

    public void render(SpriteBatch spriteBatch) {
    }
}

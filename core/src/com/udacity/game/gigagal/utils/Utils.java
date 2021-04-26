package com.udacity.game.gigagal.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public class Utils {
    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y) {
        drawTextureRegion(batch, region, x, y, 0f);
    }

    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, Vector2 position, Vector2 offset) {
        drawTextureRegion(batch, region, position.x - offset.x, position.y - offset.y);
    }

    public static void drawTextureRegion(SpriteBatch batch, TextureRegion region, float x, float y, float rotation) {
        batch.draw(
                region.getTexture(),
                x,
                y,
                region.getRegionWidth() / 2f,
                region.getRegionHeight() / 2f,
                region.getRegionWidth(),
                region.getRegionHeight(),
                1,
                1,
                rotation,
                region.getRegionX(),
                region.getRegionY(),
                region.getRegionWidth(),
                region.getRegionHeight(),
                false,
                false);
    }

    public static float secondsSince(long timeNanos) {
        return MathUtils.nanoToSec * (TimeUtils.nanoTime() - timeNanos);
    }
}

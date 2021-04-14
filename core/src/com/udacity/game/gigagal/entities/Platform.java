package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.udacity.game.gigagal.utils.Assets;

public class Platform implements Comparable<Platform> {
    public float top;
    public float bottom;
    public float left;
    public float right;
    public float width;
    public float height;

    public Platform(float left, float top, float width, float height) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
        this.width = width;
        this.height = height;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, left -1, bottom -1, width + 2, height + 2);
    }

    @Override
    public int compareTo(Platform platform) {
        return Float.compare(this.top, platform.top);
    }

    @Override
    public String toString() {
        return "Location: x:" + left + " y: " + bottom + " width: " + width + " height: " + height;
    }
}

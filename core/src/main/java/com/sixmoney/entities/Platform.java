package com.sixmoney.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.utils.Assets;

public class Platform implements Comparable<Platform> {
    public float top;
    public float bottom;
    public float left;
    public float right;
    public float width;
    public float height;
    public boolean solid;
    public boolean droppable;
    public boolean hasPlayer;
    public float playerPosition;

    public Platform(float left, float top, float width, float height) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
        this.width = width;
        this.height = height;
        solid = false;
        droppable = true;
        hasPlayer = false;
        playerPosition = 0;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.instance.platformAssets.ninePatch_platform.draw(spriteBatch, left, bottom, width, height);
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

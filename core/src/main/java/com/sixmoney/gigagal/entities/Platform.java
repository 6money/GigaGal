package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.gigagal.utils.Assets;

public class Platform implements Comparable<Platform> {
    public float top;
    public float bottom;
    public float left;
    public float right;
    public float width;
    public float height;
    public int zIndex;
    public boolean solid;
    public boolean droppable;
    public boolean bounce;
    public boolean hasPlayer;
    public float playerPosition;

    public Platform(float left, float top, float width, float height) {
        new Platform(left, top, width, height, 0);
    }

    public Platform(float left, float top, float width, float height, int zIndex) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
        solid = false;
        droppable = true;
        bounce = false;
        hasPlayer = false;
        playerPosition = 0;
    }

    public void render(SpriteBatch spriteBatch) {
        Assets.get_instance().platformAssets.ninePatch_platform.draw(spriteBatch, left, bottom, width, height);
    }

    @Override
    public int compareTo(Platform platform) {
        return Float.compare(platform.zIndex, this.zIndex);
    }

    @Override
    public String toString() {
        return "Location: x:" + left + " y: " + bottom + " width: " + width + " height: " + height + " zIndex: " + zIndex;
    }
}

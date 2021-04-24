package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;

public class Diamond {
    public Vector2 position;

    public Diamond(Vector2 pos) {
        position = pos;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.diamondAssets.diamond, position.x, position.y);
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(position.x, position.y, Constants.DIAMOND_SIZE.x, Constants.DIAMOND_SIZE.y);
    }
}

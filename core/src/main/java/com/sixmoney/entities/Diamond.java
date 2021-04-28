package com.sixmoney.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.utils.Assets;
import com.sixmoney.utils.Constants;

public class Diamond {
    public Vector2 position;
    public Rectangle diamond_bounding_box;

    public Diamond(Vector2 pos) {
        position = pos;
        diamond_bounding_box = new Rectangle(
                position.x,
                position.y,
                Constants.DIAMOND_SIZE.x,
                Constants.DIAMOND_SIZE.y
        );
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.diamondAssets.diamond, position.x, position.y);
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(diamond_bounding_box.x, diamond_bounding_box.y, diamond_bounding_box.width, diamond_bounding_box.height);
    }
}

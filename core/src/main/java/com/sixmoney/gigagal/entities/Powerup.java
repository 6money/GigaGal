package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class Powerup {
    protected Vector2 position;

    public String ammo_type;
    public Rectangle powerup_bounding_box;

    public Powerup(Vector2 position) {
        this.position = position;
        powerup_bounding_box = new Rectangle(
                position.x - Constants.POWERUP_CENTER.x,
                position.y - Constants.POWERUP_CENTER.y,
                Constants.POWERUP_CENTER.x * 2,
                Constants.POWERUP_CENTER.y * 2
        );
        ammo_type = "basic";
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.powerupAssets.powerup, position.x - Constants.POWERUP_CENTER.x, position.y - Constants.POWERUP_CENTER.y);
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                powerup_bounding_box.x,
                powerup_bounding_box.y,
                powerup_bounding_box.width,
                powerup_bounding_box.height
        );
    }
}

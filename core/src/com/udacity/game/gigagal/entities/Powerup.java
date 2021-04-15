package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;

public class Powerup {
    protected Vector2 position;

    public String ammo_type;

    public Powerup(Vector2 position) {
        this.position = position;
        ammo_type = "basic";
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.powerupAssets.powerup, position.x - Constants.POWERUP_CENTER.x, position.y - Constants.POWERUP_CENTER.y);
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                position.x - Constants.POWERUP_CENTER.x,
                position.y - Constants.POWERUP_CENTER.y,
                Constants.POWERUP_CENTER.x * 2,
                Constants.POWERUP_CENTER.y * 2
        );
    }
}

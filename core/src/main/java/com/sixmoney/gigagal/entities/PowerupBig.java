package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class PowerupBig extends Powerup {
    public PowerupBig(Vector2 position) {
        super(position);
        ammo_type = "big";
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.powerupAssets.powerup2, position.x - Constants.POWERUP_CENTER.x, position.y - Constants.POWERUP_CENTER.y);
    }
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class PowerupNuke extends Powerup {

    public PowerupNuke(Vector2 position) {
        super(position);
        ammo_type = "nuke";
        ammo_amount = Constants.POWERUP_AMOUNT_NUKE;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.get_instance().powerupAssets.powerup4, position.x - Constants.POWERUP_CENTER.x, position.y - Constants.POWERUP_CENTER.y);
    }
}

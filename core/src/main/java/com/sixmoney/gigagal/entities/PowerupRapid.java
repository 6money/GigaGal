package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;

public class PowerupRapid extends Powerup {
    public PowerupRapid(Vector2 position) {
        super(position);
        ammo_type = "rapid";
        ammo_amount = Constants.POWERUP_AMOUNT_RAPID;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(Assets.instance.powerupAssets.powerup3, position.x - Constants.POWERUP_CENTER.x, position.y - Constants.POWERUP_CENTER.y);
    }
}

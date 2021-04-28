package com.sixmoney.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sixmoney.utils.Assets;
import com.sixmoney.utils.Constants;
import com.sixmoney.utils.Enums.*;
import com.sixmoney.utils.Utils;

public class BigEnemy extends Enemy {
    public BigEnemy(Platform platform) {
        super(platform);
        health = Constants.ENEMY_BIG_HEALTH;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (charging && direction == Direction.LEFT) {
            Utils.drawTextureRegion(spriteBatch, Assets.instance.enemyAssets.enemy2, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, 30);
        } else if (charging && direction == Direction.RIGHT) {
            Utils.drawTextureRegion(spriteBatch, Assets.instance.enemyAssets.enemy2, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, -30);
        } else {
            Utils.drawTextureRegion(spriteBatch, Assets.instance.enemyAssets.enemy2, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y);
        }
    }
}

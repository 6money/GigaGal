package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums;
import com.sixmoney.gigagal.utils.Utils;

public class EnemyRanged extends Enemy {
    public EnemyRanged(Platform platform, Level level) {
        super(platform, level);
        ranged = true;
    }

    public void render(SpriteBatch spriteBatch) {
        if (charging && direction == Enums.Direction.LEFT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy3, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, 30);
        } else if (charging && direction == Enums.Direction.RIGHT) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy3, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y, -30);
        } else {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().enemyAssets.enemy3, position.x - Constants.ENEMY_CENTER_POS.x, position.y - Constants.ENEMY_CENTER_POS.y);
        }
    }

    @Override
    public void shoot() {
        Enums.Direction shootDirection;
        if (platform.playerPosition < position.x - platform.left) {
             shootDirection = Enums.Direction.LEFT;
        } else {
            shootDirection = Enums.Direction.RIGHT;
        }
        level.getBullets().add(new Lazer(level, new Vector2(position.x, position.y - Constants.ENEMY_CENTER_POS.y / 2), shootDirection));
    }
}

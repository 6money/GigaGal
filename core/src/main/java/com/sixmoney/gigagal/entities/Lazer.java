package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums;
import com.sixmoney.gigagal.utils.Utils;

public class Lazer extends Bullet {
    public Lazer(Level level, Vector2 position, Enums.Direction direction) {
        super(level, position, direction);
        damage = 1;
    }

    public Lazer(Level level, Vector2 position, Vector2 directionVector) {
        super(level, position, directionVector);
        damage = 1;
    }

    @Override
    public void update(float delta) {
        if (directionVector != null) {
            position.x += directionVector.x * delta * Constants.BULLET_SPEED;
            position.y += directionVector.y * delta * Constants.BULLET_SPEED;
        } else {
            float movement_amount = delta * Constants.BULLET_SPEED;

            if (direction == Enums.Direction.LEFT) {
                position.x -= movement_amount;
            } else {
                position.x += movement_amount;
            }
        }

        Vector2 bullet_center = new Vector2(position.x + Constants.BULLET_CENTER.x, position.y + Constants.BULLET_CENTER.y);
        if (bullet_center.dst(level.gigaGal.position) < Constants.GIGAGAL_STANCE_WIDTH) {
            playExplosion();
            add_explosion(bullet_center);
            active = false;
            level.gigaGal.kill();
        }

        float viewport_width = level.getViewport().getWorldWidth();
        Vector3 viewport_position = level.getViewport().getCamera().position;

        if (!travelPastScreen) {
            if (position.x < viewport_position.x - viewport_width / 2 || position.x > viewport_position.x + viewport_width / 2) {
                active = false;
            }
        } else {
            if (position.x > 2000 || position.x < -2000) {
                active = false;
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        if (direction != null) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().bulletAssets.lazer, position.x, position.y - Constants.BULLET_CENTER.y);
        } else if (directionVector != null) {
            Utils.drawTextureRegion(spriteBatch, Assets.get_instance().bulletAssets.lazer, position.x, position.y - Constants.BULLET_CENTER.y, directionVector.angleDeg());
        }
    }
}

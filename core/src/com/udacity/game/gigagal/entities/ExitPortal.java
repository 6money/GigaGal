package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Utils;

public class ExitPortal {
    public final static String TAG = ExitPortal.class.getName();

    private long start_time;

    public Vector2 position;

    public ExitPortal(Vector2 position) {
        this.position = position;
        start_time = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch spriteBatch) {
        float time_elapsed = Utils.secondsSince(start_time);

        TextureRegion exit_portal_key_frame = (TextureRegion) Assets.instance.exitPortalAssets.exit_portal_animation.getKeyFrame(time_elapsed);
        spriteBatch.draw(exit_portal_key_frame, position.x - Constants.EXIT_PORTAL_CENTER.x, position.y - Constants.EXIT_PORTAL_CENTER.y);
    }
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface Particle {
    int getNextParticleEffect(Vector2 position);
    boolean nextParticleReady();
    void update(float delta);
    void update(float delta, int particleID, Vector2 position);
    void draw(SpriteBatch spriteBatch);
    void stop(int particleID);
    void dispose();
}

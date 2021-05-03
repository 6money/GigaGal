package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface Particle {
    public ParticleEffect getNextParticleEffect(Vector2 position);
    public boolean nextParticleReady();
    public void update(float delta);
    public void draw(SpriteBatch spriteBatch);
    public void dispose();
}

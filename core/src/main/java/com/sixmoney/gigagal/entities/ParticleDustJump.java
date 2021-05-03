package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class ParticleDustJump implements Particle {
    protected Array<ParticleEffect> particleEffects;
    private int particleArrayPosition;
    private int particleArraySize;
    private long particleStartTime;

    public ParticleDustJump() {
        particleEffects = new Array<>(3);
        for (int i = 0; i < 3; i++) {
            ParticleEffect temp_particle = new ParticleEffect();
            temp_particle.load(Gdx.files.internal("particles/pixel_dust_jump"), Assets.instance.getAtlas());
            particleEffects.add(temp_particle);
        }
        particleArrayPosition = 0;
        particleArraySize = particleEffects.size;
        particleStartTime = TimeUtils.nanoTime() + 100000000;
    }

    @Override
    public ParticleEffect getNextParticleEffect(Vector2 position) {
        particleStartTime = TimeUtils.nanoTime();
        ParticleEffect particleEffect = particleEffects.get(particleArrayPosition);
        if (particleEffect.isComplete()) {
            particleEffect.getEmitters().first().setPosition(position.x, position.y);
            particleEffect.start();
        }
        particleArrayPosition++;
        if (particleArrayPosition == particleArraySize) {
            particleArrayPosition = 0;
        }

        return particleEffect;
    }

    @Override
    public boolean nextParticleReady() {
        return true;
    }

    @Override
    public void update(float delta) {
        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.update(delta);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.draw(spriteBatch);
        }
    }

    @Override
    public void dispose() {
        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.dispose();
        }
    }
}

package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Utils;

public class ParticleDust implements Particle {
    private int particleArrayPosition;
    private int particleArraySize;
    private ArrayMap<ParticleEffect, Boolean> particleEffectMap;
    private long particleStartTime;

    public ParticleDust() {
        particleEffectMap = new ArrayMap<>(10);
        for (int i = 0; i < 10; i++) {
            ParticleEffect tempParticle = new ParticleEffect();
            tempParticle.load(Gdx.files.internal("particles/pixel_dust"), Assets.get_instance().getAtlas());
            particleEffectMap.put(tempParticle, false);
        }
        particleArrayPosition = 0;
        particleArraySize = particleEffectMap.size;
        particleStartTime = TimeUtils.nanoTime() + 100000000;
    }

    @Override
    public int getNextParticleEffect(Vector2 position) {
        particleStartTime = TimeUtils.nanoTime();
        ParticleEffect particleEffect = particleEffectMap.getKeyAt(particleArrayPosition);
            particleEffectMap.setValue(particleArrayPosition, true);
            particleEffect.setPosition(position.x, position.y);
            particleEffect.start();
        int returnValue = particleArrayPosition;
        particleArrayPosition++;
        if (particleArrayPosition == particleArraySize) {
            particleArrayPosition = 0;
        }

        return returnValue;
    }

    @Override
    public boolean nextParticleReady() {
        return Utils.secondsSince(particleStartTime) > Constants.GIGAGAL_PARTICLE_DELAY;
    }

    @Override
    public void update(float delta) {
        for (int i = 0; i < particleEffectMap.size; i++) {
            if (particleEffectMap.getValueAt(i)) {
                particleEffectMap.getKeyAt(i).update(delta);
            }
        }
    }

    @Override
    public void update(float delta, int particleID, Vector2 position) {
        ParticleEffect particleEffect = particleEffectMap.getKeyAt(particleID);
        particleEffect.setPosition(position.x, position.y);
        particleEffect.update(delta);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        for (int i = 0; i < particleEffectMap.size; i++) {
            if (particleEffectMap.getValueAt(i)) {
                particleEffectMap.getKeyAt(i).draw(spriteBatch);
            }
        }
    }

    @Override
    public void stop(int particleID) {
        particleEffectMap.setValue(particleID, false);
    }

    @Override
    public void dispose() {
        for (int i = 0; i < particleEffectMap.size; i++) {
            particleEffectMap.getKeyAt(i).dispose();
        }
    }
}

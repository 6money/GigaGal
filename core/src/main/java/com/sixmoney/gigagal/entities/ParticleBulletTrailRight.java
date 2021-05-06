package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.sixmoney.gigagal.utils.Assets;

public class ParticleBulletTrailRight implements Particle {
    private int particleArrayPosition;
    private int particleArraySize;
    private ArrayMap<ParticleEffect, Boolean> particleEffectMap;

    public ParticleBulletTrailRight() {
        particleEffectMap = new ArrayMap<>(40);
        for (int i = 0; i < 40; i++) {
            ParticleEffect tempParticle = new ParticleEffect();
            tempParticle.load(Gdx.files.internal("particles/pixel_bullet_trail_right"), Assets.get_instance().getAtlas());
            particleEffectMap.put(tempParticle, false);
        }
        particleArrayPosition = 0;
        particleArraySize = particleEffectMap.size;
    }

    @Override
    public int getNextParticleEffect(Vector2 position) {
        ParticleEffect particleEffect = particleEffectMap.getKeyAt(particleArrayPosition);
        particleEffect.setPosition(position.x, position.y);
        particleEffectMap.setValue(particleArrayPosition, true);
        int returnValue = particleArrayPosition;
        particleArrayPosition++;
        if (particleArrayPosition == particleArraySize) {
            particleArrayPosition = 0;
        }

        return returnValue;
    }

    @Override
    public boolean nextParticleReady() {
        return true;
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

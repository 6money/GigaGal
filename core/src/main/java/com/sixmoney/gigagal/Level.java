package com.sixmoney.gigagal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sixmoney.gigagal.entities.Boss;
import com.sixmoney.gigagal.entities.Bullet;
import com.sixmoney.gigagal.entities.Diamond;
import com.sixmoney.gigagal.entities.Enemy;
import com.sixmoney.gigagal.entities.ExitPortal;
import com.sixmoney.gigagal.entities.Explosion;
import com.sixmoney.gigagal.entities.ExplosionBig;
import com.sixmoney.gigagal.entities.GigaGal;
import com.sixmoney.gigagal.entities.Platform;
import com.sixmoney.gigagal.entities.Powerup;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.ParallaxCamera;
import com.sixmoney.gigagal.utils.SoundManager;

public class Level {
    public final static String TAG = Level.class.getName();

    private ExitPortal exitPortal;
    private Array<Platform> platforms;
    private final DelayedRemovalArray<Enemy> enemies;
    private final DelayedRemovalArray<Boss> bosses;
    private final DelayedRemovalArray<Bullet> bullets;
    private final DelayedRemovalArray<Explosion> explosions;
    private final DelayedRemovalArray<Powerup> powerups;
    private final DelayedRemovalArray<Diamond> diamonds;
    private float killplane_height;
    private final ParticleEffectPool pepExplosion;
    private final ParticleEffectPool pepBulletTrail;
    private final DelayedRemovalArray<PooledEffect> explosionParticles;
    private final ParallaxCamera parallaxCamera;
    private Texture background;
    private Texture background2;
    private TextureRegion backgroundR;
    private TextureRegion backgroundR2;
    private double accumulator;
    private float step;

    public boolean gameOver;
    public boolean victory;
    public int score;
    public float scoreMultiplier;
    public Viewport viewport;
    public GigaGal gigaGal;
    public boolean paused;
    public float difficultly;


    public Level(Viewport viewport, float difficultly, ParallaxCamera parallaxCamera) {
        this.viewport = viewport;
        this.parallaxCamera = parallaxCamera;
        this.difficultly = difficultly;
        if (difficultly == 0f) {
            scoreMultiplier = 1f;
        } else if (difficultly == 50f) {
            scoreMultiplier = 1.5f;
        } else {
            scoreMultiplier = 2f;
        }
        platforms = new Array<>();
        enemies = new DelayedRemovalArray<>();
        bosses = new DelayedRemovalArray<>();
        bullets = new DelayedRemovalArray<>();
        explosions = new DelayedRemovalArray<>();
        powerups = new DelayedRemovalArray<>();
        diamonds = new DelayedRemovalArray<>();
        exitPortal = new ExitPortal(Constants.EXIT_PORTAL_POSITION);

        background = Assets.get_instance().backgroundAssets.clouds3;
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundR = new TextureRegion(background);
        backgroundR.setRegion(0, 0, background.getWidth() * 8, background.getHeight() * 10);

        background2 = Assets.get_instance().backgroundAssets.clouds2;
        background2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundR2 = new TextureRegion(background);
        backgroundR2.setRegion(0, 0, background.getWidth() * 8, background.getHeight() * 10);

        pepExplosion = new ParticleEffectPool(Assets.get_instance().getAssetManager().get(Constants.EXPLOSION_PARTICLE), 5, 5);
        explosionParticles = new DelayedRemovalArray<>();

        pepBulletTrail = new ParticleEffectPool(Assets.get_instance().getAssetManager().get(Constants.BULLET_TRAIL_PARTICLE), 5, 40);

        gameOver = false;
        victory = false;
        score = 0;
        paused = false;
        accumulator = 0;
        step = 1.0f / 240.0f;
    }

    public DelayedRemovalArray<Enemy> getEnemies() {
        return enemies;
    }

    public DelayedRemovalArray<Boss> getBosses() {
        return bosses;
    }

    public DelayedRemovalArray<Bullet> getBullets() {
        return bullets;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public DelayedRemovalArray<Explosion> getExplosions() {
        return explosions;
    }

    public DelayedRemovalArray<Powerup> getPowerups() {
        return powerups;
    }

    public DelayedRemovalArray<Diamond> getDiamonds() {
        return diamonds;
    }

    public void setPlatforms(Array<Platform> platforms) {
        this.platforms = platforms;
    }

    public void setGigaGal(GigaGal gigaGal) {
        this.gigaGal = gigaGal;
    }

    public void setExitPortal(ExitPortal exitPortal) {
        this.exitPortal = exitPortal;
    }

    public float getKillplane_height() {
        return killplane_height;
    }

    public void setKillplane_height(float killplane_height) {
        this.killplane_height = killplane_height;
    }

    public void pauseRunningEffect() {
        gigaGal.pauseRunningEffect();
    }

    public void update(float delta) {
        if (gameOver || victory) {
            gigaGal.stopRunningEffect();
        }

        if (!gameOver && !victory && !paused) {
            Rectangle gigagal_bounding_box = new Rectangle(
                    gigaGal.position.x - Constants.GIGAGAL_STANCE_WIDTH,
                    gigaGal.position.y - Constants.GIGAGAL_EYE_HEIGHT,
                    Constants.GIGAGAL_STANCE_WIDTH * 2,
                    Constants.GIGAGAL_HEIGHT
            );
            if (gigagal_bounding_box.contains(exitPortal.position)) {
                score += (gigaGal.ammmoBasic * Constants.AMMO_SCORE) * scoreMultiplier;
                score += (gigaGal.ammmoBig * Constants.AMMO_SPECIAL_SCORE) * scoreMultiplier;
                score += (gigaGal.ammmoRapid * Constants.AMMO_RAPID_SCORE) * scoreMultiplier;
                score += (gigaGal.ammmoNuke * Constants.AMMO_NUKE_SCORE) * scoreMultiplier;
                victory = true;
            }
            if (gigaGal.lives <= 0) {
                gameOver = true;
            }

            double frameTime = Math.min(delta, 0.25);
            accumulator += frameTime;
            while (accumulator >= step) {
                accumulator -= step;
                gigaGal.update(step, platforms);
            }
            gigaGal.update((float) accumulator, platforms);
            accumulator = 0;

            enemies.begin();
            for (Enemy enemy : enemies) {
                if (enemy.position.x < viewport.getCamera().position.x + (viewport.getWorldWidth() / 2f)
                        && enemy.position.x > viewport.getCamera().position.x - (viewport.getWorldWidth() / 2f)) {
                    enemy.onScreen = true;
                } else {
                    enemy.onScreen = false;
                }
                enemy.update(delta);
                if (enemy.getHealth() <= 0) {
                    SoundManager.get_instance().playSound(Constants.DEATH_SOUND_PATH);
                    explosions.add(new ExplosionBig(enemy.position));
                    enemies.removeValue(enemy, true);
                    score += Constants.ENEMY_KILL_SCORE * scoreMultiplier;

                    PooledEffect particleExplosion = pepExplosion.obtain();
                    particleExplosion.setPosition(enemy.position.x, enemy.position.y);
                    explosionParticles.add(particleExplosion);
                }
            }
            enemies.end();

            bosses.begin();
            for (Boss boss: bosses) {
                if (boss.position.x < viewport.getCamera().position.x + (viewport.getWorldWidth() / 2f)
                        && boss.position.x > viewport.getCamera().position.x - (viewport.getWorldWidth() / 2f)) {
                    boss.onScreen = true;
                } else {
                    boss.onScreen = false;
                }
                boss.update(delta);
                if (boss.health <= 0) {
                    exitPortal.position = boss.position;
                    SoundManager.get_instance().playSound(Constants.DEATH_SOUND_PATH);
                    explosions.add(new ExplosionBig(boss.position));
                    bosses.removeValue(boss, true);
                    score += Constants.ENEMY_KILL_SCORE * scoreMultiplier;

                    PooledEffect particleExplosion = pepExplosion.obtain();
                    particleExplosion.setPosition(boss.position.x, boss.position.y);
                    explosionParticles.add(particleExplosion);
                }
            }
            bosses.end();

            explosions.begin();
            for (Explosion explosion : explosions) {
                if (explosion.isFinished()) {
                    explosions.removeValue(explosion, true);
                }
            }
            explosions.end();

            bullets.begin();
            for (Bullet bullet : bullets) {
                bullet.update(delta);
                if (!bullet.active) {
                    if (bullet.particleBulletTrail != null) {
                        pepBulletTrail.free(bullet.particleBulletTrail);
                    }
                    bullets.removeValue(bullet, true);


                }
            }
            bullets.end();

            explosionParticles.begin();
            for (PooledEffect explosionParticle: explosionParticles) {
                explosionParticle.update(delta);
                if (explosionParticle.isComplete()) {
                    explosionParticle.free();
                    explosionParticles.removeValue(explosionParticle, true);
                }
            }
            explosionParticles.end();
        }
    }

    public void addBullet(Bullet bullet) {
        PooledEffect bulletTrailParticle = pepBulletTrail.obtain();
        bullet.setParticleBulletTrail(bulletTrailParticle);
        bullets.add(bullet);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(parallaxCamera.calculateParallaxMatrix(0.25f, 0.25f));
        spriteBatch.begin();
        spriteBatch.draw(backgroundR, -800, -400, backgroundR.getRegionWidth() * 2, backgroundR.getRegionHeight() * 2);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(parallaxCamera.calculateParallaxMatrix(0.5f, 0.5f));
        spriteBatch.begin();
        spriteBatch.draw(backgroundR2, -800, -400);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(parallaxCamera.calculateParallaxMatrix(1f, 1));
        spriteBatch.begin();
        for (Platform platform: platforms) {
            platform.render(spriteBatch);
        }

        for (Powerup powerup: powerups) {
            powerup.render(spriteBatch);
        }

        for (Enemy enemy: enemies) {
            enemy.render(spriteBatch);
        }

        for (Boss boss: bosses) {
            boss.render(spriteBatch);
        }

        for (Bullet bullet: bullets) {
            bullet.render(spriteBatch);
        }

        exitPortal.render(spriteBatch);

        gigaGal.render(spriteBatch);

        for (Explosion explosion: explosions) {
            explosion.render(spriteBatch);
        }

        for (Diamond diamond: diamonds) {
            diamond.render(spriteBatch);
        }

        for (PooledEffect particleExplosion: explosionParticles) {
            particleExplosion.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        for (Enemy enemy: enemies) {
            enemy.debugRender(shapeRenderer);
        }

        for (Boss boss: bosses) {
            boss.debugRender(shapeRenderer);
        }

        for (Powerup powerup: powerups) {
            powerup.debugRender(shapeRenderer);
        }

        for (Diamond diamond: diamonds) {
            diamond.debugRender(shapeRenderer);
        }

        gigaGal.debugRender(shapeRenderer);
    }

    public void dispose() {
        gigaGal.dispose();
        pepExplosion.clear();
        pepBulletTrail.clear();
    }
}

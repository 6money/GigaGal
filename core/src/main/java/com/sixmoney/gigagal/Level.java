package com.sixmoney.gigagal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
        bullets = new DelayedRemovalArray<>();
        explosions = new DelayedRemovalArray<>();
        powerups = new DelayedRemovalArray<>();
        diamonds = new DelayedRemovalArray<>();
        exitPortal = new ExitPortal(Constants.EXIT_PORTAL_POSITION);
//        addDebugPlatforms();

        background = Assets.get_instance().backgroundAssets.clouds3;
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundR = new TextureRegion(background);
        backgroundR.setRegion(0, 0, background.getWidth() * 4, background.getHeight() * 8);

        background2 = Assets.get_instance().backgroundAssets.clouds2;
        background2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundR2 = new TextureRegion(background);
        backgroundR2.setRegion(0, 0, background.getWidth() * 4, background.getHeight() * 8);

        ParticleEffect explosionParticle = new ParticleEffect();
        explosionParticle.load(Gdx.files.internal("particles/pixel_explosion"), Assets.get_instance().getAtlas());
        pepExplosion = new ParticleEffectPool(explosionParticle, 5, 5);
        explosionParticles = new DelayedRemovalArray<>();

        ParticleEffect bullerTrailParticle = new ParticleEffect();
        bullerTrailParticle.load(Gdx.files.internal("particles/pixel_bullet_trail_2"), Assets.get_instance().getAtlas());
        pepBulletTrail = new ParticleEffectPool(bullerTrailParticle, 5, 5);

        gameOver = false;
        victory = false;
        score = 0;
        paused = false;
    }

    public DelayedRemovalArray<Enemy> getEnemies() {
        return enemies;
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

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }

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
                victory = true;
            }
            if (gigaGal.lives <= 0) {
                gameOver = true;
            }

            gigaGal.update(delta, platforms);

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
        spriteBatch.draw(backgroundR, -400, -400, backgroundR.getRegionWidth() * 2, backgroundR.getRegionHeight() * 2);
        spriteBatch.end();

        spriteBatch.setProjectionMatrix(parallaxCamera.calculateParallaxMatrix(0.5f, 0.5f));
        spriteBatch.begin();
        spriteBatch.draw(backgroundR2, -400, -400);
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

        for (Powerup powerup: powerups) {
            powerup.debugRender(shapeRenderer);
        }

        for (Diamond diamond: diamonds) {
            diamond.debugRender(shapeRenderer);
        }

        gigaGal.debugRender(shapeRenderer);
    }

    private void addDebugPlatforms() {
        platforms.add(new Platform(-20, 100, 80, 10));
        platforms.add(new Platform(80, 60, 60, 40));
        platforms.add(new Platform(40, 50, 20, 10));
        platforms.add(new Platform(0, 0, 80, 40));

        platforms.add(new Platform(20, 170, 80, 40));
        platforms.add(new Platform(-90, 160, 20, 20));
        platforms.add(new Platform(100, 162, 40, 10));
        platforms.add(new Platform(-90, 200, 20, 20));
        platforms.add(new Platform(-90, 240, 20, 20));
        platforms.add(new Platform(-90, 280, 20, 20));
        platforms.add(new Platform(-70, 280, 280, 10));
        platforms.add(new Platform(210, 210, 10, 10));

        enemies.add(new Enemy(platforms.get(1), this));

        powerups.add(new Powerup(new Vector2(25, 180)));

        gigaGal = new GigaGal(new Vector2(20, 0), this);
    }

    public void dispose() {
        gigaGal.dispose();
        pepExplosion.clear();
        pepBulletTrail.clear();
    }
}

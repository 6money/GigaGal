package com.sixmoney.gigagal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.SoundManager;

public class Level {
    public final static String TAG = Level.class.getName();

    private ExitPortal exitPortal;
    private Array<Platform> platforms;
    private DelayedRemovalArray<Enemy> enemies;
    private DelayedRemovalArray<Bullet> bullets;
    private DelayedRemovalArray<Explosion> explosions;
    private DelayedRemovalArray<Powerup> powerups;
    private DelayedRemovalArray<Diamond> diamonds;
    private float killplane_height;

    public boolean gameOver;
    public boolean victory;
    public int score;
    public Viewport viewport;
    public GigaGal gigaGal;
    public boolean paused;

    public Level(Viewport viewport) {
        this.viewport = viewport;
        platforms = new Array<>();
        enemies = new DelayedRemovalArray<>();
        bullets = new DelayedRemovalArray<>();
        explosions = new DelayedRemovalArray<>();
        powerups = new DelayedRemovalArray<>();
        diamonds = new DelayedRemovalArray<>();
        exitPortal = new ExitPortal(Constants.EXIT_PORTAL_POSITION);
//        addDebugPlatforms();

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
                score += gigaGal.ammmoBasic * Constants.AMMO_SCORE;
                score += gigaGal.ammmoBig * Constants.AMMO_SPECIAL_SCORE;
                score += gigaGal.ammmoRapid * Constants.AMMO_RAPID_SCORE;
                victory = true;
            }
            if (gigaGal.lives <= 0) {
                gameOver = true;
            }

            gigaGal.update(delta, platforms);

            enemies.begin();
            for (Enemy enemy : enemies) {
                enemy.update(delta);
                if (enemy.getHealth() <= 0) {
                    SoundManager.get_instance().playSound(Constants.DEATH_SOUND_PATH);
                    explosions.add(new ExplosionBig(enemy.position));
                    enemies.removeValue(enemy, true);
                    score += Constants.ENEMY_KILL_SCORE;
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
                    bullets.removeValue(bullet, true);
                }
            }

            bullets.end();
        }
    }

    public void render(SpriteBatch spriteBatch) {
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

        enemies.add(new Enemy(platforms.get(1)));

        powerups.add(new Powerup(new Vector2(25, 180)));

        gigaGal = new GigaGal(new Vector2(20, 0), this);
    }

    public void dispose() {
        gigaGal.dispose();
    }
}

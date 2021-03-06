package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums.Direction;
import com.sixmoney.gigagal.utils.Enums.GunType;
import com.sixmoney.gigagal.utils.Enums.JumpState;
import com.sixmoney.gigagal.utils.Enums.WalkState;
import com.sixmoney.gigagal.utils.SoundManager;
import com.sixmoney.gigagal.utils.Utils;

public class GigaGal {

    public final static String TAG = GigaGal.class.getName();

    private Vector2 spawn_position;
    private Vector2 position_last_frame;
    private Vector2 velocity;
    private Direction facing;
    private JumpState jumpState;
    private WalkState walkState;
    private long jumpStartTime;
    private long walkStartTime;
    private long killStartTime;
    private Level level;
    private boolean hit_solid;
    private boolean canDrop;
    private boolean bounce;
    private boolean dead;
    private SoundManager soundManager;
    private Music runningEffectMusic;
    private ParticleEffectPool pepDust;
    private ParticleEffectPool pepDustJump;
    private DelayedRemovalArray<PooledEffect> dustParticles;
    private DelayedRemovalArray<PooledEffect> dustJumpParticles;
    private long dustParticleStartTime;
    private GunType gunType;


    public Vector2 position;
    public int ammmoBasic;
    public int ammmoBig;
    public int ammmoRapid;
    public int ammmoNuke;
    public int lives;
    public boolean jumpButtonPressed;
    public boolean shootButtonPressed;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean dropButtonPressed;
    public long bulletFireStartTime;

    public GigaGal(Vector2 spawn_position, Level level) {
        this.spawn_position = spawn_position;
        this.level = level;
        soundManager = SoundManager.get_instance();
        runningEffectMusic = soundManager.getMusic(Constants.RUNNING_SOUND_PATH);
        runningEffectMusic.pause();
        init();

        pepDust = new ParticleEffectPool(Assets.get_instance().getAssetManager().get(Constants.DUST_PARTICLE), 10, 10);
        dustParticles = new DelayedRemovalArray<>();
        dustParticleStartTime = TimeUtils.nanoTime() + 100000000;

        pepDustJump = new ParticleEffectPool(Assets.get_instance().getAssetManager().get(Constants.DUST_JUMP_PARTICLE), 4, 4);
        dustJumpParticles = new DelayedRemovalArray<>();
    }

    public void init(){
        respawn();
        ammmoBasic = Constants.GIGAGAL_INIT_AMMO;
        ammmoBig = 0;
        ammmoRapid = 0;
        ammmoNuke = 0;
        gunType = GunType.PISTOL;
        if (level.difficultly == 0f) {
            lives = Constants.GIGAGAL_INIT_LIVES;
        } else if (level.difficultly == 50f) {
            lives = Constants.GIGAGAL_INIT_LIVES - 1;
        } else {
            lives = Constants.GIGAGAL_INIT_LIVES - 2;
        }
    }

    public void respawn() {
        position = new Vector2(spawn_position);
        position.y += Constants.GIGAGAL_EYE_HEIGHT;
        position_last_frame = new Vector2(spawn_position);
        position_last_frame.y += Constants.GIGAGAL_EYE_HEIGHT;
        velocity = new Vector2(0, 0);
        facing = Direction.RIGHT;
        jumpState = JumpState.FALLING;
        walkState = WalkState.STANDING;
        hit_solid = false;
        dead = false;
        bulletFireStartTime = TimeUtils.nanoTime();
    }

    public void update(float delta, Array<Platform> platforms) {
        if (dead && Utils.secondsSince(killStartTime) > Constants.GIGAGAL_DEATH_DELAY) {
            respawn();
        }

        if (!dead) {
            position_last_frame.set(position);
            velocity.y -= delta * Constants.GRAVITY;
            position.mulAdd(velocity, delta);
            hit_solid = false;

            Rectangle gigagal_bounding_box = new Rectangle(
                    position.x - Constants.GIGAGAL_STANCE_WIDTH,
                    position.y - Constants.GIGAGAL_EYE_HEIGHT,
                    Constants.GIGAGAL_STANCE_WIDTH * 2,
                    Constants.GIGAGAL_HEIGHT
            );

            if (!dead && position.y < level.getKillplane_height()) {
                kill();
            }

            for (Platform platform : platforms) {
                // Check for move through solid platforms
                if (platform.solid && gigagal_bounding_box.overlaps(new Rectangle(platform.left, platform.bottom, platform.width, platform.height - 1))) {
                    if (velocity.y > 0) {
                        velocity.y = 0;
                    }
                    hit_solid = true;
                    velocity.x = 0;
                    position.set(position_last_frame);
                    position.mulAdd(velocity, delta);

                    if (position.x < platform.left) {
                        position.x -= 0.25f;
                    } else if (position.x > platform.left + platform.width) {
                        position.x += 0.25f;
                    }
                }
            }

            if (jumpState != JumpState.JUMPING) {
                if (jumpState != JumpState.RECOILING) {
                    jumpState = JumpState.FALLING;
                }

                boolean landed_flag = false;
                for (Platform platform : platforms) {
                    platform.hasPlayer = false;
                    if (landedOnPlatform(platform)) {
                        platform.hasPlayer = true;
                        platform.playerPosition = position.x - platform.left;
                        if (!landed_flag && !platform.invisible) {
                            canDrop = platform.droppable;
                            bounce = platform.bounce;
                            jumpState = JumpState.GROUNDED;
                            velocity.y = 0;
                            velocity.x = 0;
                            position.y = platform.top + Constants.GIGAGAL_EYE_HEIGHT;
                            landed_flag = true;
                        }
                        if (canDrop && !platform.droppable) {
                            canDrop = platform.droppable;
                        }
                        if (!bounce && platform.bounce) {
                            bounce = platform.bounce;
                        }
                    }
                }

                if (bounce) {
                    bounce = false;
                    startJump(gigagal_bounding_box, platforms, true);
                }
            }

            for (Enemy enemy : level.getEnemies()) {
                Rectangle enemy_bounding_box = enemy.enemy_bounding_box;

                boolean hit_enemy = gigagal_bounding_box.overlaps(enemy_bounding_box);
                if (hit_enemy && position.x + Constants.GIGAGAL_STANCE_WIDTH < enemy.position.x + Constants.ENEMY_COLLISION_RADIUS / 2) {
                    recoilFromEnemy(Direction.LEFT);
                } else if (hit_enemy && position.x + Constants.GIGAGAL_STANCE_WIDTH > enemy.position.x + Constants.ENEMY_COLLISION_RADIUS / 2) {
                    recoilFromEnemy(Direction.RIGHT);
                }

            }

            for (Boss boss : level.getBosses()) {
                Rectangle enemy_bounding_box = boss.enemy_bounding_box;

                boolean hit_enemy = gigagal_bounding_box.overlaps(enemy_bounding_box);
                if (hit_enemy && position.x + Constants.GIGAGAL_STANCE_WIDTH < boss.position.x + Constants.ENEMY_COLLISION_RADIUS / 2) {
                    recoilFromEnemy(Direction.LEFT);
                } else if (hit_enemy && position.x + Constants.GIGAGAL_STANCE_WIDTH > boss.position.x + Constants.ENEMY_COLLISION_RADIUS / 2) {
                    recoilFromEnemy(Direction.RIGHT);
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.Z) || jumpButtonPressed) {
                switch (jumpState) {
                    case GROUNDED:
                        startJump(gigagal_bounding_box, platforms, false);
                    case JUMPING:
                        continueJump(gigagal_bounding_box, platforms, false);
                    case FALLING:
                        break;
                }
            } else {
                endJump();
            }

            if (jumpState != JumpState.RECOILING && !hit_solid) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftButtonPressed) {
                    if (jumpState == JumpState.GROUNDED) {
                        runningEffectMusic.play();
                    } else {
                        runningEffectMusic.pause();
                    }
                    moveLeft(delta);
                } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightButtonPressed) {
                    if (jumpState == JumpState.GROUNDED) {
                        runningEffectMusic.play();
                    } else {
                        runningEffectMusic.pause();
                    }
                    moveRight(delta);
                } else {
                    walkState = WalkState.STANDING;
                    runningEffectMusic.pause();
                }

                if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || dropButtonPressed) {
                    moveDown(delta);
                }
            } else if (jumpState == JumpState.RECOILING) {
                runningEffectMusic.pause();
            }

            level.getPowerups().begin();
            for (Powerup powerup : level.getPowerups()) {
                Rectangle power_bounding_box = powerup.powerup_bounding_box;

                boolean hit_powerup = gigagal_bounding_box.overlaps(power_bounding_box);
                if (hit_powerup) {
                    soundManager.playSound(Constants.COLLECT_POWERUP_PATH);
                    switch (powerup.ammo_type) {
                        case "basic":
                            ammmoBasic += powerup.ammo_amount;
                            break;
                        case "big":
                            ammmoBig += powerup.ammo_amount;
                            break;
                        case "rapid":
                            ammmoRapid += powerup.ammo_amount;
                            break;
                        case "nuke":
                            ammmoNuke += powerup.ammo_amount;
                            break;
                    }
                    level.getPowerups().removeValue(powerup, true);
                    level.score += Constants.POWERUP_SCORE * level.scoreMultiplier;
                }
            }
            level.getPowerups().end();

            level.getDiamonds().begin();
            for (Diamond diamond : level.getDiamonds()) {
                Rectangle diamond_bounding_box = diamond.diamond_bounding_box;

                boolean hit_diamond = gigagal_bounding_box.overlaps(diamond_bounding_box);
                if (hit_diamond) {
                    soundManager.playSound(Constants.COLLECT_DIAMOND_PATH);
                    level.getDiamonds().removeValue(diamond, true);
                    level.score += Constants.DIAMOND_SCORE * level.scoreMultiplier;
                }
            }
            level.getDiamonds().end();

            if (shootButtonPressed && ammmoRapid > 0) {
                if (Utils.secondsSince(bulletFireStartTime) >= Constants.BULLET_RAPID_FIRE_DELAY) {
                    bulletFireStartTime = TimeUtils.nanoTime();
                    shoot();
                }
            } else if (shootButtonPressed && (ammmoBasic > 0 || ammmoBig > 0 || ammmoNuke > 0)) {
                shoot();
                shootButtonPressed = false;
            }

            if (ammmoRapid > 0) {
                gunType = GunType.RAPID;
            } else if (ammmoBig > 0) {
                gunType = GunType.CANNON;
            } else if (ammmoBasic > 0) {
                gunType = GunType.PISTOL;
            } else if (ammmoNuke > 0) {
                gunType = GunType.NUKE_CANNON;
            } else {
                gunType = GunType.PISTOL;
            }
        }


        dustJumpParticles.begin();
        for (PooledEffect dustJumpParticle: dustJumpParticles) {
            dustJumpParticle.update(delta);
            if (dustJumpParticle.isComplete()) {
                dustJumpParticle.free();
                dustJumpParticles.removeValue(dustJumpParticle, true);
            }
        }
        dustJumpParticles.end();
        dustParticles.begin();
        for (PooledEffect dustParticle: dustParticles) {
            dustParticle.update(delta);
            if (dustParticle.isComplete()) {
                dustParticle.free();
                dustParticles.removeValue(dustParticle, true);
            }
        }
        dustParticles.end();
    }

    public void shoot() {
        Vector2 bulletPosition;
        int bulletOffset = 0;

        if (gunType == GunType.CANNON || gunType == GunType.NUKE_CANNON) {
            bulletOffset = -1;
        }

        if (facing == Direction.RIGHT) {
            bulletPosition = new Vector2(
                    position.x + Constants.GIGAGAL_BARREL_POS.x,
                    position.y + Constants.GIGAGAL_BARREL_POS.y + bulletOffset
            );
        } else {
            bulletPosition = new Vector2(
                    position.x - Constants.GIGAGAL_BARREL_POS.x,
                    position.y + Constants.GIGAGAL_BARREL_POS.y + bulletOffset
            );
        }

        if (ammmoRapid > 0) {
            playGunshot();
            ammmoRapid--;
            Bullet bullet = new BulletRapid(level, bulletPosition, facing);
            level.addBullet(bullet);
        } else if (ammmoBig > 0) {
            playGunshot();
            ammmoBig--;
            Bullet bullet = new BulletBig(level, bulletPosition, facing);
            level.addBullet(bullet);
        } else if (ammmoBasic > 0) {
            playGunshot();
            ammmoBasic--;
            Bullet bullet = new Bullet(level, bulletPosition, facing);
            level.addBullet(bullet);
        } else if (ammmoNuke > 0) {
            playGunshot();
            ammmoNuke--;
            Bullet bullet = new BulletNuke(level, bulletPosition, facing);
            level.addBullet(bullet);
        }
    }

    public void playGunshot() {
        int gunshotid = MathUtils.random(1, 4);
        switch (gunshotid) {
            case 2:
                soundManager.playSound(Constants.GUNSHOT2_PATH);
                break;
            case 3:
                soundManager.playSound(Constants.GUNSHOT3_PATH);
                break;
            case 4:
                soundManager.playSound(Constants.GUNSHOT4_PATH);
                break;
            default:
                soundManager.playSound(Constants.GUNSHOT1_PATH);
                break;
        }
    }

    boolean landedOnPlatform(Platform platform) {
        boolean landed = false;

        if ((position_last_frame.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.top &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT <= platform.top) ||
                (position_last_frame.y - Constants.GIGAGAL_EYE_HEIGHT == platform.top &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT == platform.top)) {
            float left_toe = position.x - Constants.GIGAGAL_STANCE_WIDTH;
            float right_toe = position.x + Constants.GIGAGAL_STANCE_WIDTH;

            if (left_toe < platform.right && left_toe > platform.left) {
                landed = true;
            } else if (right_toe < platform.right && right_toe > platform.left) {
                landed = true;
            } else if (left_toe < platform.left && right_toe > platform.right) {
                landed = true;
            }
        }

        return landed;
    }

    private void moveLeft(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }

        walkState = WalkState.WALKING;
        facing = Direction.LEFT;
        position.x -= delta * Constants.GIGAGAL_MOVEMENT_SPEED;

        if (Utils.secondsSince(dustParticleStartTime) > Constants.GIGAGAL_PARTICLE_DELAY && jumpState == JumpState.GROUNDED) {
            dustParticleStartTime = TimeUtils.nanoTime();
            PooledEffect particleDust = pepDust.obtain();
            particleDust.setPosition(position.x, position.y - Constants.GIGAGAL_EYE_HEIGHT);
            dustParticles.add(particleDust);
        }
    }

    private void moveRight(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }

        walkState = WalkState.WALKING;
        facing = Direction.RIGHT;
        position.x += delta * Constants.GIGAGAL_MOVEMENT_SPEED;

        if (Utils.secondsSince(dustParticleStartTime) > Constants.GIGAGAL_PARTICLE_DELAY && jumpState == JumpState.GROUNDED) {
            dustParticleStartTime = TimeUtils.nanoTime();
            PooledEffect particleDust = pepDust.obtain();
            particleDust.setPosition(position.x, position.y - Constants.GIGAGAL_EYE_HEIGHT);
            dustParticles.add(particleDust);
        }
    }

    private void moveDown(float delta) {
        if (jumpState == JumpState.GROUNDED && canDrop) {
            soundManager.playSound(Constants.JUMP_SOUND_PATH);
            position.y -= delta * Constants.GIGAGAL_MOVEMENT_SPEED;
            jumpState = JumpState.FALLING;
        }
    }

    private void startJump(Rectangle gigagal_bounding_box, Array<Platform> platforms, boolean bounceJump) {
        soundManager.playSound(Constants.JUMP_SOUND_PATH);
        jumpState = JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        PooledEffect particleDustJump = pepDustJump.obtain();
        particleDustJump.setPosition(position.x, position.y - Constants.GIGAGAL_EYE_HEIGHT);
        dustJumpParticles.add(particleDustJump);
        continueJump(gigagal_bounding_box,  platforms, bounceJump);
    }

    private void continueJump(Rectangle gigagal_bounding_box, Array<Platform> platforms, boolean bounceJump) {
        boolean hit_solid = false;

        if (jumpState != JumpState.JUMPING) {
            return;
        }

        float jumpDuration = Utils.secondsSince(jumpStartTime);

        for (Platform platform: platforms) {
            if (platform.solid && gigagal_bounding_box.overlaps(new Rectangle(platform.left, platform.bottom, platform.width, platform.height - 1))) {
                velocity.y = 0;
                velocity.x = 0;
                hit_solid = true;
                break;
            }
        }

        float maxJumpDuration = Constants.GIGAGAL_JUMP_DURATION;
        if (bounceJump) {
            maxJumpDuration = Constants.GIGAGAL_BOUNCE_JUMP_DURATION;
        }
        float jumpSpeed = Constants.GIGAGAL_JUMP_SPEED;
        if (bounceJump) {
            jumpSpeed = Constants.GIGAGAL_BOUNCE_JUMP_SPEED;
        }

        if (jumpDuration < maxJumpDuration && !hit_solid) {
            velocity.y = jumpSpeed;
        } else {
            endJump();
        }
    }

    private void endJump() {
        if (jumpState == JumpState.JUMPING) {
            jumpState = JumpState.FALLING;
        }
    }

    private void recoilFromEnemy(Direction hitDirection) {
        soundManager.playSound(Constants.JUMP_SOUND_PATH);
        jumpState = JumpState.RECOILING;
        velocity.y = Constants.GIGAGAL_KNOCKBACK_SPEED.y;

        if (hitDirection == Direction.LEFT) {
            velocity.x = -Constants.GIGAGAL_KNOCKBACK_SPEED.x;
        } else if (hitDirection == Direction.RIGHT) {
            velocity.x = Constants.GIGAGAL_KNOCKBACK_SPEED.x;
        }

    }

    public void stopRunningEffect() {
        runningEffectMusic.stop();
    }

    public void pauseRunningEffect() {
        if (runningEffectMusic.isPlaying()) {
            runningEffectMusic.pause();
        }
    }

    public void kill() {
        if (dead) {
            return;
        }
        dead = true;
        killStartTime = TimeUtils.nanoTime();
        lives -= 1;
        if (lives <= 0) {
            return;
        }
        killStartTime = TimeUtils.nanoTime();
        soundManager.playSound(Constants.DEATH_SOUND_PATH);
    }

    public void render(SpriteBatch spriteBatch) {
        TextureRegion gigagalTexture;
        TextureRegion gigagalArmTexture = null;
        TextureRegion gunTexture;

        if (facing == Direction.LEFT) {
            if (jumpState != JumpState.GROUNDED) {
                gigagalTexture = Assets.get_instance().gigaGalAssets.jumping_left;
            } else if (walkState == WalkState.WALKING) {
                float walking_duration = Utils.secondsSince(walkStartTime);
                gigagalTexture = (TextureAtlas.AtlasRegion) Assets.get_instance().gigaGalAssets.walkLeftLoop.getKeyFrame(walking_duration);
                gigagalArmTexture = (TextureAtlas.AtlasRegion) Assets.get_instance().gigaGalAssets.walkArmLeftLoop.getKeyFrame(walking_duration);
            } else {
                gigagalTexture = Assets.get_instance().gigaGalAssets.standing_left;
            }

            if (gunType == GunType.RAPID) {
                gunTexture = Assets.get_instance().gigaGalAssets.rapid_left;
            } else if (gunType == GunType.CANNON) {
                gunTexture = Assets.get_instance().gigaGalAssets.cannon_left;
            } else if (gunType == GunType.NUKE_CANNON) {
                gunTexture = Assets.get_instance().gigaGalAssets.nuke_cannon_left;
            } else {
                gunTexture = Assets.get_instance().gigaGalAssets.pistol_left;
            }
        } else {
            if (jumpState != JumpState.GROUNDED) {
                gigagalTexture = Assets.get_instance().gigaGalAssets.jumping_right;
            } else if (walkState == WalkState.WALKING) {
                float walking_duration = Utils.secondsSince(walkStartTime);
                gigagalTexture = (TextureAtlas.AtlasRegion) Assets.get_instance().gigaGalAssets.walkRightLoop.getKeyFrame(walking_duration);
                gigagalArmTexture = (TextureAtlas.AtlasRegion) Assets.get_instance().gigaGalAssets.walkArmRightLoop.getKeyFrame(walking_duration);
            } else {
                gigagalTexture = Assets.get_instance().gigaGalAssets.standing_right;
            }

            if (gunType == GunType.RAPID) {
                gunTexture = Assets.get_instance().gigaGalAssets.rapid_right;
            } else if (gunType == GunType.CANNON) {
                gunTexture = Assets.get_instance().gigaGalAssets.cannon_right;
            } else if (gunType == GunType.NUKE_CANNON) {
                gunTexture = Assets.get_instance().gigaGalAssets.nuke_cannon_right;
            } else {
                gunTexture = Assets.get_instance().gigaGalAssets.pistol_right;
            }
        }

        if (gunType != GunType.RAPID) {
            Utils.drawTextureRegion(
                    spriteBatch,
                    gunTexture,
                    position.x - Constants.GIGAGAL_EYE_POS.x,
                    position.y - Constants.GIGAGAL_EYE_POS.y);
        }

        Utils.drawTextureRegion(
                spriteBatch,
                gigagalTexture,
                position.x - Constants.GIGAGAL_EYE_POS.x,
                position.y - Constants.GIGAGAL_EYE_POS.y);

        if (gunType == GunType.RAPID) {
            Utils.drawTextureRegion(
                    spriteBatch,
                    gunTexture,
                    position.x - Constants.GIGAGAL_EYE_POS.x,
                    position.y - Constants.GIGAGAL_EYE_POS.y);
        }

        if (gigagalArmTexture != null) {
            Utils.drawTextureRegion(
                    spriteBatch,
                    gigagalArmTexture,
                    position.x - Constants.GIGAGAL_EYE_POS.x,
                    position.y - Constants.GIGAGAL_EYE_POS.y);
        }


        for (PooledEffect particleDustJump: dustJumpParticles) {
            particleDustJump.draw(spriteBatch);
        }
        for (PooledEffect particleDust: dustParticles) {
            particleDust.draw(spriteBatch);
        }

    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                position.x - Constants.GIGAGAL_STANCE_WIDTH,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH * 2,
                Constants.GIGAGAL_HEIGHT
        );
    }

    public void dispose() {
        pepDust.clear();
        pepDustJump.clear();
        runningEffectMusic.dispose();
    }
}

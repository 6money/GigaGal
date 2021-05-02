package com.sixmoney.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.sixmoney.gigagal.Level;
import com.sixmoney.gigagal.utils.Assets;
import com.sixmoney.gigagal.utils.Constants;
import com.sixmoney.gigagal.utils.Enums.*;
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
    private Level level;
    private boolean hit_solid;
    private boolean canDrop;
    private SoundManager soundManager;
    private long runningEffectId;
    public Array<ParticleEffect> particleEffects;
    private int particleArrayPosition;
    private int particleArraySize;
    private long particleStartTime;

    public Vector2 position;
    public int ammmoBasic;
    public int ammmoBig;
    public int ammmoRapid;
    public int lives;
    public boolean jumpButtonPressed;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean dropButtonPressed;

    public GigaGal(Vector2 spawn_position, Level level) {
        this.spawn_position = spawn_position;
        this.level = level;
        soundManager = SoundManager.get_instance();
        runningEffectId = soundManager.playSound(Constants.RUNNING_SOUND_PATH, true);
        soundManager.pauseSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
        particleEffects = new Array<>(10);
        for (int i = 0; i < 10; i++) {
            ParticleEffect temp_particle = new ParticleEffect();
            temp_particle.load(Gdx.files.internal("particles/pixel_dust"), Assets.instance.getAtlas());
            particleEffects.add(temp_particle);
        }
        particleArrayPosition = 0;
        particleArraySize = particleEffects.size;
        particleStartTime = 0;
        init();
    }

    public void init(){
        respawn();
        ammmoBasic = Constants.GIGAGAL_INIT_AMMO;
        ammmoBig = 0;
        ammmoRapid = 0;
        lives = Constants.GIGAGAL_INIT_LIVES;
        particleStartTime = TimeUtils.nanoTime();
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
    }

    public void update(float delta, Array<Platform> platforms) {
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

        if (position.y < level.getKillplane_height()) {
            lives -= 1;
            if (lives <= 0) {
                return;
            }
            soundManager.playSound(Constants.DEATH_SOUND_PATH);
            respawn();
        }

        for (Platform platform: platforms) {
            // First check if we are on platform to update all platform states
            platform.hasPlayer = false;
            landedOnPlatform(platform);

            // Then checks for move through platforms
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

            for (Platform platform: platforms) {
                platform.hasPlayer = false;
                if (landedOnPlatform(platform)) {
                    jumpState = JumpState.GROUNDED;
                    velocity.y = 0;
                    velocity.x = 0;
                    position.y = platform.top + Constants.GIGAGAL_EYE_HEIGHT;
                    break;
                }
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

        if (Gdx.input.isKeyPressed(Input.Keys.Z ) || jumpButtonPressed) {
            switch (jumpState) {
                case GROUNDED:
                    startJump(gigagal_bounding_box,  platforms);
                case JUMPING:
                    continueJump(gigagal_bounding_box,  platforms);
                case FALLING:
                    break;
            }
        } else {
            endJump();
        }

        if (jumpState != JumpState.RECOILING && !hit_solid) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftButtonPressed) {
                if (jumpState == JumpState.GROUNDED) {
                    soundManager.resumeSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
                } else {
                    soundManager.pauseSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
                }
                moveLeft(delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightButtonPressed) {
                if (jumpState == JumpState.GROUNDED) {
                    soundManager.resumeSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
                } else {
                    soundManager.pauseSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
                }
                moveRight(delta);
            } else {
                walkState = WalkState.STANDING;
                soundManager.pauseSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || dropButtonPressed) {
                moveDown(delta);
            }


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
                }
                level.getPowerups().removeValue(powerup, true);
                level.score += Constants.POWERUP_SCORE;
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
                level.score += Constants.DIAMOND_SCORE;
            }
        }
        level.getDiamonds().end();

        if (Gdx.input.isKeyPressed(Input.Keys.X) && ammmoRapid > 0) {
            shoot();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.X) && (ammmoBasic > 0 || ammmoBig > 0)) {
            shoot();
        }

        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.update(delta);
        }
    }

    public void shoot() {
        Vector2 bullet_position;

        if (facing == Direction.RIGHT) {
            bullet_position = new Vector2(
                    position.x + Constants.GIGAGAL_BARREL_POS.x,
                    position.y + Constants.GIGAGAL_BARREL_POS.y
            );
        } else {
            bullet_position = new Vector2(
                    position.x - Constants.GIGAGAL_BARREL_POS.x,
                    position.y + Constants.GIGAGAL_BARREL_POS.y
            );
        }

        if (ammmoRapid > 0) {
            playGunshot();
            ammmoRapid--;
            level.getBullets().add(new BulletRapid(level, bullet_position, facing));
        } else if (ammmoBig > 0) {
            playGunshot();
            ammmoBig--;
            level.getBullets().add(new BulletBig(level, bullet_position, facing));
        } else if (ammmoBasic > 0) {
            playGunshot();
            ammmoBasic--;
            level.getBullets().add(new Bullet(level, bullet_position, facing));
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
        canDrop = true;

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

        if (landed) {
            canDrop = platform.droppable;
            platform.hasPlayer = true;
            platform.playerPosition = position.x - platform.left;
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
        spawnParticle();
    }

    private void moveRight(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }

        walkState = WalkState.WALKING;
        facing = Direction.RIGHT;
        position.x += delta * Constants.GIGAGAL_MOVEMENT_SPEED;
        spawnParticle();
    }

    private void moveDown(float delta) {
        if (jumpState == JumpState.GROUNDED && canDrop) {
            soundManager.playSound(Constants.JUMP_SOUND_PATH);
            position.y -= delta * Constants.GIGAGAL_MOVEMENT_SPEED;
            jumpState = JumpState.FALLING;
        }
    }

    private void startJump(Rectangle gigagal_bounding_box, Array<Platform> platforms) {
        soundManager.playSound(Constants.JUMP_SOUND_PATH);
        jumpState = JumpState.JUMPING;
        jumpStartTime = TimeUtils.nanoTime();
        continueJump(gigagal_bounding_box,  platforms);
    }

    private void continueJump(Rectangle gigagal_bounding_box, Array<Platform> platforms) {
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

        if (jumpDuration < Constants.GIGAGAL_JUMP_DURATION && !hit_solid) {
            velocity.y = Constants.GIGAGAL_JUMP_SPEED;
        } else {
            endJump();
        }
    }

    private void endJump() {
        if (jumpState == JumpState.JUMPING) {
            jumpState = JumpState.FALLING;
        }
    }

    private void spawnParticle() {
        if (Utils.secondsSince(particleStartTime) > Constants.GIGAGAL_PARTICLE_DELAY && jumpState == JumpState.GROUNDED) {
            particleStartTime = TimeUtils.nanoTime();
            ParticleEffect particleEffect = particleEffects.get(particleArrayPosition);
            if (particleEffect.isComplete()) {
                particleEffect.getEmitters().first().setPosition(position.x, position.y - Constants.GIGAGAL_EYE_HEIGHT);
                particleEffect.start();
            }
            particleArrayPosition++;
            if (particleArrayPosition == particleArraySize) {
                particleArrayPosition = 0;
            }
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
        soundManager.stopSound(Constants.RUNNING_SOUND_PATH, runningEffectId);
    }

    public void render(SpriteBatch spriteBatch) {
        TextureRegion textureRegion;

        if (facing == Direction.LEFT) {
            if (jumpState != JumpState.GROUNDED) {
                textureRegion = Assets.instance.gigaGalAssets.jumping_left;
            } else if (walkState == WalkState.WALKING) {
                float walking_duration = Utils.secondsSince(walkStartTime);
                textureRegion = (TextureAtlas.AtlasRegion) Assets.instance.gigaGalAssets.walkLeftLoop.getKeyFrame(walking_duration);
            } else {
                textureRegion = Assets.instance.gigaGalAssets.standing_left;
            }
        } else {
            if (jumpState != JumpState.GROUNDED) {
                textureRegion = Assets.instance.gigaGalAssets.jumping_right;
            } else if (walkState == WalkState.WALKING) {
                float walking_duration = Utils.secondsSince(walkStartTime);
                textureRegion = (TextureAtlas.AtlasRegion) Assets.instance.gigaGalAssets.walkRightLoop.getKeyFrame(walking_duration);
            } else {
                textureRegion = Assets.instance.gigaGalAssets.standing_right;
            }
        }

        Utils.drawTextureRegion(
                spriteBatch,
                textureRegion,
                position.x - Constants.GIGAGAL_EYE_POS.x,
                position.y - Constants.GIGAGAL_EYE_POS.y);

        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.draw(spriteBatch);
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
        for (ParticleEffect particleEffect: particleEffects) {
            particleEffect.dispose();
        }
    }
}

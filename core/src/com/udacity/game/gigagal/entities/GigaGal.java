package com.udacity.game.gigagal.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.game.gigagal.Level;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Enums.*;
import com.udacity.game.gigagal.utils.Utils;

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

    public Vector2 position;
    public int ammmo_basic;
    public int ammmo_big;
    public int lives;
    public boolean jumpButtonPressed;
    public boolean leftButtonPressed;
    public boolean rightButtonPressed;
    public boolean dropButtonPressed;

    public GigaGal(Vector2 spawn_position, Level level) {
        this.spawn_position = spawn_position;
        this.level = level;
        init();
    }

    public void init(){
        respawn();
        ammmo_basic = Constants.GIGAGAL_INIT_AMMO;
        ammmo_big = 0;
        lives = Constants.GIGAGAL_INIT_LIVES;
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
            respawn();
        }

        for (Platform platform: platforms) {
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
            Rectangle enemy_bounding_box = new Rectangle(
                    enemy.position.x - Constants.ENEMY_COLLISION_RADIUS,
                    enemy.position.y - Constants.ENEMY_COLLISION_RADIUS,
                    Constants.ENEMY_COLLISION_RADIUS * 2,
                    Constants.ENEMY_COLLISION_RADIUS * 2
            );

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
                moveLeft(delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightButtonPressed) {
                moveRight(delta);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || dropButtonPressed) {
                moveDown(delta);
            } else {
                walkState = WalkState.STANDING;
            }
        }

        level.getPowerups().begin();
        for (Powerup powerup : level.getPowerups()) {
            Rectangle power_bounding_box = new Rectangle(
                    powerup.position.x + Constants.POWERUP_CENTER.x,
                    powerup.position.y + Constants.POWERUP_CENTER.y,
                    Constants.POWERUP_CENTER.x * 2,
                    Constants.POWERUP_CENTER.y * 2
            );

            boolean hit_powerup = gigagal_bounding_box.overlaps(power_bounding_box);
            if (hit_powerup) {
                if (powerup.ammo_type.equals("basic")) {
                    ammmo_basic += Constants.POWERUP_AMOUNT;
                } else if (powerup.ammo_type.equals("big")) {
                    ammmo_big += Constants.POWERUP_AMOUNT;
                }
                level.getPowerups().removeValue(powerup, true);
                level.score += Constants.POWERUP_SCORE;
            }
        }
        level.getPowerups().end();

        level.getDiamonds().begin();
        for (Diamond diamond : level.getDiamonds()) {
            Rectangle diamond_bounding_box = new Rectangle(
                    diamond.position.x,
                    diamond.position.y,
                    Constants.DIAMOND_SIZE.x,
                    Constants.DIAMOND_SIZE.y
            );

            boolean hit_diamond = gigagal_bounding_box.overlaps(diamond_bounding_box);
            if (hit_diamond) {
                level.getDiamonds().removeValue(diamond, true);
                level.score += Constants.DIAMOND_SCORE;
            }
        }
        level.getDiamonds().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.X) && (ammmo_basic > 0 || ammmo_big > 0)) {
            shoot();
        }
    }

    public void shoot() {
        if (ammmo_big > 0) {
            ammmo_big--;
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
            level.getBullets().add(new BigBullet(level, bullet_position, facing));
        } else if (ammmo_basic > 0) {
            ammmo_basic--;
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
            level.getBullets().add(new Bullet(level, bullet_position, facing));
        }
    }

    boolean landedOnPlatform(Platform platform) {
        boolean landed = false;

        if (position_last_frame.y - Constants.GIGAGAL_EYE_HEIGHT >= platform.top &&
                position.y - Constants.GIGAGAL_EYE_HEIGHT <= platform.top) {
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
    }

    private void moveRight(float delta) {
        if (jumpState == JumpState.GROUNDED && walkState != WalkState.WALKING) {
            walkStartTime = TimeUtils.nanoTime();
        }

        walkState = WalkState.WALKING;
        facing = Direction.RIGHT;
        position.x += delta * Constants.GIGAGAL_MOVEMENT_SPEED;
    }

    private void moveDown(float delta) {
        if (jumpState == JumpState.GROUNDED) {
            position.y -= delta * Constants.GIGAGAL_MOVEMENT_SPEED;
            jumpState = JumpState.FALLING;
        }
    }

    private void startJump(Rectangle gigagal_bounding_box, Array<Platform> platforms) {
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

    private void recoilFromEnemy(Direction hitDirection) {
        jumpState = JumpState.RECOILING;
        velocity.y = Constants.GIGAGAL_KNOCKBACK_SPEED.y;

        if (hitDirection == Direction.LEFT) {
            velocity.x = -Constants.GIGAGAL_KNOCKBACK_SPEED.x;
        } else if (hitDirection == Direction.RIGHT) {
            velocity.x = Constants.GIGAGAL_KNOCKBACK_SPEED.x;
        }

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
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                position.x - Constants.GIGAGAL_STANCE_WIDTH,
                position.y - Constants.GIGAGAL_EYE_HEIGHT,
                Constants.GIGAGAL_STANCE_WIDTH * 2,
                Constants.GIGAGAL_HEIGHT
        );
    }
}

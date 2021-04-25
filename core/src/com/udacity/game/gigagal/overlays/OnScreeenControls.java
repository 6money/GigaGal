package com.udacity.game.gigagal.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.game.gigagal.entities.GigaGal;
import com.udacity.game.gigagal.screens.GameplayScreen;
import com.udacity.game.gigagal.utils.Assets;
import com.udacity.game.gigagal.utils.Constants;
import com.udacity.game.gigagal.utils.Utils;

public class OnScreeenControls extends InputAdapter {
    public static final String TAG = OnScreeenControls.class.getName();

    private Vector2 moveLeftCenter = new Vector2();
    private Vector2 moveRightCenter = new Vector2();
    private Vector2 shootCenter = new Vector2();
    private Vector2 jumpCenter = new Vector2();
    private Vector2 dropCenter = new Vector2();
    private Vector2 pauseCenter = new Vector2();
    private int moveLeftPointer;
    private int moveRightPointer;
    private int jumpPointer;
    private int dropPointer;
    private  GameplayScreen gameplayScreen;

    public final Viewport viewport;
    public GigaGal gigaGal;

    public OnScreeenControls(GameplayScreen gameplayScreen) {
        this.viewport = new ExtendViewport(
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE,
                Constants.ONSCREEN_CONTROLS_VIEWPORT_SIZE);
        this.gameplayScreen = gameplayScreen;

        recalculateButtonPositions();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (viewportPosition.dst(shootCenter) < Constants.BUTTON_RADIUS) {
            gigaGal.shoot();

        } else if (viewportPosition.dst(jumpCenter) < Constants.BUTTON_RADIUS) {
            jumpPointer = pointer;
            gigaGal.jumpButtonPressed = true;
        } else if (viewportPosition.dst(dropCenter) < Constants.BUTTON_RADIUS) {
            dropPointer = pointer;
            gigaGal.dropButtonPressed = true;
        } else if (viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS) {
            moveLeftPointer = pointer;
            gigaGal.leftButtonPressed = true;
        } else if (viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS) {
            moveRightPointer = pointer;
            gigaGal.rightButtonPressed = true;
        } else if (viewportPosition.dst(pauseCenter) < Constants.BUTTON_RADIUS / 2) {
            gameplayScreen.level.paused = true;
            Gdx.input.setInputProcessor(gameplayScreen.pauseOverlay);
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 viewportPosition = viewport.unproject(new Vector2(screenX, screenY));

        if (pointer == moveLeftPointer && viewportPosition.dst(moveRightCenter) < Constants.BUTTON_RADIUS) {
            gigaGal.leftButtonPressed = false;
            gigaGal.rightButtonPressed = true;
            moveLeftPointer = 0;
            moveRightPointer = pointer;
        }

        if (pointer == moveRightPointer && viewportPosition.dst(moveLeftCenter) < Constants.BUTTON_RADIUS) {
            gigaGal.leftButtonPressed = true;
            gigaGal.rightButtonPressed = false;
            moveLeftPointer = pointer;
            moveRightPointer = 0;
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    public void render(SpriteBatch batch) {

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (!Gdx.input.isTouched(jumpPointer)) {
            gigaGal.jumpButtonPressed = false;
            jumpPointer = 0;
        }

        if (!Gdx.input.isTouched(dropPointer)) {
            gigaGal.dropButtonPressed = false;
            dropPointer = 0;
        }

        if (!Gdx.input.isTouched(moveLeftPointer)) {
            gigaGal.leftButtonPressed = false;
            moveLeftPointer = 0;
        }

        if (!Gdx.input.isTouched(moveRightPointer)) {
            gigaGal.rightButtonPressed = false;
            moveRightPointer = 0;
        }

        Utils.drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets.move_left,
                moveLeftCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets.move_right,
                moveRightCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets.shoot,
                shootCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets.jump,
                jumpCenter,
                Constants.BUTTON_CENTER
        );

        Utils.drawTextureRegion(
                batch,
                Assets.instance.onscreenControlsAssets.drop,
                dropCenter,
                Constants.BUTTON_CENTER
        );

        batch.end();
    }

    public void debugRender(ShapeRenderer shapeRenderer) {
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.circle(moveLeftCenter.x, moveLeftCenter.y, Constants.BUTTON_RADIUS);
        shapeRenderer.circle(moveRightCenter.x, moveRightCenter.y, Constants.BUTTON_RADIUS);
        shapeRenderer.circle(jumpCenter.x, jumpCenter.y, Constants.BUTTON_RADIUS);
        shapeRenderer.circle(shootCenter.x, shootCenter.y, Constants.BUTTON_RADIUS);
        shapeRenderer.circle(pauseCenter.x, pauseCenter.y, Constants.BUTTON_RADIUS / 2);
        shapeRenderer.circle(dropCenter.x, dropCenter.y, Constants.BUTTON_RADIUS);
    }

    public void recalculateButtonPositions() {
        moveLeftCenter.set(Constants.BUTTON_RADIUS, Constants.BUTTON_RADIUS * 1.5f);
        moveRightCenter.set(Constants.BUTTON_RADIUS * 3, Constants.BUTTON_RADIUS * 3 / 4);
        jumpCenter.set(viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 3, Constants.BUTTON_RADIUS * 3 / 4);
        dropCenter.set(viewport.getWorldWidth() - Constants.BUTTON_RADIUS * 5, Constants.BUTTON_RADIUS * 3 / 4);
        shootCenter.set(viewport.getWorldWidth() - Constants.BUTTON_RADIUS, Constants.BUTTON_RADIUS * 1.5f);
        pauseCenter.set(Constants.BUTTON_RADIUS, viewport.getWorldHeight() - Constants.BUTTON_RADIUS);

    }
}

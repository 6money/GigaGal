package com.udacity.game.gigagal.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.udacity.game.gigagal.entities.GigaGal;

public class ChaseCam {
    private boolean following;

    public Camera chase_cam;
    public GigaGal gigaGal;

    public ChaseCam(Camera camera, GigaGal gigaGal) {
        chase_cam = camera;
        this.gigaGal = gigaGal;
        following = false;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            following = !following;
        }

        if (!following) {
            chase_cam.position.set(gigaGal.position.x, gigaGal.position.y, 0);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                chase_cam.position.x -= delta * Constants.CHASE_CAM_SPEED;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                chase_cam.position.x += delta * Constants.CHASE_CAM_SPEED;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                chase_cam.position.y += delta * Constants.CHASE_CAM_SPEED;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                chase_cam.position.y -= delta * Constants.CHASE_CAM_SPEED;
            }
        }
    }
}

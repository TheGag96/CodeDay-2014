package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity{
    private static final float SPEED = 8;
    private static final float JUMP_SPEED = 10;

    public float jumpTimer = 0;
    public static final float JUMP_TIMER_MAX = 30/60f;

    public boolean jumping = false;

    public Player(float x, float y) {
        super(x, y);
        setTexture(new Texture(Gdx.files.internal("entities/player.png")));
        setRegionWidth(16);
        setRegionHeight(32);
        checksOtherEntities = true;
        setSize(1, 2);
    }

    @Override
    public void performLogic(float deltaTime) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!blockedLeft)
                velX = -SPEED;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!blockedRight)
                velX = SPEED;
        }
        else {
            velX = 0;
        }

        if (blockedDown) {
            jumping = false;
            jumpTimer = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (!Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                if (blockedDown) {
                    jumping = true;
                }

            }
            jumpTimer += deltaTime;
            if (jumpTimer < JUMP_TIMER_MAX) {
                velY = JUMP_SPEED;
            }
        }
        else {
            jumpTimer = JUMP_TIMER_MAX+1;
        }

        velY += GRAVITY;
    }

    @Override
    public void onEntityCollision(int direction, Entity e) {
        if (e instanceof Lever) {
            if (Gdx.input.isKeyPressed(Input.Keys.X) && !Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                ((Lever)e).flipped = !((Lever)e).flipped;
            }
        }

    }
}

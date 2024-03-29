package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Entity{
    private static final float SPEED = 16;
    private static final float JUMP_SPEED = 15;

    public float jumpTimer = 0;
    public static final float JUMP_TIMER_MAX = 15/60f;

    public boolean jumping = false;

    public Player(float x, float y) {
        super(x, y);
        setTexture(new Texture(Gdx.files.internal("entities/player.png")));
        setRegionWidth(16);
        setRegionHeight(32);
        checksOtherEntities = true;
        setSize(1, 2);
    }

    boolean zWasPressed = false;
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

        if (blockedUp)
            jumpTimer = JUMP_TIMER_MAX+1;

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            if (!zWasPressed) {
                zWasPressed = true;
                if (blockedDown) {
                    jumping = true;
                }

            }
            if (jumping) {
                jumpTimer += deltaTime;
                if (jumpTimer < JUMP_TIMER_MAX) {
                    velY = JUMP_SPEED;
                }
            }
        }
        else {
            zWasPressed = false;
            jumpTimer = JUMP_TIMER_MAX+1;
        }

        velY += GRAVITY;
    }

    boolean xWasPressed = false;
    @Override
    public void onEntityCollision(int direction, Entity e) {
        if (e instanceof Lever) {
            if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                if (!xWasPressed) {
                    xWasPressed = true;
                    ((Lever)e).flipped = !((Lever)e).flipped;
                }

            }
            else {
                xWasPressed = false;
            }
        }
        else if (e instanceof Button) {
            ((Button)e).pressed = true;
        }
        else if (e instanceof Door) {
            if (((Door)e).open) {
                MainGame.resultsScreen = true;
                MainGame.win = true;
            }
        }

    }
}

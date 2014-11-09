package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MovableBox extends Entity {

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/button_normal.png"));
    private static final Texture pressedTexture = new Texture(Gdx.files.internal("entities/button_pressed.png"));

    public MovableBox(float x, float y) {
        super(x, y);
        setTexture(normalTexture);
        setSize(2, 2);
        setRegionWidth(32);
        setRegionHeight(32);
        checksOtherEntities = true;
    }

    @Override
    public void performLogic(float deltaTime) {


        velY += GRAVITY;
    }

    @Override
    public void onEntityColliding(int direction, Entity e) {
        if (e instanceof Player) {
            if (direction == RIGHT) {
                newX = e.newX - getWidth();
            }
            else {
                newX = e.newX + e.getWidth();
            }
        }
    }

    @Override
    public void onEntityCollision(int direction, Entity e) {
        if (e instanceof Button) {
            ((Button)e).pressed = true;
        }
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MovableBox extends Entity {

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/movablebox.png"));

    public boolean pushing = false;

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
            if (direction == TOP) {
                System.out.println("hello");
                e.newY = getY() + getWidth();
                e.velY = 0;
                e.blockedDown = true;
            }
            else {
                if (newX - e.newX > 0) {
                    newX = e.newX + e.getWidth();
                }
                else {
                    newX = e.newX - getWidth();
                }
                checkBlockCollisionsX(MainGame.map);
                if (blockedRight) {
                    e.newX = newX - e.getWidth();
                }
                else if (blockedLeft) {
                    e.newX = newX + getWidth();
                }
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

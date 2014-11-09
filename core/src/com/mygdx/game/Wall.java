package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Wall extends Entity {

    private static final Texture normalTexture = new Texture(Gdx.files.internal("entities/wall_normal.png"));
    private static final Texture openTexture = new Texture(Gdx.files.internal("entities/wall_open.png"));

    public int puzzleFlag;

    public boolean open = false;

    public Wall(float x, float y, int puzzleFlag) {
        super(x, y);
        this.puzzleFlag = puzzleFlag;
        setSize(1, 3);
        setRegionWidth(16);
        setRegionHeight(48);
        setTexture(normalTexture);
    }

    @Override
    public void performLogic(float deltaTime) {
        if (MainGame.puzzleFlags.get(puzzleFlag)) {
            open = true;
            setTexture(openTexture);
        }
        else {
            open = false;
            setTexture(normalTexture);
        }
    }

    @Override
    public void onEntityColliding(int direction, Entity e) {
        if (!open) {
            if (direction == RIGHT) {
                e.newX = getX() + getWidth();
                e.velX = 0;
                blockedLeft = true;
            }
            else {
                e.newX = getX() - e.getWidth();
                e.velX = 0;
                e.blockedRight = true;
            }
        }
    }
}
